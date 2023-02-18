package ru.senya.pixateka.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import ru.senya.pixateka.subjects.Item;
import ru.senya.pixateka.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {



    private List<Item> Items;
    public MyAdapter(List<Item> items) {
        Items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setImageView(Items.get(position));
        holder.setTextView(Items.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView ImageView;
        TextView textView;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ImageView = itemView.findViewById(R.id.pic);
            textView = itemView.findViewById(R.id.text);
        }
        void setImageView(Item item) {
            ImageView.setImageResource(item.getPic());
        }
        void setTextView(String s){
            textView.setText(s);
        }
    }
}
