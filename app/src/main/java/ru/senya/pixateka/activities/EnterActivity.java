package ru.senya.pixateka.activities;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ru.senya.pixateka.databinding.ActivityEnterBinding;
import ru.senya.pixateka.subjects.Item;

public class EnterActivity extends AppCompatActivity {

    ActivityEnterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityEnterBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        init();
    }

    void init() {
        binding.button.setOnClickListener(view -> {
            if (true) {
                startActivity(new Intent(this, RegistrationActivity.class));
                finish();
            }
        });
    }

}
