package ru.senya.pixateka.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.senya.pixateka.databinding.ActivityRegistrationBinding;
import ru.senya.pixateka.fragments.FragmentEnter;
import ru.senya.pixateka.fragments.FragmentRegistraion;

public class RegistrationActivity extends AppCompatActivity {

    ActivityRegistrationBinding binding;
    int k = 0;
    Fragment fragmentRegistraion = new FragmentRegistraion();
    Fragment fragmentEnter = new FragmentEnter();

    FragmentManager fragmentManager = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        initFragments();
        initListener();
    }

    private void initFragments() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(binding.registration.getId(), fragmentRegistraion);
        fragmentTransaction.commit();
    }

    private void initListener() {
    }

}
