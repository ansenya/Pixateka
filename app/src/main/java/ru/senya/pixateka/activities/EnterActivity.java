package ru.senya.pixateka.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.senya.pixateka.databinding.ActivityEnterBinding;
import ru.senya.pixateka.fragments.FragmentEnter;

public class EnterActivity extends AppCompatActivity {

    ActivityEnterBinding binding;
    Fragment fragment = new FragmentEnter();
    FragmentManager fragmentManager = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        binding = ActivityEnterBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        initFragments();
    }

    private void initFragments() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(binding.registration.getId(), fragment);
        fragmentTransaction.commit();
    }



}
