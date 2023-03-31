package ru.senya.pixateka.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.senya.pixateka.R;
import ru.senya.pixateka.databinding.ActivityMainBinding;
import ru.senya.pixateka.fragments.FragmentAdd;
import ru.senya.pixateka.fragments.FragmentMain;
import ru.senya.pixateka.fragments.FragmentNotifications;
import ru.senya.pixateka.fragments.FragmentProfile;
import ru.senya.pixateka.fragments.FragmentSearch;
import ru.senya.pixateka.App;
import ru.senya.pixateka.room.ItemDAO;
import ru.senya.pixateka.room.ItemEntity;
import ru.senya.pixateka.subjects.Item;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    List<ItemEntity> data = new ArrayList<>();
    FragmentProfile fragmentProfile = new FragmentProfile(data);
    FragmentMain fragmentMain = new FragmentMain(data);
    FragmentNotifications fragmentNotifications = new FragmentNotifications();
    FragmentAdd fragmentAdd = new FragmentAdd(data);
    FragmentSearch fragmentSearch = new FragmentSearch(data);
    int[] examples = new int[149];
    String[] examplesTXT = new String[149];
    ItemDAO itemDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        new Thread(() -> {
            data.addAll(App.getDatabase().itemDAO().getAll());
        }).start();
        setContentView(binding.getRoot());
        setFragments();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE},
                    1);
        }
    }

    private void initMain() {
        for (int i = 0; i < 148; i++) {
            examples[i] = R.drawable.a1 + i;
            examplesTXT[i] = 1 + i + "";
        }
    }

    private void initMain2() {
        new Thread(() -> {
            for (int i = 0; i < examples.length; i++) {
                ItemEntity item = new ItemEntity(examples[i], examplesTXT[i]);
                itemDAO.save(item);
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        if (binding.navigationMain.getVisibility()==View.VISIBLE) fragmentMain.back();
        else if (binding.navigationProfile.getVisibility()==View.VISIBLE) fragmentProfile.back();
        else if (binding.navigationSearch.getVisibility()==View.VISIBLE) fragmentSearch.back();
    }

    private void setFragment() {
        binding.navigationMain.setVisibility(View.GONE);
        binding.navigationProfile.setVisibility(View.GONE);
        binding.navigationNotifications.setVisibility(View.GONE);
        binding.navigationAdd.setVisibility(View.GONE);
        binding.navigationSearch.setVisibility(View.GONE);
    }

    @SuppressLint("NonConstantResourceId")
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
                case R.id.navigation_add:
                    setFragment();
                    binding.navigationAdd.setVisibility(View.VISIBLE);
                    fragmentAdd.reload();
                    return true;
                case R.id.navigation_profile:
                    setFragment();
                    binding.navigationProfile.setVisibility(View.VISIBLE);
                    fragmentProfile.myNotify();
                    return true;
                case R.id.search:
                    setFragment();
                    binding.navigationSearch.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        });
        getSupportFragmentManager().beginTransaction().
                replace(binding.navigationMain.getId(), fragmentMain).
                replace(binding.navigationAdd.getId(), fragmentAdd).
                replace(binding.navigationSearch.getId(), fragmentSearch).
                replace(binding.navigationNotifications.getId(), fragmentNotifications).
                replace(binding.navigationProfile.getId(), fragmentProfile)
                .commit();
        setFragment();
        binding.navigationMain.setVisibility(View.VISIBLE);

    }

}