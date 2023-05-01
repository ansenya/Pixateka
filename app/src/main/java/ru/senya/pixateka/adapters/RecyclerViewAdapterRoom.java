package ru.senya.pixateka.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.net.URI;
import java.util.List;

import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.room.ItemEntity;


public class RecyclerViewAdapterRoom extends RecyclerView.Adapter<RecyclerViewAdapterRoom.MyViewHolder> {

    private final List<ItemEntity> Items;
    private Context context;

    public RecyclerViewAdapterRoom(List<ItemEntity> items, Context context) {
        Items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setImageView(Items.get(position), context);
        holder.setTextView(Items.get(position));
        holder.imageView.setOnLongClickListener(view -> {
            Items.remove(Items.get(position));
            this.notifyDataSetChanged();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imageView;
        TextView textView;
        TextView textView2;
        TextView textView3;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.pic);
            textView = itemView.findViewById(R.id.text);
            textView2 = itemView.findViewById(R.id.sets);
            textView3 = itemView.findViewById(R.id.description);

            textView2.setOnClickListener(view -> {
                Toast.makeText(itemView.getContext(), "sets", Toast.LENGTH_SHORT).show();
            });

        }

        void setImageView(ItemEntity item, Context context) {
                Glide.
                        with(imageView.getContext()).
                        load(item.getPath()).
                        into(imageView);
        }

        void setTextView(ItemEntity item) {
            textView.setText(item.getName());
            textView3.setText("by " + item.getEmail());
        }
    }
}
