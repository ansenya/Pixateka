package ru.senya.pixateka.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.res.Resources;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ru.senya.pixateka.R;
import ru.senya.pixateka.activities.MainActivity;
import ru.senya.pixateka.databinding.FragmentAddBinding;
import ru.senya.pixateka.subjects.Item;

public class FragmentAdd extends Fragment {
    FragmentAddBinding binding;
    int id = 148;

    List<Item> itemsMain;
    List<Item> itemsProfile;

    public FragmentAdd(List<Item> itemsMain, List<Item> itemsProfile) {
        this.itemsMain = itemsMain;
        this.itemsProfile = itemsProfile;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(LayoutInflater.from(getContext()), container, false);
        binding.button.setOnClickListener(view -> {
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 3);
            binding.button2.setVisibility(View.VISIBLE);
            binding.button.setVisibility(View.GONE);
        });
        binding.button2.setOnClickListener(view -> {
            Toast.makeText(getContext(), "фото загружено", Toast.LENGTH_SHORT).show();
            binding.selectedPhoto.setImageURI(null);
        });
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                Drawable drawable = Drawable.createFromStream(inputStream, uri.toString());
                itemsProfile.add(0,new Item(uri, binding.name.getInputText().toString()) );
                itemsMain.add(0, new Item(uri, binding.name.getInputText().toString()));
                Log.e("MyTag",drawable.hashCode()+"");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            binding.selectedPhoto.setImageURI(uri);
        }
    }
}
