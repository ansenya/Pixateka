package ru.senya.pixateka.activities;

import static ru.senya.pixateka.database.retrofit.Utils.getRealPath;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.senya.pixateka.App;
import ru.senya.pixateka.database.retrofit.Utils;
import ru.senya.pixateka.database.retrofit.userApi.User;
import ru.senya.pixateka.databinding.ActivityRegistrationBinding;

public class RegistrationActivity extends AppCompatActivity {

    ActivityRegistrationBinding binding;
    private FirebaseAuth firebaseAuth;
    MultipartBody.Part avatar = null;
    File file;
    Boolean clicked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        init();
    }


    private void init() {
        binding.buttonEnter2.setOnClickListener(view -> {
            if (clicked) {
                clicked = false;
                String errorString = "";
                if (binding.inputLogin.getInputText().isEmpty()) {
                    errorString += "Логин не может быть пустой\n";
                }
                if (binding.inputPassword.getInputText().length() < 8) {
                    errorString += "Пароль должен быть больше 7 знаков\n";
                } else {
                    if (!binding.inputRepeatPassword.getInputText().equals(binding.inputPassword.getInputText())) {
                        errorString += "Пароли не совпадают\n";
                    }
                }
                if (binding.inputLogin.getInputText().isEmpty() || binding.inputRepeatPassword.getInputText().isEmpty()) {
                    errorString += "Пароль не может быть пустой\n";
                } else {
                    if (!binding.inputRepeatPassword.getInputText().equals(binding.inputPassword.getInputText())) {
                        errorString += "Пароли не совпадают\n";
                    }
                }
                if (errorString.equals("")) {

                    RequestBody username = RequestBody.create(MediaType.parse("text/plain"), binding.inputLogin.getInputText());
                    RequestBody password = RequestBody.create(MediaType.parse("text/plain"), binding.inputPassword.getInputText());
                    RequestBody email = RequestBody.create(MediaType.parse("text/plain"), "mail@mail.com");
                    RequestBody first_name = RequestBody.create(MediaType.parse("text/plain"), "first_name");
                    RequestBody last_name = RequestBody.create(MediaType.parse("text/plain"), "last_name");
                    RequestBody country = RequestBody.create(MediaType.parse("text/plain"), "ru");
//                    App.getUserService().register(username, avatar, password, email, first_name, last_name, country).enqueue(new Callback<User>() {
//                        @Override
//                        public void onResponse(Call<User> call, Response<User> response) {
//                            if (response.isSuccessful() && response.body() != null) {
//                                binding.progressCircular.setVisibility(View.VISIBLE);
//
//                                RequestBody username = RequestBody.create(MediaType.parse("text/plain"), binding.inputLogin.getInputText().trim());
//                                RequestBody password = RequestBody.create(MediaType.parse("text/plain"), binding.inputPassword.getInputText().trim());
//                                App.getUserService().login(password, username).enqueue(new Callback<User>() {
//                                    @Override
//                                    public void onResponse(Call<User> call, Response<User> response) {
//                                        if (response.isSuccessful()) {
//                                            User user = response.body();
//
//                                            String src = response.headers().toMultimap().get("set-cookie").toString();
//
//                                            String csrftoken = "csrftoken=(.*?);";
//                                            Pattern pattern = Pattern.compile(csrftoken);
//                                            Matcher matcher = pattern.matcher(src);
//
//                                            if (matcher.find()) {
//                                                Utils.setTOKEN(matcher.group(1));
//                                                user.setToken(Utils.TOKEN);
//                                                Log.e("MyTag", "token=" + Utils.TOKEN);
//                                            }
//
//                                            String sessionId = "sessionid=(.*?);";
//                                            pattern = Pattern.compile(sessionId);
//                                            matcher = pattern.matcher(src);
//
//                                            if (matcher.find()) {
//                                                Utils.setSessionId(matcher.group(1));
//                                                user.setSessionId(Utils.SESSION_ID);
//                                                Log.e("MyTag", Utils.SESSION_ID);
//                                            }
//                                            try {
//                                                new Thread(() -> {
//                                                    App.getDatabase().userDAO().save(user);
//                                                    App.setMainUser(App.getDatabase().userDAO().getUser()[0]);
//                                                    Utils.setTOKEN(App.getMainUser().token);
//                                                    Utils.setSessionId(App.getMainUser().sessionId);
//                                                    runOnUiThread(() -> {
//                                                        binding.progressCircular.setVisibility(View.GONE);
//                                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                                                        finish();
//                                                    });
//                                                }).start();
//
//                                            } catch (Exception e) {
//                                                binding.progressCircular.setVisibility(View.GONE);
//                                            }
//
//                                        } else {
//                                            binding.progressCircular.setVisibility(View.GONE);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<User> call, Throwable t) {
//                                        Log.e("MyTag", "error", t);
//                                        Snackbar.make(binding.getRoot(), "Что-то пошло не так", Snackbar.LENGTH_LONG).show();
//                                        binding.progressCircular.setVisibility(View.GONE);
//                                    }
//                                });
//
//                            } else {
//                                try {
//                                    if (response.errorBody().string().contains("password")) {
//                                        Snackbar.make(binding.getRoot(), "Пароль не должен быть слишком простым", Snackbar.LENGTH_LONG).show();
//                                    }
//                                } catch (IOException e) {
//                                    throw new RuntimeException(e);
//                                }
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<User> call, Throwable t) {
//                            Log.e("MyTag", "error", t);
//                            Snackbar.make(binding.getRoot(), "Что-то пошло не так", Snackbar.LENGTH_LONG).show();
//                        }
//                    });

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
                clicked = true;
            }


        });
        binding.buttonBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.choosePhotoImg.setOnClickListener(v -> {
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 3);
        });
        binding.choosePhotoBtn.setOnClickListener(v -> {
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 3);
        });
//        binding.inputPassword.binding.pic.setOnClickListener(v -> {
//            binding.inputPassword.setInputType(InputType.TYPE_CLASS_TEXT);
//            binding.inputPassword.binding.input.setSelection(binding.inputPassword.binding.input.getText().length());
//            new Thread(() -> {
//                try {
//                    Thread.sleep(3500);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                runOnUiThread(() -> binding.inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
//                binding.inputPassword.binding.input.setSelection(binding.inputPassword.binding.input.getText().length());
//            }).start();
//        });
//        binding.inputRepeatPassword.binding.pic.setOnClickListener(v -> {
//            binding.inputRepeatPassword.setInputType(InputType.TYPE_CLASS_TEXT);
//            binding.inputRepeatPassword.binding.input.setSelection(binding.inputRepeatPassword.binding.input.getText().length());
//            new Thread(() -> {
//                try {
//                    Thread.sleep(3500);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                runOnUiThread(() -> binding.inputRepeatPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
//                binding.inputRepeatPassword.binding.input.setSelection(binding.inputRepeatPassword.binding.input.getText().length());
//            }).start();
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            binding.choosePhotoImg.setImageURI(data.getData());
            binding.choosePhotoImg.setCornerRadius(300);
            file = new File(getRealPath(getApplicationContext(), data.getData()));
            avatar = MultipartBody.Part.createFormData("avatar", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        }
    }
}
