package ru.senya.pixateka.adapters;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.activities.FullscreenViewActivity;
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
        holder.setupClickListener(position, data, this);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ViewItemBinding binding;
        private final Context context;
        SharedPreferences preferences;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ViewItemBinding.bind(itemView);
            context = itemView.getContext();
            preferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
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

        @SuppressLint("NonConstantResourceId")
        public void setupClickListener(int position, List<ImageEntity> data, NewRecyclerAdapter adapter) {

            ImageEntity image = data.get(position);

            binding.image.setOnClickListener(view -> {
                Bundle extras = new Bundle();
                extras.putString("path", image.getPath());
                extras.putString("color", image.getHexColor());
                extras.putString("width", String.valueOf(image.getWidth()));
                extras.putString("height", String.valueOf(image.getHeight()));

                context.startActivity(new Intent(context, FullscreenViewActivity.class).putExtras(extras));
            });

            binding.sets.setOnClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(context, view);
                if (image.getUser().getId().equals(preferences.getString("user_id", ""))) {
                    popupMenu.inflate(R.menu.p_menu);
                } else {
                    popupMenu.inflate(R.menu.menu);
                }

                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.download:
                            try {
                                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                                        BitmapFactory.decodeStream(new URL(image.getPath()).openConnection().getInputStream()),
                                        image.getName(), image.getTags() + java.time.LocalDateTime.now());

                                Snackbar.make(binding.getRoot(), context.getString(R.string.image_ready), Snackbar.LENGTH_LONG).show();

                            } catch (Exception e) {
                                Snackbar.make(binding.getRoot(), context.getString(R.string.error_idk), Snackbar.LENGTH_LONG).show();
                            }
                            return true;
                        case R.id.share:
                            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("url", image.getPath());
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(context, "скопировано", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.delete:
                            App.getItemService().delete("Bearer " + preferences.getString("jwt_key", ""), image.getId())
                                    .enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                                            if (response.isSuccessful()) {
                                                Snackbar.make(binding.getRoot(), context.getString(R.string.image_deleted), Snackbar.LENGTH_LONG).show();
                                                data.remove(position);
                                                adapter.notifyItemRemoved(position);
                                            } if (response.code() == 400) {
                                                Snackbar.make(binding.getRoot(), context.getString(R.string.error_idk), Snackbar.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                            Snackbar.make(binding.getRoot(), context.getString(R.string.error_idk), Snackbar.LENGTH_LONG).show();
                                        }
                                    });
                    }
                    return false;
                });
                popupMenu.show();
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
