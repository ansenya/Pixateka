package ru.senya.pixateka.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.time.LocalDateTime;

import ru.senya.pixateka.R;
import ru.senya.pixateka.databinding.ViewFullscreenBinding;
import ru.senya.pixateka.database.room.ItemEntity;

public class viewFullscreen extends NestedScrollView {

    ViewFullscreenBinding binding;
    Context context;
    ItemEntity itemEntity;
    private FragmentActivity activity;


    public viewFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        binding = ViewFullscreenBinding.inflate(LayoutInflater.from(getContext()), this, true);
        binding.sets.setOnClickListener(v -> {
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
        binding.pic.setOnClickListener(v -> {

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


    public void update(ItemEntity item, FragmentActivity activity) {
        this.itemEntity = item;
        this.activity = activity;
        Glide.
                with(context).
                load(item.getPath()).
                into(binding.pic);
        binding.text.setText(item.getName());
    }

    public void goUp() {
        scrollTo(0, 0);
    }

}
