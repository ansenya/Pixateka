package ru.senya.pixateka.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.adapters.RecyclerAdapterSecondary;
import ru.senya.pixateka.database.retrofit.itemApi.Item;
import ru.senya.pixateka.databinding.ViewFullscreenBinding;
import ru.senya.pixateka.database.room.ItemEntity;

public class viewFullscreen extends NestedScrollView {

    ViewFullscreenBinding binding;
    Context context;
    ItemEntity itemEntity;
    private FragmentActivity activity;
    int[] colors = {R.color.v1, R.color.v2, R.color.v3, R.color.v4, R.color.v5};
    Random random = new Random();
    LinkedList<ItemEntity> data = new LinkedList<>();

    public viewFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        binding = ViewFullscreenBinding.inflate(LayoutInflater.from(getContext()), this, true);
        binding.included.sets.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.inflate(R.menu.menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
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
                }
            });
            popupMenu.show();
        });
//        binding.included.mainImage.setOnClickListener(v -> {
//            binding.mainFragment.setVisibility(GONE);
//            binding.container.setVisibility(VISIBLE);
//            binding.scalableImage.setImage(ImageSource.resource(colors[random.nextInt(colors.length)]));
//            Glide.
//                    with(context).
//                    asBitmap().
//                    load(itemEntity.getPath()).
//                    placeholder(colors[random.nextInt(colors.length)]).
//                    into(new CustomTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                            binding.scalableImage.setImage(ImageSource.bitmap(resource));
//                        }
//
//                        @Override
//                        public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                        }
//                    });
//        });
        binding.list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }


    @Override
    public boolean fullScroll(int direction) {
        return super.fullScroll(direction);
    }


    public void update(ItemEntity item, FragmentActivity activity) {
        goUp();
        if (binding.list.getAdapter() == null) {
            binding.list.setAdapter(new RecyclerAdapterSecondary(data, getContext(), activity, this));
        }
        data.clear();
        this.itemEntity = item;
        this.activity = activity;
        Glide.
                with(context).
                load(item.getPath()).
                placeholder(colors[random.nextInt(colors.length)]).
                useAnimationPool(true).
                into(binding.included.mainImage);
        binding.included.imageName.setText(item.getName());
        binding.mainDescription.setText(item.getDescription());
        binding.pfp.setImageResource(R.drawable.a21);
        new Thread(() -> {
            App.getItemService().getPhotosByUserId(1).enqueue(new Callback<ArrayList<Item>>() {
                @Override
                public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                    if (response.body() != null) {
                        for (Item item1 : response.body()) {
                            data.add(new ItemEntity(item1.id, item1.author, item1.image, item1.name, item1.description, item1.author, item1.tags));
                            activity.runOnUiThread(() -> {
                                binding.list.getAdapter().notifyItemChanged(data.size() - 1);
                            });
                        }

                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Item>> call, Throwable t) {
                    Toast.makeText(activity, "download failed", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    public void goUp() {
        scrollTo(0, 0);
    }

}
