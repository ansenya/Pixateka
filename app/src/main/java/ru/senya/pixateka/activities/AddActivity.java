package ru.senya.pixateka.activities;

import static ru.senya.pixateka.database.retrofit.Utils.getRealPath;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.senya.pixateka.App;
import ru.senya.pixateka.databinding.ActivityAddBinding;

public class AddActivity extends AppCompatActivity {

    private ActivityAddBinding binding;
    private String path;
    private ActivityResultLauncher<String> getContent;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        getContent = setGetContent();

        getImage();
        adjustClickListeners();

        setContentView(binding.getRoot());
    }

    private void uploadImage() {
        File image = new File(path);
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
