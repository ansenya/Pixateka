package ru.senya.pixateka.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
import ru.senya.pixateka.subjects.Item;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    List<Item> itemsMain = new ArrayList<Item>();
    List<Item> itemsProfile = new ArrayList<Item>();
    FragmentProfile fragmentProfile = new FragmentProfile(itemsProfile);
    FragmentMain fragmentMain = new FragmentMain(itemsMain);
    FragmentNotifications fragmentNotifications = new FragmentNotifications();
    FragmentAdd fragmentAdd = new FragmentAdd(itemsMain, itemsProfile);
    FragmentSearch fragmentSearch = new FragmentSearch(itemsMain);
    int[] examples = new int[149];
    String[] examplesTXT = new String[149];
    Random random = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        initMain();
        setFragment(fragmentProfile);
        setFragment(fragmentMain);
        binding.navView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setFragment(fragmentMain);
                    return true;
                case R.id.navigation_notifications:
                    setFragment(fragmentNotifications);
                    return true;
                case R.id.navigation_add:
                    setFragment(fragmentAdd);
                    return true;
                case R.id.navigation_profile:
                    setFragment(fragmentProfile);
                    return true;
                case R.id.search:
                    setFragment(fragmentSearch);
                    return true;
            }
            return false;
        });
    }

    private void initMain() {
        for (int i = 0; i < 148; i++) {
            examples[i] = R.drawable.a1 + i;
            examplesTXT[i] = 1 + i + "";
            itemsMain.add(new Item(examples[i], examplesTXT[i]));
        }

    }

    @Override
    public void onBackPressed() {
        if (fragmentMain.visible() || fragmentProfile.visible()) {
            fragmentProfile.back();
            fragmentMain.back();
        } else finish();
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(binding.shit.getId(), fragment).commit();
    }

}