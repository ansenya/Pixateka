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

import ru.senya.pixateka.R;
import ru.senya.pixateka.databinding.ActivityMainBinding;
import ru.senya.pixateka.fragments.FragmentAdd;
import ru.senya.pixateka.fragments.FragmentMain;
import ru.senya.pixateka.fragments.FragmentNotifications;
import ru.senya.pixateka.fragments.FragmentProfile;
import ru.senya.pixateka.subjects.Item;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    List<Item> itemsMain = new ArrayList<Item>();
    List<Item> itemsProfile = new ArrayList<Item>();
    FragmentProfile fragmentProfile = new FragmentProfile(itemsProfile);
    FragmentMain fragmentMain = new FragmentMain(itemsMain);
    FragmentNotifications fragmentNotifications = new FragmentNotifications();
    FragmentAdd fragmentAdd = new FragmentAdd(itemsMain, itemsProfile);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        itemsMain.add(new Item(R.drawable.a1, "123"));
        itemsMain.add(new Item(R.drawable.a100, "123"));
        itemsMain.add(new Item(R.drawable.a123, "123"));
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
            }
            return false;
        });
    }

    @Override
    public void onBackPressed() {
        if (fragmentMain.visible() || fragmentProfile.visible()) {
            fragmentMain.back();
            fragmentProfile.back();
        } else finish();
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(binding.shit.getId(), fragment).commit();
    }

}