package ru.senya.pixateka.activities;

import static ru.senya.pixateka.database.retrofit.Utils.BASE_URL;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.database.retrofit.itemApi.Item;
import ru.senya.pixateka.database.retrofit.itemApi.ItemInterface;
import ru.senya.pixateka.databinding.ActivityMainBinding;
import ru.senya.pixateka.fragments.FragmentMain;
import ru.senya.pixateka.fragments.FragmentNotifications;
import ru.senya.pixateka.fragments.FragmentProfile;
import ru.senya.pixateka.fragments.FragmentSearch;
import ru.senya.pixateka.database.room.ItemEntity;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ArrayList<ItemEntity> mainData = new ArrayList<>();
    ArrayList<ItemEntity> profileData = new ArrayList<>();
    FragmentProfile fragmentProfile;
    FragmentMain fragmentMain;
    FragmentNotifications fragmentNotifications = new FragmentNotifications();
    FragmentSearch fragmentSearch = new FragmentSearch(mainData);
    Retrofit retrofit;
    ItemInterface service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(ItemInterface.class);
        getData();
        profileData();
        checkPermission();
        fragmentMain = new FragmentMain(mainData, getApplicationContext());
        fragmentProfile = new FragmentProfile(profileData);
        setFragments();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

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

    private void getData() {
        new Thread(() -> {
            mainData.addAll(App.getDatabase().itemDAO().getAll());
        }).start();

        new Thread(() -> {
            FirebaseStorage.getInstance().getReference().child("images").listAll().
                    addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult listResult) {
                            if (listResult.getItems().size() == 0) {
                                new Thread(() -> {
                                    mainData.clear();
                                    App.getDatabase().itemDAO().delete();
                                }).start();
                            } else {
                                for (StorageReference file : listResult.getItems()) {
                                    file.getDownloadUrl().addOnSuccessListener(uri -> {
                                        boolean containsImage = false;

                                        for (ItemEntity item : mainData) {
                                            if (item.getPath().equals(uri.toString()))
                                                containsImage = true;
                                        }
                                        if (!containsImage) {
                                            new Thread(() -> {
                                                file.getMetadata().
                                                        addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                                            @Override
                                                            public void onSuccess(StorageMetadata storageMetadata) {
                                                                String description = storageMetadata.getCustomMetadata("description");
                                                                if (description == null)
                                                                    description = "";

                                                                ItemEntity item = new ItemEntity( 1,
                                                                        storageMetadata.getCustomMetadata("userUid"), //uid
                                                                        uri.toString(), //path to image
                                                                        storageMetadata.getCustomMetadata("name"), //name of the file
                                                                        description, //description
                                                                        storageMetadata.getCustomMetadata("userEmail"), //uEmail
                                                                        "");

                                                                new Thread(() -> {
                                                                    App.getDatabase().itemDAO().save(item);
                                                                    runOnUiThread(() -> {
                                                                        mainData.add(item);
                                                                        fragmentMain.myNotify(mainData.size() - 1);
                                                                    });
                                                                }).start();

                                                            }
                                                        }).
                                                        addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.e("DownloadingError", e.toString());
                                                                Toast.makeText(MainActivity.this, "Downloading images from server failed", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                            }).start();
                                        }

                                    });
                                }
                            }
                        }
                    }).
                    addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("DownloadingError", e.toString());
                            Toast.makeText(MainActivity.this, "Downloading images from server failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        });


        Call<ArrayList<Item>> call = service.getAllPhotos();

        call.enqueue(new Callback<ArrayList<Item>>() {
            @Override
            public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                Toast.makeText(MainActivity.this, "good", Toast.LENGTH_SHORT).show();
                if (response.body() != null && response.body().size() > 0) {
                    ArrayList<Item> items = response.body();
                    new Thread(() -> {
                        for (Item item : items) {
                            ItemEntity itemEntity = new ItemEntity(item.id, item.author, item.image, item.name, item.description, item.author, item.tags);
                            if (!mainData.contains(itemEntity)) {
                                App.getDatabase().itemDAO().save(itemEntity);
                            }
                        }
                        mainData.addAll(App.getDatabase().itemDAO().getAll());
                        runOnUiThread(() -> {
                            fragmentMain.myNotify();
                        });
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Item>> call, Throwable t) {

            }
        });

    }

    private void profileData(){
        Call<ArrayList<Item>> call = service.getPhotosByUserId("admin");
        call.enqueue(new Callback<ArrayList<Item>>() {
            @Override
            public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                Toast.makeText(MainActivity.this, "pp good", Toast.LENGTH_SHORT).show();
                for (Item item: response.body()){
                    profileData.add(new ItemEntity(item.id, item.author, item.image, item.name, item.description, item.author, item.tags));
                }
                fragmentProfile.myNotify();
            }

            @Override
            public void onFailure(Call<ArrayList<Item>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "pp bad", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
    }


    private void setFragment() {
        binding.navigationMain.setVisibility(View.INVISIBLE);
        binding.navigationProfile.setVisibility(View.INVISIBLE);
        binding.navigationNotifications.setVisibility(View.INVISIBLE);
        binding.navigationSearch.setVisibility(View.INVISIBLE);
    }


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