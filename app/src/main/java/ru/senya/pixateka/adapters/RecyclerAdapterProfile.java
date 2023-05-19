package ru.senya.pixateka.adapters;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.activities.Visible;
import ru.senya.pixateka.database.retrofit.Utils;
import ru.senya.pixateka.database.room.ItemEntity;
import ru.senya.pixateka.databinding.NewFragmentProfileBinding;
import ru.senya.pixateka.view.viewFullscreen;


public class RecyclerAdapterProfile extends RecyclerView.Adapter<RecyclerAdapterProfile.ViewHolder> {

    private final List<ItemEntity> data;
    private final Context context;
    Toolbar toolbar;
    FragmentActivity activity;
    ru.senya.pixateka.view.viewFullscreen fragment;
    NestedScrollView nestedScrollView;
    NewFragmentProfileBinding binding;
    Visible visible;
    SwipeRefreshLayout.OnRefreshListener onRefreshListener;

    public RecyclerAdapterProfile(List<ItemEntity> data, Context context, viewFullscreen fragment, Toolbar toolbar, FragmentActivity activity, NestedScrollView nestedScrollView, NewFragmentProfileBinding binding, Visible visible, SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        this.data = data;
        this.context = context;
        this.toolbar = toolbar;
        this.activity = activity;
        this.fragment = fragment;
        this.nestedScrollView = nestedScrollView;
        this.binding = binding;
        this.visible = visible;
        this.onRefreshListener = onRefreshListener;
    }

    @NonNull
    @Override
    public RecyclerAdapterProfile.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setImage(data.get(position));
        holder.sets.setOnClickListener(v -> {
            PopupMenu menu = new PopupMenu(context, v);
            menu.inflate(R.menu.p_menu);
            menu.setOnMenuItemClickListener(item -> {
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
                                Toast.makeText(context, "smth wrong", Toast.LENGTH_SHORT).show();
                            }

                        }).start();
                        return true;
                    case R.id.share:
                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("url", data.get(position).getPath());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(context, "copied to clipboard", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.delete:
                        App.getItemService().deleteItem(data.get(position).id, Utils.TOKEN, "csrftoken=" + Utils.TOKEN + "; " + "sessionid=" + Utils.SESSION_ID).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    new Thread(() -> {
                                        App.getDatabase().itemDAO().deleteByUserId(data.get(position).id);
                                        activity.runOnUiThread(() -> {
                                            data.remove(position);
                                            onRefreshListener.onRefresh();
                                            binding.recyclerList.getAdapter().notifyDataSetChanged();
                                        });
                                    }).start();
                                    notifyDataSetChanged();
                                } else {
                                    try {
                                        Log.e("MyTag", response.errorBody().string());
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });

                }
                return false;
            });
            menu.show();
        });
        holder.mainImage.setOnClickListener(v -> {
            toolbar.setVisibility(View.VISIBLE);
            fragment.update(data.get(position), activity);
            visible.setVisible(true);
            fragment.setVisibility(View.VISIBLE);
            nestedScrollView.setVisibility(View.GONE);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView mainImage;
        TextView imageName, imageDescription;
        ImageView sets;
        int[] colors = {R.color.v1, R.color.v2, R.color.v3, R.color.v4, R.color.v5};
        Random random = new Random();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mainImage = itemView.findViewById(R.id.main_image);
            imageName = itemView.findViewById(R.id.image_name);
            imageDescription = itemView.findViewById(R.id.description);
            sets = itemView.findViewById(R.id.sets);
        }

        public void setImage(ItemEntity item) {
            Bitmap bitmap = Bitmap.createBitmap(Integer.parseInt(item.width), Integer.parseInt(item.height), Bitmap.Config.ARGB_8888); // create placeholder with exact width and height
            bitmap.eraseColor(Color.parseColor(item.color)); // fulfill bitmap with average color
            try {
                Glide.
                        with(context).
                        load(item.getPath()).
                        placeholder(new BitmapDrawable(bitmap)).
                        override(700).
                        into(mainImage);
            } catch (NullPointerException e) {
                Log.e("MyTag", item.id + "");
                Glide.
                        with(context).
                        load(item.getPath()).
                        placeholder(colors[random.nextInt(colors.length)]).
                        into(mainImage);
            }
            if (item.getName().equals("43083945")) {
                if (!item.tags.split(" ")[0].trim().isEmpty()){
                    imageName.setText("ИИ: " + item.tags.split(" ")[0]);
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
