package ru.senya.pixateka.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.activities.MainActivity;
import ru.senya.pixateka.databinding.FragmentAddBinding;
import ru.senya.pixateka.room.ItemEntity;
import ru.senya.pixateka.subjects.Item;

public class FragmentAdd extends Fragment {
    FragmentAddBinding binding;
    int id = 148;
    String path;
    Bitmap bitmap;

    List<ItemEntity> itemsMain = new ArrayList<ItemEntity>();
    List<ItemEntity> itemsProfile = new ArrayList<>();

    public FragmentAdd(List<ItemEntity> itemsMain) {
       this.itemsMain = itemsMain;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(LayoutInflater.from(getContext()), container, false);
        binding.button.setOnClickListener(view -> {
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).addFlags(FLAG_GRANT_READ_URI_PERMISSION), 3);
            binding.button2.setVisibility(View.VISIBLE);
            binding.button.setVisibility(View.GONE);
        });
        binding.button2.setOnClickListener(view ->
        {
            Toast.makeText(getContext(), "фото загружено", Toast.LENGTH_SHORT).show();
            new Thread(() -> {
                App.getDatabase().itemDAO().save(new ItemEntity(path, ""));
            }).start();
            binding.selectedPhoto.setImageBitmap(BitmapFactory.decodeFile(path));
            itemsMain.add(new ItemEntity(path, ""));
        });
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            path = getRealPathFromURI_API11to18(getContext(), data.getData());

//            try {
//                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
//            } catch (FileNotFoundException e) {
//                throw new RuntimeException(e);
//            }

        }
    }

    public void reload(){
        binding.button2.setVisibility(View.GONE);
        binding.button.setVisibility(View.VISIBLE);
    }

    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
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
}
