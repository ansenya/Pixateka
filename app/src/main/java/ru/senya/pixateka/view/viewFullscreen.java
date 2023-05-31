package ru.senya.pixateka.view;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.activities.ProfileActivity;
import ru.senya.pixateka.adapters.RecyclerAdapterSecondary;
import ru.senya.pixateka.database.retrofit.userApi.User;
import ru.senya.pixateka.database.room.ItemEntity;
import ru.senya.pixateka.databinding.ViewFullscreenBinding;

public class viewFullscreen extends NestedScrollView {

    public ViewFullscreenBinding binding;
    Context context;
    ItemEntity itemEntity;
    private FragmentActivity activity;
    int[] colors = {R.color.v1, R.color.v2, R.color.v3, R.color.v4, R.color.v5};
    Random random = new Random();
    viewFullscreen viewFullscreen;
    LinkedList<ItemEntity> data = new LinkedList<>();
    LinkedList<ItemEntity> likeData = new LinkedList<>();

    Stack<ItemEntity> previous = new Stack<>();

    public viewFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        binding = ViewFullscreenBinding.inflate(LayoutInflater.from(getContext()), this, true);
        binding.included.sets.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.inflate(R.menu.menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.download:
                        Toast.makeText(context, "downloading...", Toast.LENGTH_SHORT).show();
                        new Thread(() -> {
                            try {
                                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                                        BitmapFactory.decodeStream(new URL(itemEntity.getPath()).openConnection().getInputStream()),
                                        itemEntity.getName(), itemEntity.getDescription() + LocalDateTime.now());

                                activity.runOnUiThread(() -> {
                                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                                });

                            } catch (Exception e) {
                                Toast.makeText(context, "smth wrong", Toast.LENGTH_SHORT).show();
                            }

                        }).start();

