package ru.senya.pixateka.activities;

import static ru.senya.pixateka.activities.AddActivity.getRealPath;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonElement;

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
import ru.senya.pixateka.databinding.ActivityRegistrationBinding;

public class RegistrationActivity extends AppCompatActivity {

    ActivityRegistrationBinding binding;
    private FirebaseAuth firebaseAuth;
    MultipartBody.Part avatar = null;
    File file;
    Boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        init();
    }


    private void init() {
        binding.buttonEnter2.setOnClickListener(view -> {
            if (clicked) {
                clicked = true;
                String errorString = "";
                if (binding.inputLogin.getInputText().isEmpty()) {
                    errorString += "Логин не может быть пустой\n";
                }
                if (binding.inputLogin.getInputText().isEmpty() || binding.inputRepeatPassword.getInputText().isEmpty()) {
                    errorString += "Пароль не может быть пустой\n";
                } else {
                    if (!binding.inputRepeatPassword.getInputText().equals(binding.inputPassword.getInputText())) {
                        errorString += "Пароли не совпадают\n";
                    }
                }
                if (!binding.checkbox.isChecked()) {
                    errorString += "Согласитесь с пользовательским соглашением";
                }
                if (errorString.equals("")) {

                    RequestBody username = RequestBody.create(MediaType.parse("text/plain"), binding.inputLogin.getInputText());
                    RequestBody password = RequestBody.create(MediaType.parse("text/plain"), binding.inputPassword.getInputText());
                    RequestBody email = RequestBody.create(MediaType.parse("text/plain"), binding.inputEmail.getInputText());
                    RequestBody first_name = RequestBody.create(MediaType.parse("text/plain"), binding.inputFirstName.getInputText());
                    RequestBody last_name = RequestBody.create(MediaType.parse("text/plain"), "last_name");
                    RequestBody country = RequestBody.create(MediaType.parse("text/plain"), binding.inputCountry.getInputText());


                    App.getUserService().register(username, avatar, password, email, first_name, last_name, country).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                App.setMainUser(response.body());
                                Log.e("MyTag", response.headers().get("csrftoken"));
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                try {
                                    Log.e("MyTag", response.errorBody().string());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e("MyTag", "error", t);
                            Toast.makeText(RegistrationActivity.this, "smth wrong happened", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    binding.wrong.setText(errorString);
                    binding.wrong.setVisibility(View.VISIBLE);
                    new Thread(() -> {
                        try {
                            Thread.sleep(4500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        runOnUiThread(() -> {
                            binding.wrong.setVisibility(View.INVISIBLE);
                        });
                    }).start();
                }
                clicked = false;
            }


        });binding.buttonBack.setOnClickListener(v ->{
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
            onBackPressed();
        });
        binding.choosePhotoImg.setOnClickListener(v -> {
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 3);
        });
        binding.choosePhotoBtn.setOnClickListener(v -> {
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 3);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            binding.choosePhotoImg.setImageURI(data.getData());
            binding.choosePhotoImg.setCornerRadius(300);
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            file = new File(getRealPath(getApplicationContext(), data.getData()));
            avatar = MultipartBody.Part.createFormData("avatar", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        }
    }
}
