//package ru.senya.pixateka.adapters;
//
//import android.graphics.BitmapFactory;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.makeramen.roundedimageview.RoundedImageView;
//
//import java.util.List;
//
//import ru.senya.pixateka.R;
//
//
//public class RecyclerViewAdapterProfile extends RecyclerView.Adapter<RecyclerViewAdapterProfile.ViewHolder> {
//
//
//
//    public RecyclerViewAdapterProfile(List<UserItemEntity> items) {
//        this.items = items;
//    }
//
//    @NonNull
//    @Override
//    public RecyclerViewAdapterProfile.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerViewAdapterProfile.ViewHolder holder, int position) {
//        holder.setImageView(items.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//        RoundedImageView imageView;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imageView = itemView.findViewById(R.id.pic);
//        }
//        public void setImageView(UserItemEntity user){
//            imageView.setImageBitmap(BitmapFactory.decodeFile(user.path));
//        }
//    }
//}
