package ru.senya.pixateka.adapters;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.databinding.ViewFullscreenBinding;
import ru.senya.pixateka.room.ItemEntity;
import ru.senya.pixateka.view.viewFullscreen;


public class RecyclerViewAdapterRoom extends RecyclerView.Adapter<RecyclerViewAdapterRoom.MyViewHolder> {

    private final List<ItemEntity> Items;
    private Context context;
    private View.OnClickListener onClickListener;

    ru.senya.pixateka.view.viewFullscreen viewFullscreen;
    RecyclerView recyclerView;
    Toolbar mainToolbar, toolbar;
    FloatingActionButton floatingActionButton;
    FragmentActivity activity;

    public RecyclerViewAdapterRoom(FragmentActivity activity,
                                   List<ItemEntity> items,
                                   Context context,
                                   View.OnClickListener onClickListener,
                                   ru.senya.pixateka.view.viewFullscreen viewFullscreen,
                                   RecyclerView recyclerView,
                                   Toolbar toolbar,
                                   FloatingActionButton floatingActionButton,
                                   Toolbar toolbar2) {
        this.activity = activity;
        Items = items;
        this.context = context;
        this.onClickListener = onClickListener;
        this.viewFullscreen = viewFullscreen;
        this.recyclerView = recyclerView;
        this.mainToolbar = toolbar;
        this.floatingActionButton = floatingActionButton;
        this.toolbar = toolbar2;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int p = position;
        holder.setImageView(Items.get(position), context);
        holder.setTextView(Items.get(position));

        holder.textView2.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.inflate(R.menu.menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.download:
                            Toast.makeText(context, "downloading...", Toast.LENGTH_SHORT).show();
                            new Thread(() -> {
                                try {
                                    MediaStore.Images.Media.insertImage(context.getContentResolver(),
                                            BitmapFactory.decodeStream(new URL(Items.get(p).getPath()).openConnection().getInputStream()),
                                            Items.get(p).getName(), Items.get(p).getDescription()+java.time.LocalDateTime.now());

                                    activity.runOnUiThread(() -> {
                                        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                                    });

                                } catch (IOException e) {
                                    Toast.makeText(context, "smth wrong", Toast.LENGTH_SHORT).show();
                                }

                            }).start();

                            return true;
                        case R.id.share:
                            ClipboardManager clipboard =  (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("url", Items.get(p).getPath());
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(context, "copied to clipboard", Toast.LENGTH_SHORT).show();
                            return true;


                    }
                    return false;
                }
            });

            popupMenu.show();
        });

        holder.imageView.setOnClickListener(v -> {
            viewFullscreen.setVisibility(VISIBLE);
            recyclerView.setVisibility(GONE);
            mainToolbar.setVisibility(View.INVISIBLE);
            toolbar.setVisibility(VISIBLE);
            floatingActionButton.setVisibility(GONE);
            viewFullscreen.update(Items.get(position));
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
            textView3 = itemView.findViewById(R.id.description);
            textView2 = itemView.findViewById(R.id.sets);

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
