package ru.senya.pixateka.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import ru.senya.pixateka.R;
import ru.senya.pixateka.databinding.ActivityMainBinding;
import ru.senya.pixateka.fragments.FragmentAdd;
import ru.senya.pixateka.fragments.FragmentMain;
import ru.senya.pixateka.fragments.FragmentNotifications;
import ru.senya.pixateka.fragments.FragmentProfile;
import ru.senya.pixateka.fragments.FragmentSearch;
import ru.senya.pixateka.App;
import ru.senya.pixateka.room.ItemEntity;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    List<ItemEntity> data = new ArrayList<>();

    FragmentProfile fragmentProfile = new FragmentProfile(null, null);
    FragmentMain fragmentMain;
    FragmentNotifications fragmentNotifications = new FragmentNotifications();
    FragmentSearch fragmentSearch = new FragmentSearch(data);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        new Thread(() -> {
            data.addAll(App.getDatabase().itemDAO().getAll());
        }).start();
        setContentView(binding.getRoot());
        fragmentMain = new FragmentMain(data, getApplicationContext());
        setFragments();

        StorageReference list = FirebaseStorage.getInstance().getReference().child("images");
        list.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference file : listResult.getItems()) {
                    file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            new Thread(() -> {
                                file.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                            @Override
                                            public void onSuccess(StorageMetadata storageMetadata) {
                                                String description = storageMetadata.getCustomMetadata("description");
                                                if (description == null) description = "";

                                                ItemEntity item = new ItemEntity(
                                                        storageMetadata.getCustomMetadata("userUid"),
                                                        uri.toString(),
                                                        storageMetadata.getCustomMetadata("name"),
                                                        description,
                                                        storageMetadata.getCustomMetadata("userEmail"),
                                                        "");

                                                new Thread(() -> {
                                                    App.getDatabase().itemDAO().save(item);
                                                    runOnUiThread(() -> {
                                                        data.add(item);
                                                        fragmentMain.myNotify(data.size() - 1);
                                                    });
                                                }).start();

                                            }
                                        }).

                                        addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("Metadata", e.toString());
                                            }
                                        });

                            }).start();
                        }
                    });
                }
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE},
                    1);
        }
    }


    @Override
    public void onBackPressed() {
        if (binding.navigationMain.getVisibility() == View.VISIBLE) fragmentMain.back();
        else if (binding.navigationProfile.getVisibility() == View.VISIBLE)
            fragmentProfile.back();
        else if (binding.navigationSearch.getVisibility() == View.VISIBLE)
            fragmentSearch.back();
        else if (fragmentProfile.isEditVisible()) {
            fragmentProfile.back();
        }
    }

    private void setFragment() {
        binding.navigationMain.setVisibility(View.INVISIBLE);
        binding.navigationProfile.setVisibility(View.INVISIBLE);
        binding.navigationNotifications.setVisibility(View.INVISIBLE);
        binding.navigationSearch.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("NonConstantResourceId")
    private void setFragments() {
        binding.navView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setFragment();
                    binding.navigationMain.setVisibility(View.VISIBLE);
                    fragmentMain.myNotify();
                    return true;
                case R.id.navigation_notifications:
                    setFragment();
                    binding.navigationNotifications.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_profile:
                    setFragment();
                    binding.navigationProfile.setVisibility(View.VISIBLE);
                    fragmentProfile.myNotify();
                    return true;
                case R.id.search:
                    setFragment();
                    binding.navigationSearch.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        });
        getSupportFragmentManager().beginTransaction().
                replace(binding.navigationMain.getId(), fragmentMain).
                replace(binding.navigationSearch.getId(), fragmentSearch).
                replace(binding.navigationNotifications.getId(), fragmentNotifications).
                replace(binding.navigationProfile.getId(), fragmentProfile)
                .commit();
        setFragment();
        binding.navigationMain.setVisibility(View.VISIBLE);

    }

}