package ru.senya.pixateka.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.room.ItemEntity;
import ru.senya.pixateka.room.UserEntity;

public class StartActivity extends AppCompatActivity {


    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private final String authorized = "Authorized";
    boolean b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        new Thread(() -> {
            App.getDatabase().userDAO().save(new UserEntity(R.drawable.a21, R.drawable.a20,
                    "",
                    "",
                    "",
                    "",
                    ""
                    ));
            App.getDatabase().itemDAO().save(new ItemEntity(R.drawable.a82, "123"));
        }).start();
        if (!preferences.contains(authorized)) {
            editor.putBoolean(authorized, false);
            editor.commit();
            startActivity(new Intent(this, EnterActivity.class));
            finish();
        }
        if (preferences.getBoolean(authorized, true)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, EnterActivity.class));
            finish();
        }


    }
}
