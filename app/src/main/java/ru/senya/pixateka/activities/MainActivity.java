package ru.senya.pixateka.activities;

import static ru.senya.pixateka.database.retrofit.Utils.BASE_URL;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.database.retrofit.itemApi.Item;
import ru.senya.pixateka.database.retrofit.itemApi.ItemInterface;
import ru.senya.pixateka.database.retrofit.userApi.User;
import ru.senya.pixateka.database.room.ItemEntity;
import ru.senya.pixateka.databinding.ActivityMainBinding;
import ru.senya.pixateka.fragments.FragmentMain;
import ru.senya.pixateka.fragments.FragmentNotifications;
import ru.senya.pixateka.fragments.FragmentProfile;
import ru.senya.pixateka.fragments.FragmentSearch;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ArrayList<ItemEntity> mainData = new ArrayList<>();
    ArrayList<ItemEntity> profileData = new ArrayList<>();
    FragmentProfile fragmentProfile;
    FragmentMain fragmentMain;
    FragmentNotifications fragmentNotifications = new FragmentNotifications();
    FragmentSearch fragmentSearch;
    Retrofit retrofit;
    ItemInterface service;
    User mainUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        mainUser = App.getMainUser();
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(ItemInterface.class);
        getData();
        profileData();
        checkPermission();
        fragmentMain = new FragmentMain(mainData, getApplicationContext());
        fragmentProfile = new FragmentProfile(profileData, mainUser);
        fragmentSearch = new FragmentSearch(mainData);

        setFragments();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onBackPressed() {
        if (binding.navigationMain.getVisibility() == View.VISIBLE) fragmentMain.back();
        else if (binding.navigationProfile.getVisibility() == View.VISIBLE)
            fragmentProfile.back();
        else if (binding.navigationSearch.getVisibility() == View.VISIBLE)
            fragmentSearch.back();
        else if (fragmentProfile.isEditVisible()) {
            fragmentProfile.back();
        }
    }

    private void getData() {
        new Thread(() -> {
            mainData.addAll(App.getDatabase().itemDAO().getAll());
        }).start();
    }

    private void profileData() {
        Call<ArrayList<Item>> call = service.getPhotosByUserId(App.getMainUser().id);
        call.enqueue(new Callback<ArrayList<Item>>() {
            @Override
            public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                Toast.makeText(MainActivity.this, "pp good", Toast.LENGTH_SHORT).show();
                for (Item item : response.body()) {
                    profileData.add(new ItemEntity(item.id, item.author, item.image, item.name, item.description, item.author, item.tags));
                }
                fragmentProfile.myNotify();
            }

            @Override
            public void onFailure(Call<ArrayList<Item>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "pp bad", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
            Toast.makeText(this, "Need Permission to access storage for Downloading Image", Toast.LENGTH_SHORT).show();
        }

    }


    private void setFragment() {
        binding.navigationMain.setVisibility(View.INVISIBLE);
        binding.navigationProfile.setVisibility(View.INVISIBLE);
        binding.navigationNotifications.setVisibility(View.INVISIBLE);
        binding.navigationSearch.setVisibility(View.INVISIBLE);
    }


    private void setFragments() {
        binding.navView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setFragment();
                    binding.navigationMain.setVisibility(View.VISIBLE);
                    fragmentMain.myNotify();
                    return true;
                case R.id.navigation_notifications:
                    setFragment();
                    binding.navigationNotifications.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_profile:
                    setFragment();
                    binding.navigationProfile.setVisibility(View.VISIBLE);
                    fragmentProfile.myNotify();
                    return true;
                case R.id.search:
                    setFragment();
                    if (binding.navigationSearch.getVisibility() == View.VISIBLE)
                        fragmentSearch.back();
                    binding.navigationSearch.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        });
        getSupportFragmentManager().beginTransaction().
                replace(binding.navigationMain.getId(), fragmentMain).
                replace(binding.navigationSearch.getId(), fragmentSearch).
                replace(binding.navigationNotifications.getId(), fragmentNotifications).
                replace(binding.navigationProfile.getId(), fragmentProfile)
                .commit();
        setFragment();
        binding.navigationMain.setVisibility(View.VISIBLE);

    }

}