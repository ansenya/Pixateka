package ru.senya.pixateka.activities;

import static ru.senya.pixateka.retrofit.Utils.BASE_URL;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.senya.pixateka.databinding.ActvityTestBinding;
import ru.senya.pixateka.retrofit.User;
import ru.senya.pixateka.retrofit.Users;
import ru.senya.pixateka.retrofit.UsersInterface;

public class TestActivity extends AppCompatActivity {
    ActvityTestBinding binding;
    Retrofit retrofit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        binding = ActvityTestBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        UsersInterface usersInterface = retrofit.create(UsersInterface.class);

        Call<List<User>> call = usersInterface.getUsers();

        call.enqueue(new Callback<List<User>>() {
            String s="";
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                ArrayList<User> users = new ArrayList<>();
                users.addAll(response.body());
                for (User user:users){
                    s+=user.email;
                    s+="\n";
                }
                runOnUiThread(()->{
                    binding.text.setText(s);
                });
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });

    }
}
