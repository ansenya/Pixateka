package ru.senya.pixateka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Item> Items;

    public MyAdapter(List<Item> items) {
        Items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setImageView(Items.get(position));
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView ImageView;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ImageView = itemView.findViewById(R.id.pic);
        }
        void setImageView(Item item) {
            ImageView.setImageResource(item.getPic());
        }
    }
}
