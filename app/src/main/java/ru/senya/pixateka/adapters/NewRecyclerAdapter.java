package ru.senya.pixateka.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.List;

import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.database.room.ItemEntity;
import ru.senya.pixateka.databinding.ViewItemBinding;

public class NewRecyclerAdapter extends RecyclerView.Adapter<NewRecyclerAdapter.ViewHolder> {

    List<ItemEntity> data = App.getData();

    @NonNull
    @Override
    public NewRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewRecyclerAdapter.ViewHolder holder, int position) {
        holder.loadImage(data.get(position));
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

        public void loadImage(ItemEntity item) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            Integer.parseInt(item.width),
                            Integer.parseInt(item.height),
                            Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.parseColor(item.color));
            BitmapDrawable placeholder = new BitmapDrawable(context.getResources(), bitmap);

            Glide.with(context)
                    .load(item.getPath())
                    .placeholder(placeholder)
                    .into(binding.image);

            binding.imageName.setText(item.getName());
        }

    }
}
