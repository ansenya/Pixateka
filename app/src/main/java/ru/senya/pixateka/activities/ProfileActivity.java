package ru.senya.pixateka.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.database.retrofit.userApi.User;
import ru.senya.pixateka.database.room.ItemEntity;
import ru.senya.pixateka.databinding.ActivityProfileBinding;
import ru.senya.pixateka.fragments.FragmentProfile;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    FragmentProfile fragmentProfile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        String uid = getIntent().getStringExtra("uid");
        binding.toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        new Thread(() -> {
            ArrayList<ItemEntity> arrayList = (ArrayList<ItemEntity>) App.getDatabase().itemDAO().getAllProfile(uid);
            Collections.reverse(arrayList);
            App.getUserService().getUser(Integer.parseInt(uid), App.getMainUser().token, "csrftoken=" + App.getMainUser().token + "; " + "sessionid=" + App.getMainUser().sessionId).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        fragmentProfile = new FragmentProfile(arrayList,
                                response.body(),
                                binding.vfs, binding.toolbar, 1);
                        runOnUiThread(() -> {
                            getSupportFragmentManager().beginTransaction().replace(binding.profile.getId(), fragmentProfile).commit();
                        });
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(ProfileActivity.this, "no internet connection", Toast.LENGTH_SHORT).show();
                    if (getApplicationContext() != null) onBackPressed();
                }
            });
        }).start();


    }

    @Override
    public void onBackPressed() {
        if (binding.vfs.getVisibility() == View.VISIBLE) {
            fragmentProfile.back();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        binding = null;
        fragmentProfile = null;
        super.onDestroy();
    }
}
