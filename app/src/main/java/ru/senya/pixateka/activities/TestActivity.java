package ru.senya.pixateka.activities;

import static ru.senya.pixateka.database.retrofit.Utils.BASE_URL;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.senya.pixateka.database.retrofit.itemApi.Item;
import ru.senya.pixateka.database.retrofit.itemApi.ItemInterface;
import ru.senya.pixateka.databinding.ActvityTestBinding;

public class TestActivity extends AppCompatActivity {
    ActvityTestBinding binding;
    Retrofit retrofit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        binding = ActvityTestBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        ItemInterface service = retrofit.create(ItemInterface.class);


    }
}
