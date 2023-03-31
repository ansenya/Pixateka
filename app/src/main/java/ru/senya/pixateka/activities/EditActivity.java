package ru.senya.pixateka.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.databinding.ActivityEditProfileBinding;
import ru.senya.pixateka.room.ItemEntity;
import ru.senya.pixateka.room.UserEntity;

public class EditActivity extends AppCompatActivity {
    ActivityEditProfileBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        listener();
        setContentView(binding.getRoot());
    }

    private void listener() {
        binding.buttonEdit.setOnClickListener(view -> {
            new Thread(() -> {
//                UserEntity user = App.getDatabase().userDAO().get("User");
                App.getDatabase().userDAO().save(new UserEntity(R.drawable.a21,
                        R.drawable.a20,
                        binding.name.getInputText(),
                        binding.surname.getInputText(),
                        binding.geo.getInputText(),
                        binding.birthday.getInputText(),
                        binding.about.getInputText()));
                finish();
            }).start();
        });
    }
}
