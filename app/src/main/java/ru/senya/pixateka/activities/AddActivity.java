package ru.senya.pixateka.activities;

import static ru.senya.pixateka.retrofit.Utils.getRealPath;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.net.ConnectException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.databinding.ActivityAddBinding;
import ru.senya.pixateka.models.ImageEntity;

public class AddActivity extends AppCompatActivity {

    private ActivityAddBinding binding;
    private String path;
    private ActivityResultLauncher<String> getContent;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        getContent = setGetContent();

        sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        getImage();
        adjustClickListeners();

        setContentView(binding.getRoot());
    }

    private void uploadImage() {
        String token = sharedPreferences.getString("jwt_key", "");
        File image = new File(path);

        binding.progressCircular.setVisibility(View.VISIBLE);

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), image);
        RequestBody textData = RequestBody.create(okhttp3.MediaType.parse("text/plain"), binding.inputName.getInputText());
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", image.getName(), requestBody);
        App.getItemService().upload("Bearer " + token, imagePart, textData).enqueue(new Callback<ImageEntity>() {
            @Override
            public void onResponse(@NonNull Call<ImageEntity> call, @NonNull Response<ImageEntity> response) {
                if (response.isSuccessful()){
                    onBackPressed();
                } else if (response.code() == 418){
                    binding.progressCircular.setVisibility(View.INVISIBLE);
                    Snackbar.make(binding.getRoot(), getString(R.string.error_service_unavailable), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ImageEntity> call, @NonNull Throwable t) {
                binding.progressCircular.setVisibility(View.INVISIBLE);
                try {
                    throw t;
                } catch (ConnectException e){
                    Snackbar.make(binding.getRoot(), getString(R.string.error_server_unavailable), Snackbar.LENGTH_LONG).show();
                }
                catch (Throwable e) {
                    Snackbar.make(binding.getRoot(), getString(R.string.error_idk), Snackbar.LENGTH_LONG).show();
                }

            }
        });
    }

    private void adjustClickListeners() {
        binding.selectedPhoto.setOnClickListener(view -> getImage());

        binding.submitButton.setOnClickListener(view -> {
            if (checkFields())
                uploadImage();
        });
    }

    private boolean checkFields() {
        if (binding.inputName.getInputText().trim().isEmpty()) {
            binding.inputName.setError("Введите название!");
            return false;
        }
        if (path == null) {
            Snackbar.make(binding.getRoot(), "Вы не выбрали фото!", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void getImage() {
        getContent.launch("image/*");
    }

    private ActivityResultLauncher<String> setGetContent() {
        return
                registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
                    if (result != null) {
                        Glide.
                                with(this).
                                load(result).
                                into(binding.selectedPhoto);

                        path = getRealPath(getApplicationContext(), result);
                    } else {
                        Snackbar.make(binding.getRoot(), "Вы не выбрали фото!", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }
}
