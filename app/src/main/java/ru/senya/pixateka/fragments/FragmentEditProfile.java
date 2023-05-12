package ru.senya.pixateka.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;

import static ru.senya.pixateka.activities.AddActivity.getRealPath;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.senya.pixateka.App;
import ru.senya.pixateka.database.retrofit.userApi.User;
import ru.senya.pixateka.databinding.FragmentEditProfileBinding;

public class FragmentEditProfile extends Fragment {

    FragmentEditProfileBinding binding;
    public boolean clicked = false;
    Uri uri;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(LayoutInflater.from(getContext()), container, false);
        init();
        return binding.getRoot();
    }

    private void init(){
        binding.buttonEdit.setOnClickListener(v -> {
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).addFlags(FLAG_GRANT_READ_URI_PERMISSION), 3);
        });
        binding.buttonSave.setOnClickListener(v ->{if (clicked){
            File file = new File(getRealPath(getContext(), uri));
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("avatar"+App.getMainUser().id, file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
            App.getUserService().editUserAvatar(App.getMainUser().id, "фывфывфывфывфыв", imagePart, App.getMainUser().token, "csrftoken="+App.getMainUser().token+"; "+"sessionid="+App.getMainUser().sessionId).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Log.e("MyTag", response.body().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }});


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==3 && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            binding.pfpImg.setImageURI(data.getData());
            uri = data.getData();
            clicked = true;
        }
    }
}

