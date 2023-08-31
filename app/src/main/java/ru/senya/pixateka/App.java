package ru.senya.pixateka;

import static ru.senya.pixateka.database.retrofit.Utils.BASE_URL;

import android.app.Application;

import androidx.room.Room;

import java.util.LinkedList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.senya.pixateka.database.retrofit.itemApi.ItemInterface;
import ru.senya.pixateka.database.retrofit.userApi.User;
import ru.senya.pixateka.database.retrofit.userApi.UsersInterface;
import ru.senya.pixateka.database.room.Database;
import ru.senya.pixateka.database.room.ItemEntity;

public class App extends Application {
    private static App instance;
    private static Database database;
    private static Retrofit retrofit;
    private static ItemInterface itemService;
    private static UsersInterface userService;
    private static User mainUser;
    private static String token;
    private static LinkedList<ItemEntity> data = new LinkedList<>();


    @Override
    public void onCreate() {
        instance = this;
        database = Room.databaseBuilder(this, Database.class, "database").build();
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        itemService = retrofit.create(ItemInterface.class);
        userService = retrofit.create(UsersInterface.class);
        super.onCreate();
    }

    public static String getToken() {
        return token;
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

    public static User getMainUser() {
        return mainUser;
    }

    public static void setMainUser(User mainUser) {
        App.mainUser = mainUser;
    }

    public static LinkedList<ItemEntity> getData() {
        return data;
    }

    public static void setData(LinkedList<ItemEntity> data) {
        App.data = data;
    }
}
