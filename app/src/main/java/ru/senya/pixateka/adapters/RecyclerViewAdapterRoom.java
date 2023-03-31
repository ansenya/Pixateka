package ru.senya.pixateka.adapters;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.net.URI;
import java.util.List;

import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.room.ItemEntity;
import ru.senya.pixateka.subjects.Item;

public class RecyclerViewAdapterRoom extends RecyclerView.Adapter<RecyclerViewAdapterRoom.MyViewHolder> {

    private final List<ItemEntity> Items;
    Uri uri = Uri.parse(
            "content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F1000000033/ORIGINAL/NONE/image%2Fjpeg/155019756");

    public RecyclerViewAdapterRoom(List<ItemEntity> items) {
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

    public void my(){

    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView ImageView;
        TextView textView;
        TextView textView2;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ImageView = itemView.findViewById(R.id.pic);
            textView = itemView.findViewById(R.id.text);
            textView2 = itemView.findViewById(R.id.sets);

            textView2.setOnClickListener(view -> {
                Toast.makeText(itemView.getContext(), "sets", Toast.LENGTH_SHORT).show();
            });
        }

        void setImageView(ItemEntity item) {
            if (item.getPic() == 0) {
                try {
                    ImageView.setImageBitmap(BitmapFactory.decodeFile(item.getPath()));
                } catch (Exception e){Log.e("MyTag3", e.getLocalizedMessage());}

            } else
                ImageView.setImageResource(item.getPic());
        }

        void setTextView(String s) {
            textView.setText(s);
        }
    }
}
