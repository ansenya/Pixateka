package ru.senya.pixateka.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.LinkedList;
import java.util.List;

import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.activities.FullscreenViewActivity;
import ru.senya.pixateka.activities.LoginActivity;
import ru.senya.pixateka.activities.RegistrationActivity;
import ru.senya.pixateka.database.room.ItemEntity;
import ru.senya.pixateka.databinding.ViewItemBinding;
import ru.senya.pixateka.models.ImageEntity;

public class NewRecyclerAdapter extends RecyclerView.Adapter<NewRecyclerAdapter.ViewHolder> {

    List<ImageEntity> data;

    public NewRecyclerAdapter(LinkedList<ImageEntity> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public NewRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewRecyclerAdapter.ViewHolder holder, int position) {
        holder.loadImage(data.get(position));
        holder.setupClickListener(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ViewItemBinding binding;
        private final Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ViewItemBinding.bind(itemView);
            context = itemView.getContext();
        }

        public void loadImage(ImageEntity item) {
            binding.image.startAnimation(AnimationUtils.loadAnimation(context, R.anim.wave_animation));

            Glide
                    .with(context)
                    .load(item.getPath())
                    .placeholder(getPlaceholder(item))
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            binding.image.clearAnimation();
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            binding.image.clearAnimation();
                            return false;
                        }
                    })
                    .override(480)
                    .into(binding.image);

            binding.imageName.setText(item.getName());

        }

        public void setupClickListener(ImageEntity image) {
            binding.image.setOnClickListener(view -> {
                Bundle extras = new Bundle();
                extras.putString("path", image.getPath());
                extras.putString("color", image.getHexColor());
                extras.putString("width", String.valueOf(image.getWidth()));
                extras.putString("height", String.valueOf(image.getHeight()));

                context.startActivity(new Intent(context, FullscreenViewActivity.class).putExtras(extras));
            });
        }

        private BitmapDrawable getPlaceholder(ImageEntity image) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            (int) image.getWidth(),
                            (int) image.getHeight(),
                            Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.parseColor(image.getHexColor()));
            return new BitmapDrawable(context.getResources(), bitmap);
        }

    }
}
