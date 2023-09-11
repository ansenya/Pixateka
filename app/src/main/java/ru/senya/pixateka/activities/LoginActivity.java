package ru.senya.pixateka.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.net.ConnectException;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.models.UserEntityToken;
import ru.senya.pixateka.retrofit.UsersInterface;
import ru.senya.pixateka.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    UsersInterface userService = App.getUserService();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getIntent().getStringExtra("auth") == null){
            startMainActivity();
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        sharedPreferences = App.getSharedPreferences();
        editor = App.getEditor();

        adjustClickListeners();

        setContentView(binding.getRoot());
    }

    private void adjustClickListeners() {
        binding.submitButton.setOnClickListener(view -> {
            binding.progressCircular.setActivated(true);

            if (checkFields()) {
                String credentials = Credentials.basic(binding.inputLogin.getInputText(), binding.inputPassword.getInputText());
                getToken(userService.login(credentials));
            } else binding.progressCircular.setActivated(false);

        });
    }

    private void getToken(Call<UserEntityToken> call) {
        call.enqueue(new Callback<UserEntityToken>() {
            @Override
            public void onResponse(@NonNull Call<UserEntityToken> call, @NonNull Response<UserEntityToken> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;

                    editor.putString("user_id", response.body().getId());
                    editor.putString("jwt_key", response.body().getToken());
                    editor.putString("username", response.body().getUsername());
                    editor.putBoolean("authorized", true);
                    editor.apply();

                    startMainActivity();
                } else if (response.code() == 401) {
                    binding.progressCircular.setActivated(true);

                    handleUnauthorizedError();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserEntityToken> call, @NonNull Throwable t) {
                binding.progressCircular.setActivated(true);

                try {
                    throw t;
                } catch (ConnectException e) {
                    handleConnectError(t);
                } catch (Throwable e) {
                    handleUnknownError(t);
                }
            }
        });
    }

    private void startMainActivity() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void handleUnauthorizedError() {
        Snackbar.make(binding.getRoot(), getString(R.string.invalid_login_password), Snackbar.LENGTH_LONG).show();
        setError(getString(R.string.invalid_login_password));
    }

    private void handleConnectError(Throwable t) {
        Snackbar.make(binding.getRoot(), getString(R.string.error_server_unavailable), Snackbar.LENGTH_LONG).show();
        Log.e("Auth", "err", t);
    }

    private void handleUnknownError(Throwable t) {
        Snackbar.make(binding.getRoot(), getString(R.string.error_idk), Snackbar.LENGTH_LONG).show();
        Log.e("Auth", "err", t);
    }

    private boolean checkFields() {
        boolean check = true;

        if (TextUtils.isEmpty(binding.inputLogin.getInputText())) {
            binding.inputLogin.setError(getString(R.string.error_login));
            new Handler().postDelayed(() -> binding.inputLogin.setError(null), 3000);
            check = false;
        }

        if (TextUtils.isEmpty(binding.inputPassword.getInputText())) {
            binding.inputPassword.setError(getString(R.string.error_password));
            new Handler().postDelayed(() -> binding.inputPassword.setError(null), 3000);
            check = false;
        }
        return check;
    }

    private void setError(String error) {
        binding.inputLogin.setError(error);
        binding.inputPassword.setError(error);
        new Handler().postDelayed(() -> {
            binding.inputLogin.setError(null);
            binding.inputPassword.setError(null);
        }, 3000);
    }


}
