package ru.senya.pixateka.activities;

import static ru.senya.pixateka.database.retrofit.Utils.BASE_URL;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.senya.pixateka.App;
import ru.senya.pixateka.database.retrofit.Utils;
import ru.senya.pixateka.database.retrofit.itemApi.Item;
import ru.senya.pixateka.database.retrofit.userApi.User;
import ru.senya.pixateka.database.retrofit.userApi.UsersInterface;
import ru.senya.pixateka.database.room.ItemEntity;


public class StartActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    StorageReference storageRef;
    Retrofit retrofit;
    UsersInterface service;
    List<ItemEntity> data = new ArrayList<>();
    AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        new Thread(() -> {
            data = App.getDatabase().itemDAO().getAll();
            App.getItemService().getAllPhotos().enqueue(new Callback<ArrayList<Item>>() {
                @Override
                public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        for (ItemEntity itemEntity : data) {
                            boolean deleted = true;
                            for (Item item : response.body()) {
                                if (itemEntity.id == item.id) {
                                    deleted = false;
                                    break;
                                }
                            }
                            if (deleted) {
                                new Thread(() -> {
                                    App.getDatabase().itemDAO().deleteByUserId(itemEntity.id);
                                }).start();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Item>> call, Throwable t) {

                }
            });
        }).start();

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(UsersInterface.class);

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);

        new Thread(() -> {
            if (App.getDatabase().userDAO().getUser().length == 1) {
                User mainUser = App.getDatabase().userDAO().getUser()[0];
                App.getUserService().getUser(mainUser.id, mainUser.token, "csrftoken="+mainUser.token+"; "+"sessionid="+mainUser.sessionId).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()){
                            User user = response.body();
                            user.token = mainUser.token;
                            user.sessionId = mainUser.sessionId;
                            App.setMainUser(user);
                            Utils.setTOKEN(user.token);
                            Utils.setSessionId(user.sessionId);
                            new Thread(()->{
                                App.getDatabase().userDAO().deleteUserTable();
                                App.getDatabase().userDAO().save(response.body());
                            }).start();
                            runOnUiThread(() -> {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                runOnUiThread(()->{
                                    activity.finish();
                                });
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(StartActivity.this, "error", Toast.LENGTH_SHORT).show();
                        runOnUiThread(()->{
                            activity.finish();
                        });
                    }
                });

            } else {
                runOnUiThread(() -> {
                    startActivity(new Intent(this, LoginActivity.class));
                });
            }

        }).start();

    }
}
