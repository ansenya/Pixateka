package ru.senya.pixateka.adapters;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.net.URL;
import java.util.LinkedList;
import java.util.Random;

import ru.senya.pixateka.R;
import ru.senya.pixateka.database.room.ItemEntity;
import ru.senya.pixateka.databinding.ViewItemBinding;
import ru.senya.pixateka.view.viewFullscreen;

public class RecyclerAdapterSecondary extends RecyclerView.Adapter<RecyclerAdapterSecondary.MyViewHolder> {

    LinkedList<ItemEntity> data;
    private final Context context;
    FragmentActivity activity;
    Runnable runnable;
    viewFullscreen viewFullscreen;

    public RecyclerAdapterSecondary(LinkedList<ItemEntity> data, Context context, FragmentActivity activity, viewFullscreen viewFullscreen) {
        this.data = data;
        this.context = context;
        this.activity = activity;
        this.viewFullscreen = viewFullscreen;
    }

    @NonNull
    @Override
    public RecyclerAdapterSecondary.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerAdapterSecondary.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterSecondary.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setItem(context, data.get(position));
        holder.sets.setOnClickListener(v -> {
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
                                            BitmapFactory.decodeStream(new URL(data.get(position).getPath()).openConnection().getInputStream()),
                                            data.get(position).getName(), data.get(position).getDescription() + java.time.LocalDateTime.now());

                                    activity.runOnUiThread(() -> {
                                        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                                    });

                                } catch (Exception e) {
                                    activity.runOnUiThread(() -> {
                                        Toast.makeText(context, "smth wrong", Toast.LENGTH_SHORT).show();
                                    });
                                }

                            }).start();

                            return true;
                        case R.id.share:
                            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("url", data.get(position).getPath());
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(context, "copied to clipboard", Toast.LENGTH_SHORT).show();
                            return true;
                    }
                    return false;
                }
            });

            popupMenu.show();
        });
        holder.mainImage.setOnClickListener(v -> {
            viewFullscreen.update(data.get(position), activity);
            viewFullscreen.goUp();
//            viewFullscreen.addUpdate(data.get(position), activity);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        int[] colors = {R.color.v1, R.color.v2, R.color.v3, R.color.v4, R.color.v5};
        Random random = new Random();
        ViewItemBinding binding;
        Context context;
        RoundedImageView mainImage;
        TextView imageName;
        ImageView sets;

        public MyViewHolder(@NonNull View itemView, ViewGroup parent) {
            super(itemView);
            mainImage = itemView.findViewById(R.id.main_image);
            imageName = itemView.findViewById(R.id.image_name);
            sets = itemView.findViewById(R.id.sets);
            context = parent.getContext();
        }

        public void setItem(Context context, ItemEntity item) {
            Glide.
                    with(context).
                    load(item.getPath()).dontAnimate().
                    placeholder(colors[random.nextInt(colors.length)]).
                    into(mainImage);
            if (item.getName().equals("43083945")) {
                if (!item.tags.split(" ")[0].trim().isEmpty()){
                    imageName.setText("ИИ: " + item.tags.split(" ")[0]);
                } else {
                    imageName.setText("Ничего нет");
                }
                imageName.setTypeface(Typeface.MONOSPACE);
            } else {
                imageName.setTypeface(Typeface.DEFAULT);
                imageName.setText(item.getName());
            }
        }
    }
}
