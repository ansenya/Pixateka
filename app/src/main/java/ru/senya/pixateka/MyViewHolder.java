package ru.senya.pixateka;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView pic;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        pic = itemView.findViewById(R.id.pic);
    }
}
