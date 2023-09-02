//package ru.senya.pixateka.adapters;
//
//import static android.view.View.GONE;
//import static android.view.View.VISIBLE;
//
//import android.annotation.SuppressLint;
//import android.content.ClipData;
//import android.content.ClipboardManager;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.net.ConnectivityManager;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.AnimationUtils;
//import android.widget.ImageView;
//import android.widget.PopupMenu;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.Toolbar;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.FragmentActivity;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.RequestBuilder;
//import com.bumptech.glide.load.DataSource;
//import com.bumptech.glide.load.engine.GlideException;
//import com.bumptech.glide.request.RequestListener;
//import com.bumptech.glide.request.target.Target;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.makeramen.roundedimageview.RoundedImageView;
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.List;
//import java.util.Random;
//
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import ru.senya.pixateka.App;
//import ru.senya.pixateka.R;
//import ru.senya.pixateka.retrofit.Utils;
//import ru.senya.pixateka.database.room.ItemEntity;
//import ru.senya.pixateka.fragments.FragmentMain;
//
//
//public class RecyclerAdapterMain extends RecyclerView.Adapter<RecyclerAdapterMain.MyViewHolder> {
//
//    private List<ItemEntity> data;
//    private final Context context;
//
//    ru.senya.pixateka.view.viewFullscreen viewFullscreen;
//    RecyclerView recyclerView;
//    Toolbar mainToolbar;
//    androidx.appcompat.widget.Toolbar toolbar;
//    FloatingActionButton floatingActionButton;
//    FragmentActivity activity;
//    SwipeRefreshLayout swipeRefreshLayout;
//    FragmentMain fragmentMain;
//    int pos;
//
//    public RecyclerAdapterMain(FragmentActivity activity,
//                               List<ItemEntity> items,
//                               Context context,
//                               View.OnClickListener onClickListener,
//                               ru.senya.pixateka.view.viewFullscreen viewFullscreen,
//                               RecyclerView recyclerView,
//                               Toolbar toolbar,
//                               FloatingActionButton floatingActionButton,
//                               androidx.appcompat.widget.Toolbar toolbar2,
//                               SwipeRefreshLayout swipeRefreshLayout,
//                               FragmentMain fragmentMain) {
//        this.swipeRefreshLayout = swipeRefreshLayout;
//        this.activity = activity;
//        data = items;
//        this.context = context;
//        this.viewFullscreen = viewFullscreen;
//        this.recyclerView = recyclerView;
//        this.mainToolbar = toolbar;
//        this.floatingActionButton = floatingActionButton;
//        this.toolbar = toolbar2;
//        this.fragmentMain = fragmentMain;
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        pos = position;
//        holder.setImageView(data.get(position), context, activity, this, position);
//        holder.setTextView(data.get(position));
//        holder.sets.setOnClickListener(v -> {
//            PopupMenu popupMenu = new PopupMenu(context, v);
//            if (Integer.parseInt(data.get(position).uid) == App.getMainUser().id) {
//                popupMenu.inflate(R.menu.p_menu);
//            } else {
//                popupMenu.inflate(R.menu.menu);
//            }
//
////            popupMenu.setOnMenuItemClickListener(item -> {
////                switch (item.getItemId()) {
////                    case R.id.download:
////                        new Thread(() -> {
////                            try {
////                                MediaStore.Images.Media.insertImage(context.getContentResolver(),
////                                        BitmapFactory.decodeStream(new URL(data.get(position).getPath()).openConnection().getInputStream()),
////                                        data.get(position).getName(), data.get(position).getDescription() + java.time.LocalDateTime.now());
////
////                                activity.runOnUiThread(() -> {
////                                    Toast.makeText(context, "готово", Toast.LENGTH_SHORT).show();
////                                });
////
////                            } catch (Exception e) {
////                                Toast.makeText(context, "произошла ошибка", Toast.LENGTH_SHORT).show();
////                            }
////
////                        }).start();
////
////                        return true;
////                    case R.id.share:
////                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
////                        ClipData clip = ClipData.newPlainText("url", data.get(position).getPath());
////                        clipboard.setPrimaryClip(clip);
////                        Toast.makeText(context, "скопировано", Toast.LENGTH_SHORT).show();
////                        return true;
////                    case R.id.delete:
////                        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
////                        boolean connected = connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected() && connectivityManager.getActiveNetworkInfo().isAvailable();
////                        if (connected)
////                            App.getItemService().deleteItem(data.get(position).id, Utils.TOKEN, "csrftoken=" + Utils.TOKEN + "; " + "sessionid=" + Utils.SESSION_ID).enqueue(new Callback<ResponseBody>() {
////                                @Override
////                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
////                                    if (response.isSuccessful()) {
////                                        new Thread(() -> {
////                                            App.getDatabase().itemDAO().deleteByUserId(data.get(position).id);
////                                            activity.runOnUiThread(() -> {
////                                                data.remove(position);
////                                                fragmentMain.onRefreshListener.onRefresh();
////                                                RecyclerAdapterMain.super.notifyDataSetChanged();
////                                            });
////                                        }).start();
////                                        notifyDataSetChanged();
////                                    } else {
////                                        try {
////                                            Log.e("MyTag", response.errorBody().string());
////                                        } catch (IOException e) {
////                                            throw new RuntimeException(e);
////                                        }
////                                    }
////                                }
////
////                                @Override
////                                public void onFailure(Call<ResponseBody> call, Throwable t) {
////
////                                }
////                            });
////                        else
////                            Toast.makeText(context, "Нет доступа в интернет", Toast.LENGTH_SHORT).show();
////
////                        return true;
////
////
////                }
////                return false;
////            });
//            popupMenu.show();
//        });
//
//        holder.mainImage.setOnClickListener(v -> {
//            viewFullscreen.setVisibility(VISIBLE);
//            recyclerView.setVisibility(GONE);
//            mainToolbar.setVisibility(View.INVISIBLE);
//            toolbar.setVisibility(VISIBLE);
//            floatingActionButton.setVisibility(GONE);
//            swipeRefreshLayout.setVisibility(GONE);
//            viewFullscreen.update(data.get(position), activity);
//        });
//        holder.mainImage.setOnLongClickListener(v -> {
//            holder.sets.callOnClick();
//            return true;
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return data.size();
//    }
//
//
//    public static class MyViewHolder extends RecyclerView.ViewHolder {
//
//        int[] colors = {R.color.v1, R.color.v2, R.color.v3, R.color.v4, R.color.v5};
//        Random random = new Random();
//        RoundedImageView mainImage;
//        TextView imageName, imageDescription;
//        ImageView sets;
//
//
//        MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//            mainImage = itemView.findViewById(R.id.main_image);
//            imageName = itemView.findViewById(R.id.image_name);
//            imageDescription = itemView.findViewById(R.id.description);
//            sets = itemView.findViewById(R.id.sets);
//        }
//
//        @SuppressLint("CheckResult")
//        void setImageView(ItemEntity item, Context context, FragmentActivity activity, RecyclerAdapterMain adapter, int p) {
//
//            Bitmap bitmap = Bitmap.createBitmap(Integer.parseInt(item.width), Integer.parseInt(item.height), Bitmap.Config.ARGB_8888);
//            bitmap.eraseColor(Color.parseColor(item.color));
//            //      mainImage.setImageBitmap(bitmap);
//            mainImage.startAnimation(AnimationUtils.loadAnimation(context, R.anim.wave_animation));
//            RequestBuilder<Drawable> requestBuilder = Glide.
//                    with(context).
//                    load(item.getPath()).
//                    placeholder(new BitmapDrawable(activity.getResources(), bitmap)).
//                    override(480);
//            requestBuilder.addListener(new RequestListener<Drawable>() {
//                @Override
//                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                    mainImage.clearAnimation();
//                    return false;
//                }
//
//                @Override
//                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                    mainImage.clearAnimation();
//                    return false;
//                }
//            });
//            requestBuilder.into(mainImage);
//
//        }
//
//        void setTextView(ItemEntity item) {
//            if (item.getName().equals("43083945")) {
//                if (!item.tags.split(" ")[0].trim().isEmpty()) {
//                    imageName.setText("\uD83E\uDD16: " + item.tags.split(" ")[0]);
//                } else {
//                    imageName.setText(R.string.nothing_found);
//                }
//                imageName.setTypeface(Typeface.MONOSPACE);
//            } else {
//                imageName.setTypeface(Typeface.DEFAULT);
//                imageName.setText(item.getName());
//            }
//        }
//    }
//}