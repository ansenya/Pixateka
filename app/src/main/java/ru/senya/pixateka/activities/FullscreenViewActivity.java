package ru.senya.pixateka.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.adapters.RecyclerAdapter;
import ru.senya.pixateka.databinding.FullscreenViewActivityBinding;
import ru.senya.pixateka.models.ImageEntity;
import ru.senya.pixateka.models.Page;

public class FullscreenViewActivity extends AppCompatActivity {

    FullscreenViewActivityBinding binding;
    int width, height;
    String id, uid, path, color, username, title, pfp;
    private final List<ImageEntity> firstData = new LinkedList<>(), secondData = new LinkedList<>();
    private final RecyclerAdapter firstAdapter = new RecyclerAdapter(firstData, this), secondAdapter = new RecyclerAdapter(secondData, this);
    int firstPage = 0, secondPage = 0;
    int firstTotalPages = 1, secondTotalPages = 1;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendClickedRequest();

        binding = FullscreenViewActivityBinding.inflate(getLayoutInflater());

        loadImage();
        setupToolbar();
        setupClickListeners();
        getFirstData(firstPage);
        getSecondData(secondPage);
        setupRecycler();
        setupPopupMenu();
        setContentView(binding.getRoot());
    }

    private void sendClickedRequest() {
        String token = "Bearer " + App.getSharedPreferences().getString("jwt_key", "");
        id = Objects.requireNonNull(getIntent().getExtras().getString("id"));
        App.getItemService().increaseClick(token, id).enqueue(new Callback<ImageEntity>() {
            @Override
            public void onResponse(Call<ImageEntity> call, Response<ImageEntity> response) {
                if (response.isSuccessful()){
                    Log.i("Click", "ok");
                } else {
                    Log.i("Click", "not_ok " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ImageEntity> call, Throwable t) {
                Toast.makeText(FullscreenViewActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadImage() {
        path = Objects.requireNonNull(getIntent().getExtras()).getString("path");
        color = getIntent().getExtras().getString("color");
        width = (int) Double.parseDouble(Objects.requireNonNull(getIntent().getExtras().getString("width")));
        height = (int) Double.parseDouble(Objects.requireNonNull(getIntent().getExtras().getString("height")));
        uid = Objects.requireNonNull(getIntent().getExtras().getString("uid"));
        username = Objects.requireNonNull(getIntent().getExtras().getString("username"));
        title = Objects.requireNonNull(getIntent().getExtras().getString("title"));
        pfp = Objects.requireNonNull(getIntent().getExtras().getString("pfp"));

        binding.included.image.setTransitionName(id);
        binding.byUser.setText(username);
        Glide
                .with(getBaseContext())
                .load(path)
                .placeholder(getPlaceholder(width, height, color))
                .into(binding.included.image);

        Glide
                .with(getBaseContext())
                .load(pfp)
                .into(binding.pfp);

    }


    private void getFirstData(int numPage) {
        if (numPage < firstTotalPages) {
            firstPage++;
            String token = "Bearer " + App.getSharedPreferences().getString("jwt_key", "");
            App.getItemService().getAlike(token, id, numPage).enqueue(new Callback<Page<ImageEntity>>() {
                @Override
                public void onResponse(@NonNull Call<Page<ImageEntity>> call, @NonNull Response<Page<ImageEntity>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getTotalPages() > firstPage) {
                            firstTotalPages = firstPage + 1;
                        }
                        firstData.addAll(Arrays.asList(response.body().getContent()));
                        if (firstPage == 1) {
                            Objects.requireNonNull(binding.recycler.getAdapter()).notifyItemRangeInserted(0, 10);
                        } else {
                            Objects.requireNonNull(binding.recycler.getAdapter()).notifyItemRangeInserted(firstData.size() - 1, 10);
                        }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<Page<ImageEntity>> call, @NonNull Throwable t) {

                }
            });

        }
    }

    private void getSecondData(int numPage) {
        if (numPage < secondTotalPages) {
            secondPage++;
            String token = "Bearer " + App.getSharedPreferences().getString("jwt_key", "");
            App.getItemService().getByUid(token, App.getSharedPreferences().getString("user_id", ""), id, numPage).enqueue(new Callback<Page<ImageEntity>>() {
                @Override
                public void onResponse(@NonNull Call<Page<ImageEntity>> call, @NonNull Response<Page<ImageEntity>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getTotalPages() > secondPage) {
                            secondTotalPages = secondPage + 1;
                        }
                        secondData.addAll(Arrays.asList(response.body().getContent()));
                        if (secondPage == 1) {
                            Objects.requireNonNull(binding.recycler.getAdapter()).notifyItemRangeInserted(0, 10);
                        } else {
                            Objects.requireNonNull(binding.recycler.getAdapter()).notifyItemRangeInserted(secondData.size() - 1, 10);
                        }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<Page<ImageEntity>> call, @NonNull Throwable t) {

                }
            });
        }
    }

    private void setupClickListeners() {
        binding.included.image.setOnClickListener(view -> {
            startActivity(new Intent(getBaseContext(), SubSampActivity.class)
                    .putExtra("w", width)
                    .putExtra("h", height)
                    .putExtra("color", color)
                    .putExtra("path", path));
        });
    }

    private void setupPopupMenu() {
        binding.included.sets.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(getBaseContext(), binding.included.sets);

            if (uid.equals(App.getSharedPreferences().getString("user_id", ""))) {
                popupMenu.inflate(R.menu.p_menu);
            } else {
                popupMenu.inflate(R.menu.menu);
            }

            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.download:
                        try {
                            MediaStore.Images.Media.insertImage(getBaseContext().getContentResolver(),
                                    BitmapFactory.decodeStream(new URL(path).openConnection().getInputStream()),
                                    "image.getName()", String.valueOf(java.time.LocalDateTime.now()));

                            Snackbar.make(binding.getRoot(), getBaseContext().getString(R.string.image_ready), Snackbar.LENGTH_LONG).show();

                        } catch (Exception e) {
                            Snackbar.make(binding.getRoot(), getBaseContext().getString(R.string.error_idk), Snackbar.LENGTH_LONG).show();
                        }
                        return true;
                    case R.id.share:
                        ClipboardManager clipboard = (ClipboardManager) getBaseContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("url", path);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getBaseContext(), "скопировано", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.delete:
                        App.getItemService().delete("Bearer " + App.getSharedPreferences().getString("jwt_key", ""), id)
                                .enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                                        if (response.isSuccessful()) {
                                            Snackbar.make(binding.getRoot(), getBaseContext().getString(R.string.image_deleted), Snackbar.LENGTH_LONG).show();
                                            onBackPressed();
                                        }
                                        if (response.code() == 400) {
                                            Snackbar.make(binding.getRoot(), getBaseContext().getString(R.string.error_idk), Snackbar.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                        Snackbar.make(binding.getRoot(), getBaseContext().getString(R.string.error_idk), Snackbar.LENGTH_LONG).show();
                                    }
                                });
                }
                return false;
            });

            popupMenu.show();
        });


    }

    private void setupToolbar(){
        binding.toolbar.setTitle(title);
        binding.toolbar.setNavigationOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void setupRecycler() {
        binding.recycler.setAdapter(firstAdapter);
        binding.recycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                assert layoutManager != null;
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int[] firstVisibleItemPositions = layoutManager.findFirstVisibleItemPositions(null);
                int firstVisibleItemPosition = firstVisibleItemPositions[0];

                Log.d("MyTag", String.valueOf(visibleItemCount));
                Log.d("MyTag", String.valueOf(totalItemCount));
                Log.d("MyTag", String.valueOf(visibleItemCount + firstVisibleItemPosition));
                Log.d("MyTag", Arrays.toString(firstVisibleItemPositions));
                Log.d("MyTag", String.valueOf(firstVisibleItemPosition));

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                    getFirstData(firstPage);
                }
            }
        });

        binding.tabs.addTab(binding.tabs.newTab().setText("Похожие"));
        binding.tabs.addTab(binding.tabs.newTab().setText("Все"));

        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    binding.recycler.setAdapter(firstAdapter);
                } else {
                    binding.recycler.setAdapter(secondAdapter);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                binding.recycler.scrollToPosition(View.FOCUS_UP);
            }
        });
    }

    private BitmapDrawable getPlaceholder(int width, int height, String color) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        width,
                        height,
                        Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.parseColor(color));
        return new BitmapDrawable(getBaseContext().getResources(), bitmap);
    }
}
