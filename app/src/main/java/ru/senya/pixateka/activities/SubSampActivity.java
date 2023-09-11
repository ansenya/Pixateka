package ru.senya.pixateka.activities;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;

import ru.senya.pixateka.databinding.ActivitySubsamplingBinding;

public class SubSampActivity extends AppCompatActivity {

    ActivitySubsamplingBinding binding;
    int w = 0, h = 0;
    String path, color;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubsamplingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        path = getIntent().getStringExtra("path");

        try {


            w = getIntent().getIntExtra("w", 0);
            h = getIntent().getIntExtra("h", 0);
            color = getIntent().getStringExtra("color");



            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.parseColor(color));
            binding.pic.setImage(ImageSource.bitmap(bitmap));
        } catch (Exception ignored){}

        Glide
                .with(getApplicationContext())
                .asBitmap()
                .load(path)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        binding.pic.setImage(ImageSource.bitmap(resource));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        onBackPressed();
                    }
                });

        binding.back.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}