                        return true;
                    case R.id.share:
                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("url", itemEntity.getPath());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(context, "copied to clipboard", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            });
            popupMenu.show();
        });
        binding.included.mainImage.setOnLongClickListener(v -> {
            binding.included.sets.callOnClick();
            return true;
        });
        binding.tabs.addTab(binding.tabs.newTab().setText("Фотографии пользователя"));
        binding.tabs.addTab(binding.tabs.newTab().setText("Похожие фотографии"));
        viewFullscreen = this;
        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals("Фотографии пользователя")) {
                    binding.list.setVisibility(VISIBLE);
                    binding.list2.setVisibility(INVISIBLE);
                    binding.nothingWasFound.setVisibility(INVISIBLE);
                } else if (tab.getText().toString().equals("Похожие фотографии")) {
                    binding.list2.setVisibility(VISIBLE);
                    binding.list.setVisibility(INVISIBLE);
                    if (likeData.size() == 0) {
                        binding.nothingWasFound.setVisibility(VISIBLE);
                    }

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.list2.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.pfp.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ProfileActivity.class);
            String uid = itemEntity.uid;
            intent.putExtra("uid", uid);
            activity.startActivity(intent);
        });
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }


    @Override
    public boolean fullScroll(int direction) {
        return super.fullScroll(direction);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void privateUpdate(ItemEntity item, FragmentActivity activity) {
        binding.pfp.setImageResource(R.drawable.pfp);
        binding.nothingWasFound.setVisibility(INVISIBLE);
        goUp();
        if (binding.list.getAdapter() == null) {
            binding.list.setAdapter(new RecyclerAdapterSecondary(data, getContext(), activity, this));
        }
        if (binding.list2.getAdapter() == null) {
            binding.list2.setAdapter(new RecyclerAdapterSecondary(likeData, getContext(), activity, this));
        }
        App.getUserService().getUser(Integer.parseInt(item.uid), App.getMainUser().token, "csrftoken=" + App.getMainUser().token + "; " + "sessionid=" + App.getMainUser().sessionId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    binding.byUser.setText("by " + response.body().username);
                } else {
                    binding.byUser.setText("");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                binding.byUser.setText("");
            }
        });
        binding.list.getAdapter().notifyDataSetChanged();
        binding.list2.getAdapter().notifyDataSetChanged();
        likeData.clear();
        data.clear();
        this.itemEntity = item;
        this.activity = activity;
        Bitmap bitmap = Bitmap.createBitmap(Integer.parseInt(item.width), Integer.parseInt(item.height), Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.parseColor(item.color));
        Glide.
                with(context).
                load(item.getPath()).
                placeholder(new BitmapDrawable(activity.getResources(), bitmap)).
                dontAnimate().
                apply(new RequestOptions().override(1500, 1500)).
                into(binding.included.mainImage);
        if (item.getTags().isEmpty() || item.getName().equals("43083945")){
            binding.tags.setText("");
        } else {
            binding.tags.setText("ИИ: "+ item.getTags());
        }
        if (item.getName().equals("43083945")) {
            if (!item.tags.split(" ")[0].trim().isEmpty()) {
                binding.included.imageName.setText("ИИ: " + item.tags.split(" ")[0]);
            } else {
                binding.included.imageName.setText("Ничего нет");
            }
            binding.included.imageName.setTypeface(Typeface.MONOSPACE);
        } else {
            binding.included.imageName.setTypeface(Typeface.DEFAULT);
            binding.included.imageName.setText(item.getName());
        }
        if (item.description==null){
            binding.mainDescription.setVisibility(GONE);
        } else {
            binding.mainDescription.setVisibility(VISIBLE);
            binding.mainDescription.setText(item.description);
        }
        if (item.getDescription()==null && (item.getTags().isEmpty()|| item.getName().equals("43083945"))) {
            binding.mainLinear.setVisibility(GONE);
        } else {
            binding.mainLinear.setVisibility(VISIBLE);
        }
        App.getUserService().getUser(Integer.parseInt(item.uid), App.getMainUser().token, "csrftoken=" + App.getMainUser().token + "; " + "sessionid=" + App.getMainUser().sessionId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body()!=null){
                    activity.runOnUiThread(()->{
                        Glide.
                                with(context).
                                load(response.body().avatar).
                                into(binding.pfp);
                    });
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });


        new Thread(() -> {
            for (ItemEntity entity : App.getDatabase().itemDAO().getAll()) {
                for (String tags : entity.tags.split(" ")) {
                    if (itemEntity.tags.contains(tags) && !entity.tags.trim().isEmpty() && entity.id != itemEntity.id) {
                        likeData.add(entity);
                        break;
                    }
                }
            }
            activity.runOnUiThread(() -> {
                if (likeData.size() == 0 && binding.list2.getVisibility() == VISIBLE) {
                    binding.nothingWasFound.setVisibility(VISIBLE);
                } else {
                    binding.list2.getAdapter().notifyDataSetChanged();
                }
            });
        }).start();
        new Thread(() -> {
            data.addAll(App.getDatabase().itemDAO().getAllOtherPictures(Integer.parseInt(item.uid), item.id));
            activity.runOnUiThread(() -> {
                binding.list.getAdapter().notifyDataSetChanged();
            });
        }).start();
    }

    public void update(ItemEntity item, FragmentActivity activity) {
        previous.add(item);
        binding.tabs.selectTab(binding.tabs.getTabAt(0));
        Log.e("MyTag", previous.toString());
        privateUpdate(item, activity);
    }

    public void goUp() {
        scrollTo(0, 0);
    }

    public boolean pop() {
        if (previous.size() < 2) {
            previous.clear();
            return true;
        } else {
            previous.pop();
            privateUpdate(previous.peek(), activity);
            binding.tabs.selectTab(binding.tabs.getTabAt(0));
            Log.e("MyTag", previous.toString());
        }
        return false;
    }

    public void fullUpdate() {
        previous.clear();
    }
}