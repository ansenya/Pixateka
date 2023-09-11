package ru.senya.pixateka.activities;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import retrofit2.Retrofit;
import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.retrofit.ItemInterface;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        retrofit = App.getRetrofit();
        service = App.getItemService();

        setupFragmentsAndNavigation();

        setContentView(binding.getRoot());
    }

    @SuppressLint("NonConstantResourceId")
    private void setupFragmentsAndNavigation() {
        initFragments();
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

    private void initFragments() {
        fragmentMain = new FragmentMain();
        fragmentSearch = new FragmentSearch();
        fragmentProfile = new FragmentProfile();
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