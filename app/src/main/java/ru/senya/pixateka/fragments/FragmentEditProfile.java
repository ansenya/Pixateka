package ru.senya.pixateka.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static ru.senya.pixateka.activities.AddActivity.getRealPath;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.database.retrofit.userApi.User;
import ru.senya.pixateka.databinding.FragmentEditProfileBinding;

public class FragmentEditProfile extends Fragment {

    FragmentEditProfileBinding binding;
    public boolean clicked = false;
    Uri uri;
    User mainUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(LayoutInflater.from(getContext()), container, false);
        init();
        mainUser = App.getMainUser();
        try {
            Bitmap bitmap = Bitmap.createBitmap(1400, 500, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(getResources().getColor(R.color.cool_color, getActivity().getTheme()));
            Glide.
                    with(getContext()).
                    load(mainUser.background).
                    placeholder(new BitmapDrawable(bitmap)).
                    override(1000).
                    into(binding.back);
        } catch (NullPointerException e){

        }

        try {
            Bitmap bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(getResources().getColor(R.color.cool_color, getActivity().getTheme()));
            Glide.
                    with(getContext()).
                    load("http://192.168.1.60:8000/media/avatars/img.png").
                    into(binding.pfpImg);
        } catch (NullPointerException e){

        }

        return binding.getRoot();
    }

    private void init() {
        binding.fab.setOnClickListener(v -> {
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).addFlags(FLAG_GRANT_READ_URI_PERMISSION), 3);
        });
        binding.buttonSave.setOnClickListener(v -> {
            if (clicked) {
                File file = new File(getRealPath(getContext(), uri));
                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("avatar" + App.getMainUser().id, file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                if (!binding.about.getInputText().isEmpty()) mainUser.setAbout(binding.about.getInputText());
                if (!binding.emailAdress.getInputText().isEmpty()) mainUser.setEmail(binding.emailAdress.getInputText());
                    App.getUserService().editUser(App.getMainUser().id, App.getMainUser().first_name, App.getMainUser().last_name, App.getMainUser().country, App.getMainUser().about, imagePart);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            binding.pfpImg.setImageURI(data.getData());
            uri = data.getData();
            clicked = true;
        }
    }
}

