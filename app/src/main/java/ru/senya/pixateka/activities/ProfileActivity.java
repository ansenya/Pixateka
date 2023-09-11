package ru.senya.pixateka.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.senya.pixateka.databinding.ActivityProfileBinding;
import ru.senya.pixateka.fragments.FragmentProfile;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    FragmentProfile fragmentProfile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
    }


}
