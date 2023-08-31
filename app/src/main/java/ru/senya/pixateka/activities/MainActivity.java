package ru.senya.pixateka.activities;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Retrofit;
import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.database.retrofit.itemApi.ItemInterface;
import ru.senya.pixateka.database.retrofit.userApi.User;
import ru.senya.pixateka.database.room.ItemEntity;
import ru.senya.pixateka.databinding.ActivityMainBinding;
import ru.senya.pixateka.fragments.FragmentMain;
import ru.senya.pixateka.fragments.FragmentProfile;
import ru.senya.pixateka.fragments.FragmentSearch;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ArrayList<ItemEntity> mainData = new ArrayList<>();
    ArrayList<ItemEntity> profileData = new ArrayList<>();
    FragmentProfile fragmentProfile;
    FragmentMain fragmentMain;
    FragmentSearch fragmentSearch;
    Retrofit retrofit;
    ItemInterface service;
    User mainUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();

        mainUser = App.getMainUser();
        retrofit = App.getRetrofit();
        service = App.getItemService();

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        adjustFragmentsAndNavigation();

        setContentView(binding.getRoot());
    }

    private void getData() {
        new Thread(() -> {
            App.getData().addAll(App.getDatabase().itemDAO().getAll());
            Collections.reverse(mainData);
            profileData.addAll(App.getDatabase().itemDAO().getAllProfile(App.getMainUser().getId()));
            Collections.reverse(profileData);
        }).start();
    }


    @SuppressLint("NonConstantResourceId")
    private void adjustFragmentsAndNavigation() {
        setupFragments();
        addFragments();

        binding.navView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction()
                            .hide(fragmentSearch)
                            .hide(fragmentProfile)
                            .show(fragmentMain)
                            .commit();
                    return true;
                case R.id.search:
                    getSupportFragmentManager().beginTransaction()
                            .hide(fragmentMain)
                            .hide(fragmentProfile)
                            .show(fragmentSearch)
                            .commit();
                    return true;
                case R.id.navigation_profile:
                    getSupportFragmentManager().beginTransaction()
                            .hide(fragmentMain)
                            .hide(fragmentSearch)
                            .show(fragmentProfile)
                            .commit();
                    return true;
            }
            return false;
        });
    }

    private void setupFragments() {
        fragmentMain = new FragmentMain(mainData);
        fragmentProfile = new FragmentProfile(profileData, mainUser, binding.vfs, binding.toolbar, 2);
        fragmentSearch = new FragmentSearch(mainData);
    }

    private void addFragments() {
        getSupportFragmentManager().beginTransaction()
                .add(binding.container.getId(), fragmentMain)
                .add(binding.container.getId(), fragmentSearch)
                .hide(fragmentSearch)
                .add(binding.container.getId(), fragmentProfile)
                .hide(fragmentProfile)
                .commit();
    }
}