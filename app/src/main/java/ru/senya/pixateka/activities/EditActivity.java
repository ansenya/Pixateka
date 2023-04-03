package ru.senya.pixateka.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.room.ItemEntity;
import ru.senya.pixateka.room.UserEntity;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //listener();
    }

//    private void listener() {
//        binding.buttonSave.setOnClickListener(view -> {
//            new Thread(() -> {
//                App.getDatabase().userDAO().save(new UserEntity(R.drawable.a21,
//                        R.drawable.a20,
//                        binding.name.getInputText(),
//                        binding.surname.getInputText(),
//                        binding.geo.getInputText(),
//                        binding.birthday.getInputText(),
//                        binding.about.getInputText()));
//                finish();
//            }).start();
//        });
//    }
}
