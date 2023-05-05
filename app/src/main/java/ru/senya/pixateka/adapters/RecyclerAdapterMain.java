package ru.senya.pixateka.adapters;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.makeramen.roundedimageview.RoundedImageView;

import java.net.URL;
import java.util.List;
import java.util.Random;

import ru.senya.pixateka.R;
import ru.senya.pixateka.database.room.ItemEntity;


public class RecyclerAdapterMain extends RecyclerView.Adapter<RecyclerAdapterMain.MyViewHolder> {

    private final List<ItemEntity> Items;
    private final Context context;

    ru.senya.pixateka.view.viewFullscreen viewFullscreen;
    RecyclerView recyclerView;
    Toolbar mainToolbar;
    androidx.appcompat.widget.Toolbar toolbar;
    FloatingActionButton floatingActionButton;
    FragmentActivity activity;
    SwipeRefreshLayout swipeRefreshLayout;

    public RecyclerAdapterMain(FragmentActivity activity,
                               List<ItemEntity> items,
                               Context context,
                               View.OnClickListener onClickListener,
                               ru.senya.pixateka.view.viewFullscreen viewFullscreen,
                               RecyclerView recyclerView,
                               Toolbar toolbar,
                               FloatingActionButton floatingActionButton,
                               androidx.appcompat.widget.Toolbar toolbar2,
                               SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.activity = activity;
        Items = items;
        this.context = context;
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.setImageView(Items.get(position), context, activity, this, position);
        holder.setTextView(Items.get(position));

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
                                            BitmapFactory.decodeStream(new URL(Items.get(position).getPath()).openConnection().getInputStream()),
                                            Items.get(position).getName(), Items.get(position).getDescription() + java.time.LocalDateTime.now());

                                    activity.runOnUiThread(() -> {
                                        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                                    });

                                } catch (Exception e) {
                                    Toast.makeText(context, "smth wrong", Toast.LENGTH_SHORT).show();
                                }

                            }).start();

                            return true;
                        case R.id.share:
                            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("url", Items.get(position).getPath());
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
            viewFullscreen.setVisibility(VISIBLE);
            recyclerView.setVisibility(GONE);
            mainToolbar.setVisibility(View.INVISIBLE);
            toolbar.setVisibility(VISIBLE);
            floatingActionButton.setVisibility(GONE);
            swipeRefreshLayout.setVisibility(GONE);
            viewFullscreen.update(Items.get(position), activity);
        });
        holder.mainImage.setOnLongClickListener(v -> {
            Log.e("MyTag", Items.get(position).getPath()+ '\n'+Items.get(position).getName());
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {

        int[] colors = {R.color.v1, R.color.v2, R.color.v3, R.color.v4, R.color.v5};
        Random random = new Random();
        RoundedImageView mainImage;
        TextView imageName, imageDescription;
        ImageView sets;


        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mainImage = itemView.findViewById(R.id.main_image);
            imageName = itemView.findViewById(R.id.image_name);
            imageDescription = itemView.findViewById(R.id.description);
            sets = itemView.findViewById(R.id.sets);
        }

        void setImageView(ItemEntity item, Context context, FragmentActivity activity, RecyclerAdapterMain adapter, int p) {
            Glide.
                    with(context).
                    load(item.getPath()).
                    placeholder(colors[random.nextInt(colors.length)]).
                    into(mainImage);
        }

        void setTextView(ItemEntity item) {
            imageName.setText(item.getName());
            imageDescription.setText("by " + item.getEmail());
        }
    }
}
