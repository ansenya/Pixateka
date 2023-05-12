package ru.senya.pixateka.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.LinkedList;
import java.util.Random;

import ru.senya.pixateka.R;
import ru.senya.pixateka.database.room.ItemEntity;
import ru.senya.pixateka.view.viewFullscreen;

public class RecyclerAdapterSearch extends RecyclerView.Adapter<RecyclerAdapterSearch.MyViewHolder> {

    LinkedList<ItemEntity> data;
    viewFullscreen fragment;
    FragmentActivity activity;
    RelativeLayout container;
    Toolbar toolbar;

    public RecyclerAdapterSearch(LinkedList<ItemEntity> data, viewFullscreen fragment, FragmentActivity activity, RelativeLayout container, Toolbar toolbar) {
        this.data = data;
        this.fragment = fragment;
        this.activity = activity;
        this.container = container;
        this.toolbar = toolbar;
    }

    @NonNull
    @Override
    public RecyclerAdapterSearch.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterSearch.MyViewHolder holder, int position) {
        holder.setImage(data.get(position));
        holder.mainImage.setOnClickListener(v -> {
            fragment.update(data.get(position), activity);
            fragment.goUp();
            fragment.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            container.setVisibility(View.INVISIBLE);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView mainImage;
        TextView imageName, imageDescription;
        Context context;
        int[] colors = {R.color.v1, R.color.v2, R.color.v3, R.color.v4, R.color.v5};
        Random random = new Random();

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            mainImage = itemView.findViewById(R.id.main_image);
            imageName = itemView.findViewById(R.id.image_name);
            imageDescription = itemView.findViewById(R.id.description);
        }

        public void setImage(ItemEntity item) {
            Glide.
                    with(context).
                    load(item.getPath()).
                    placeholder(colors[random.nextInt(colors.length)]).
                    into(mainImage);
            imageName.setText(item.getName());
            imageDescription.setText(item.getDescription());
        }
    }
}
