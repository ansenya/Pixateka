package ru.senya.pixateka.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.senya.pixateka.App;
import ru.senya.pixateka.database.retrofit.Page;
import ru.senya.pixateka.database.room.UserEntity;
import ru.senya.pixateka.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        adjustClickListeners();

        setContentView(binding.getRoot());
    }

    private void adjustClickListeners(){
        binding.submitButton.setOnClickListener(view -> {
            if (checkFields()) {
                App.getUserService().getUsers(0, 10).enqueue(new Callback<Page<UserEntity>>() {
                    @Override
                    public void onResponse(Call<Page<UserEntity>> call, Response<Page<UserEntity>> response) {
                        Log.e("MyTag", response.message());
                    }

                    @Override
                    public void onFailure(Call<Page<UserEntity>> call, Throwable t) {

                    }
                });
            }

        });
    }

    private boolean checkFields(){
        boolean check = true;

        if (binding.inputLogin.getInputText().trim().isEmpty()){
            binding.inputLogin.setError("Введите логин!");
            check = false;
        }

        if (binding.inputPassword.getInputText().trim().isEmpty()){
            binding.inputPassword.setError("Введите логин!");
            check = false;
        }
        return check;
    }


}
