package ru.senya.pixateka.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static ru.senya.pixateka.database.retrofit.Utils.getRealPath;

import android.content.Intent;
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

import com.bumptech.glide.Glide;

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
import ru.senya.pixateka.R;
import ru.senya.pixateka.database.retrofit.userApi.User;
import ru.senya.pixateka.databinding.FragmentEditProfileBinding;

public class FragmentEditProfile extends Fragment {

    FragmentEditProfileBinding binding;
    public boolean clicked1 = false, clicked2 = false;
    Uri uri, uriB;
    User mainUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(LayoutInflater.from(getContext()), container, false);
        mainUser = App.getMainUser();
        binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        binding.toolbar.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        binding.about.binding.input.setText(mainUser.about);
        init();
        return binding.getRoot();
    }

    private void init() {
        if (mainUser.avatar != null) {
            Glide.with(getContext()).load(App.getMainUser().avatar).into(binding.pfpImg);
        }
        if (mainUser.background != null) {
            Glide.with(getContext()).load(App.getMainUser().background).centerCrop().into(binding.back);
        }
        binding.fab.setOnClickListener(v -> {
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).addFlags(FLAG_GRANT_READ_URI_PERMISSION), 3);
        });
        binding.backButton.setOnClickListener(v -> {
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).addFlags(FLAG_GRANT_READ_URI_PERMISSION), 4);
        });
        binding.buttonSave.setOnClickListener(v -> {
            if (clicked1) {
                File file = new File(getRealPath(getContext(), uri));
                String cookie = "csrftoken=" + App.getMainUser().token + "; " + "sessionid=" + App.getMainUser().sessionId;
                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("avatar", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                App.getUserService().
                        editUserPics(
                                App.getMainUser().id,
                                App.getMainUser().token,
                                cookie,
                                imagePart
                        ).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()){
                                    getActivity().onBackPressed();
                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(getContext(), "not cool", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            if (clicked2) {
                File file = new File(getRealPath(getContext(), uriB));
                String cookie = "csrftoken=" + App.getMainUser().token + "; " + "sessionid=" + App.getMainUser().sessionId;
                MultipartBody.Part back = MultipartBody.Part.createFormData("background", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                App.getUserService().
                        editUserPics(
                                App.getMainUser().id,
                                App.getMainUser().token,
                                cookie,
                                back
                        ).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()){
                                    getActivity().onBackPressed();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(getContext(), "not cool", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                if (!binding.about.getInputText().trim().isEmpty()) {
                    String cookie = "csrftoken=" + App.getMainUser().token + "; " + "sessionid=" + App.getMainUser().sessionId;
                    App.getUserService().editUserDesc(App.getMainUser().id, App.getMainUser().token, cookie, binding.about.getInputText()).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            getActivity().onBackPressed();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            binding.pfpImg.setImageURI(uri);
            clicked1 = true;
        } else if (requestCode == 4 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriB = data.getData();
            binding.back.setImageURI(uriB);
            clicked2 = true;
        }
    }
}

