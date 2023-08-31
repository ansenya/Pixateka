package ru.senya.pixateka.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import javax.microedition.khronos.opengles.GL;

import ru.senya.pixateka.databinding.FullscreenViewActivityBinding;

public class FullscreenViewActivity extends AppCompatActivity {

    FullscreenViewActivityBinding binding;
    int width, height;
    String path, color;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FullscreenViewActivityBinding.inflate(getLayoutInflater());

        loadImage();

        setupClickListeners();


        setContentView(binding.getRoot());
    }

    private void loadImage() {
        path = getIntent().getExtras().getString("path");
        color = getIntent().getExtras().getString("color");
        width = (int) Double.parseDouble(getIntent().getExtras().getString("width"));
        height = (int) Double.parseDouble(getIntent().getExtras().getString("height"));

        Glide
                .with(getBaseContext())
                .load(path)
                .placeholder(getPlaceholder(width, height, color))
                .override(500)
                .into(binding.included.image);
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
