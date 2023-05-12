package ru.senya.pixateka.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.BitmapFactory;
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

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.database.retrofit.Utils;
import ru.senya.pixateka.database.room.ItemEntity;


public class RecyclerAdapterProfile extends RecyclerView.Adapter<RecyclerAdapterProfile.ViewHolder> {

    private final List<ItemEntity> data;
    private final Context context;
    ru.senya.pixateka.view.viewFullscreen viewFullscreen;
    Toolbar toolbar;
    FragmentActivity activity;
    ru.senya.pixateka.view.viewFullscreen fragment;
    NestedScrollView nestedScrollView;

    public RecyclerAdapterProfile(List<ItemEntity> data, Context context, ru.senya.pixateka.view.viewFullscreen viewFullscreen, Toolbar toolbar, FragmentActivity activity, ru.senya.pixateka.view.viewFullscreen fragment, NestedScrollView nestedScrollView) {
        this.data = data;
        this.context = context;
        this.viewFullscreen = viewFullscreen;
        this.toolbar = toolbar;
        this.activity = activity;
        this.fragment = fragment;
        this.nestedScrollView = nestedScrollView;
    }

    @NonNull
    @Override
    public RecyclerAdapterProfile.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
                        App.getItemService().deleteItem(data.get(position).id, Utils.TOKEN, "csrftoken=" + Utils.TOKEN + "; " +"sessionid="+Utils.SESSION_ID).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()){
                                    new Thread(()->{
                                        App.getDatabase().itemDAO().deleteByUserId(data.get(position).id);
                                        activity.runOnUiThread(()->{
                                            data.remove(position);
                                        });
                                    }).start();

                                    notifyDataSetChanged();
                                }
                                else {
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
        holder.mainImage.setOnClickListener(v ->{
            fragment.update(data.get(position), activity);
            fragment.setVisibility(View.VISIBLE);
            nestedScrollView.setVisibility(View.GONE);
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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mainImage = itemView.findViewById(R.id.main_image);
            imageName = itemView.findViewById(R.id.image_name);
            imageDescription = itemView.findViewById(R.id.description);
            sets = itemView.findViewById(R.id.sets);
        }

        public void setImage(ItemEntity item) {
            Glide.
                    with(context).
                    load(item.getPath()).
                    into(mainImage);
            imageName.setText(item.getName());
            imageDescription.setText(item.getDescription());
        }
    }
}
