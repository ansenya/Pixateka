package ru.senya.pixateka.adapters;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.net.URL;
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
        holder.sets.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(fragment.getContext(), v);
            popupMenu.inflate(R.menu.menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.download:
                            Toast.makeText(fragment.getContext(), "downloading...", Toast.LENGTH_SHORT).show();
                            new Thread(() -> {
                                try {
                                    MediaStore.Images.Media.insertImage(fragment.getContext().getContentResolver(),
                                            BitmapFactory.decodeStream(new URL(data.get(position).getPath()).openConnection().getInputStream()),
                                            data.get(position).getName(), data.get(position).getDescription() + java.time.LocalDateTime.now());

                                    activity.runOnUiThread(() -> {
                                        Toast.makeText(fragment.getContext(), "success", Toast.LENGTH_SHORT).show();
                                    });

                                } catch (Exception e) {
                                    Toast.makeText(fragment.getContext(), "smth wrong", Toast.LENGTH_SHORT).show();
                                }

                            }).start();

                            return true;
                        case R.id.share:
                            ClipboardManager clipboard = (ClipboardManager) fragment.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("url", data.get(position).getPath());
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(fragment.getContext(), "copied to clipboard", Toast.LENGTH_SHORT).show();
                            return true;


                    }
                    return false;
                }
            });
            popupMenu.show();
        });
        holder.mainImage.setOnLongClickListener(v -> {
            holder.sets.callOnClick();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView mainImage;
        TextView imageName, imageDescription;
        ImageView sets;
        Context context;
        int[] colors = {R.color.v1, R.color.v2, R.color.v3, R.color.v4, R.color.v5};
        Random random = new Random();

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            mainImage = itemView.findViewById(R.id.main_image);
            imageName = itemView.findViewById(R.id.image_name);
            imageDescription = itemView.findViewById(R.id.description);
            sets = itemView.findViewById(R.id.sets);
        }

        public void setImage(ItemEntity item) {

            Bitmap bitmap = Bitmap.createBitmap(Integer.parseInt(item.width), Integer.parseInt(item.height), Bitmap.Config.ARGB_8888); // create placeholder with exact width and height
            bitmap.eraseColor(Color.parseColor(item.color)); // fulfill bitmap with average color
            Glide.
                    with(context).
                    load(item.getPath()).
                    placeholder(new BitmapDrawable(bitmap)).
                    override(700).
                    into(mainImage);

            if (item.getName().equals("43083945")) {
                if (!item.tags.split(" ")[0].trim().isEmpty()){
                    imageName.setText("\uD83E\uDD16: " + item.tags.split(" ")[0]);
                } else {
                    imageName.setText(R.string.nothing_found);
                }
                imageName.setTypeface(Typeface.MONOSPACE);
            } else {
                imageName.setTypeface(Typeface.DEFAULT);
                imageName.setText(item.getName());
            }
            imageDescription.setText(item.getDescription());
        }
    }
}
