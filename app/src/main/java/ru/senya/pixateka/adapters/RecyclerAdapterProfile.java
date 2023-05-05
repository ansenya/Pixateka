package ru.senya.pixateka.adapters;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import ru.senya.pixateka.R;
import ru.senya.pixateka.database.room.ItemEntity;


public class RecyclerAdapterProfile extends RecyclerView.Adapter<RecyclerAdapterProfile.ViewHolder> {

    ArrayList<ItemEntity> data;

    public RecyclerAdapterProfile(ArrayList<ItemEntity> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerAdapterProfile.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
