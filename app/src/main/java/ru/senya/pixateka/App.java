package ru.senya.pixateka;

import static ru.senya.pixateka.utils.Utils.BASE_URL;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Room;

import java.util.LinkedList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.senya.pixateka.models.UserEntity;
import ru.senya.pixateka.retrofit.ItemInterface;
import ru.senya.pixateka.retrofit.UsersInterface;
import ru.senya.pixateka.database.room.Database;
import ru.senya.pixateka.database.room.ItemEntity;

public class App extends Application {
    private static App instance;
    private static Database database;
    private static Retrofit retrofit;
    private static ItemInterface itemService;
    private static UsersInterface userService;
    private static UserEntity mainUserEntity;
    private static LinkedList<ItemEntity> data = new LinkedList<>();

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        database = Room.databaseBuilder(this, Database.class, "database").build();
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        itemService = retrofit.create(ItemInterface.class);
        userService = retrofit.create(UsersInterface.class);
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static void setSharedPreferences(SharedPreferences sharedPreferences) {
        App.sharedPreferences = sharedPreferences;
    }

    public static SharedPreferences.Editor getEditor() {
        return editor;
    }

    public static void setEditor(SharedPreferences.Editor editor) {
        App.editor = editor;
    }

    public static App getInstance() {
        return instance;
    }

    public static void setInstance(App instance) {
        App.instance = instance;
    }

    public static Database getDatabase() {
        return database;
    }

    public static void setDatabase(Database database) {
        App.database = database;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static void setRetrofit(Retrofit retrofit) {
        App.retrofit = retrofit;
    }

    public static ItemInterface getItemService() {
        return itemService;
    }

    public static void setItemService(ItemInterface itemService) {
        App.itemService = itemService;
    }

    public static UsersInterface getUserService() {
        return userService;
    }

    public static void setUserService(UsersInterface userService) {
        App.userService = userService;
    }

    public static UserEntity getMainUser() {
        return mainUserEntity;
    }

    public static void setMainUser(UserEntity mainUserEntity) {
        App.mainUserEntity = mainUserEntity;
    }

    public static LinkedList<ItemEntity> getData() {
        return data;
    }

    public static void setData(LinkedList<ItemEntity> data) {
        App.data = data;
    }
}
