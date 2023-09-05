//package ru.senya.pixateka.activities.old;
//
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.material.snackbar.Snackbar;
//
//import java.io.IOException;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import okhttp3.MediaType;
//import okhttp3.RequestBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import ru.senya.pixateka.App;
//import ru.senya.pixateka.activities.MainActivity;
//import ru.senya.pixateka.activities.RegistrationActivity;
//import ru.senya.pixateka.utils.Utils;
//import ru.senya.pixateka.models.User;
//import ru.senya.pixateka.databinding.ActivityLoginBinding;
//
//
//public class LoginActivityOld extends AppCompatActivity {
//
//    ActivityLoginBinding binding;
//    Button buttonEnter;
//    TextView buttonRegistration;
//    int i;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        binding = ActivityLoginBinding.inflate(getLayoutInflater());
//        super.onCreate(savedInstanceState);
//        setContentView(binding.getRoot());
//        buttonEnter = binding.buttonEnter;
//        buttonRegistration = binding.buttonRegistration;
//        init();
//    }
//
//    void init() {
//        buttonEnter.setOnClickListener(view -> {
//            String error = "";
//            if (binding.inputLogin.getInputText().trim().isEmpty()) {
//                error += "Логин не может быть пустым!\n";
//            }
//            if (binding.inputPassword.getInputText().trim().isEmpty()) {
//                error += "Пароль не может быть пустым!\n";
//            }
//
//            if (error.isEmpty()) {
//                binding.progressCircular.setVisibility(View.VISIBLE);
//                RequestBody username = RequestBody.create(MediaType.parse("text/plain"), binding.inputLogin.getInputText());
//                RequestBody password = RequestBody.create(MediaType.parse("text/plain"), binding.inputPassword.getInputText());
//
//                App.getUserService().login(password, username).enqueue(new Callback<User>() {
//                    @Override
//                    public void onResponse(Call<User> call, Response<User> response) {
//                        if (response.isSuccessful()) {
//                            User user = response.body();
//                            String src = response.headers().toMultimap().get("set-cookie").toString();
//
//                            String csrftoken = "csrftoken=(.*?);";
//                            Pattern pattern = Pattern.compile(csrftoken);
//                            Matcher matcher = pattern.matcher(src);
//
//                            if (matcher.find()) {
//                                Utils.setTOKEN(matcher.group(1));
//                                user.setToken(Utils.TOKEN);
//                                Log.e("MyTag", "token=" + Utils.TOKEN);
//                            }
//
//                            String sessionId = "sessionid=(.*?);";
//                            pattern = Pattern.compile(sessionId);
//                            matcher = pattern.matcher(src);
//
//                            if (matcher.find()) {
//                                Utils.setSessionId(matcher.group(1));
//                                user.setSessionId(Utils.SESSION_ID);
//                                Log.e("MyTag", Utils.SESSION_ID);
//                            }
//                            try{
//                                new Thread(() -> {
//                                    App.getDatabase().userDAO().save(user);
//                                    App.setMainUser(App.getDatabase().userDAO().getUser()[0]);
//                                    Utils.setTOKEN(App.getMainUser().token);
//                                    Utils.setSessionId(App.getMainUser().sessionId);
//                                    runOnUiThread(() -> {
//                                        binding.progressCircular.setVisibility(View.GONE);
//                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                                        finish();
//                                    });
//                                }).start();
//
//                            } catch (Exception e){
//                                binding.progressCircular.setVisibility(View.GONE);
//                            }
//
//                        }
//                        else {
//                            try {
//                                if (response.errorBody().string().contains("Invalid credentials")){
//                                    Snackbar.make(binding.getRoot(), "Логин или пароль неверны", Snackbar.LENGTH_LONG).show();
//                                }
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }
//                            binding.progressCircular.setVisibility(View.GONE);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<User> call, Throwable t) {
//                        Log.e("MyTag", "error", t);
//                        Snackbar.make(binding.getRoot(), "Что-то пошло не так", Snackbar.LENGTH_SHORT).show();
//                        binding.progressCircular.setVisibility(View.GONE);
//                    }
//                });
//
//            } else {
//                binding.wrongIndicator.setText(error);
//                binding.wrongIndicator.setVisibility(View.VISIBLE);
//                new Thread(() -> {
//                    int savedI = i;
//                    try {
//                        Thread.sleep(3500);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                    if (savedI == i) {
//                        runOnUiThread(() -> {
//                            binding.wrongIndicator.setVisibility(View.GONE);
//                        });
//                    }
//                }).start();
//            }
//            i++;
//        });
//        buttonRegistration.setOnClickListener(view -> {
//            startActivity(new Intent(this, RegistrationActivity.class));
//        });
//    }
//
//}
