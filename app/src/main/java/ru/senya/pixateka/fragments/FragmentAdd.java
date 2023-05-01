package ru.senya.pixateka.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.senya.pixateka.App;
import ru.senya.pixateka.databinding.FragmentAddBinding;
import ru.senya.pixateka.room.ItemEntity;

public class FragmentAdd extends Fragment {

    Uri uri;
    FragmentAddBinding binding;
    String path;
    List<ItemEntity> itemsMain;
    ItemEntity item;
    int PICK_IMAGE_REQUEST = 1;
    public FragmentAdd(List<ItemEntity> itemsMain) {
        this.itemsMain = itemsMain;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(LayoutInflater.from(getContext()), container, false);
        initListeners();
        return binding.getRoot();
    }

    private void initListeners() {
        binding.buttonSelectPhoto.setOnClickListener(view->{
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 3);
            binding.buttonSelectPhoto.setVisibility(View.GONE);
            binding.button2.setVisibility(View.VISIBLE);
        });
//        binding.button2.setOnClickListener(view->{
//            new Thread(()->{
//                item = new ItemEntity(path, binding.name.getInputText(),
//                        binding.category.getInputText(),
//                        binding.tags.getInputText());
//
//                App.getDatabase().itemDAO().save(item);
//                itemsMain.add(item);
//            }).start();
//
//            Toast.makeText(getContext(), "Photo Uploaded", Toast.LENGTH_SHORT).show();
//            reload();
//        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null){
            uri = data.getData();
            path = getRealPath(getContext(), uri);
            Picasso.with(getContext()).load(uri).into(binding.selectedPhoto);
        }
    }



    private static String getRealPath(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;
        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    public void reload(){
        binding.button2.setVisibility(View.GONE);
        binding.buttonSelectPhoto.setVisibility(View.VISIBLE);
        binding.selectedPhoto.setImageBitmap(null);
    }
}
