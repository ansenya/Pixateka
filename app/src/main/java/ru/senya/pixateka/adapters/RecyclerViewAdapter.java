package ru.senya.pixateka.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import ru.senya.pixateka.R;
import ru.senya.pixateka.subjects.Item;

public class    RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private final List<Item> Items;
    private Context context;
    public RecyclerViewAdapter(List<Item> items) {
        Items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setImageView(Items.get(position), context );
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
        void setImageView(Item item, Context context) {
            try{

            } catch (Exception e){

            }


        }
        void setTextView(String s){
            textView.setText(s);
        }
    }
}
