package ru.senya.pixateka.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.senya.pixateka.databinding.ActvityTestBinding;
import ru.senya.pixateka.fragments.FragmentNotifications;

public class TestActivity extends AppCompatActivity {
    ActvityTestBinding binding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        binding = ActvityTestBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

    }
}
