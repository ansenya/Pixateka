package ru.senya.pixateka;

import static ru.senya.pixateka.database.retrofit.Utils.BASE_URL;

import android.app.Application;

import androidx.room.Room;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.senya.pixateka.database.retrofit.itemApi.ItemInterface;
import ru.senya.pixateka.database.retrofit.userApi.User;
import ru.senya.pixateka.database.retrofit.userApi.UsersInterface;
import ru.senya.pixateka.database.room.Database;


public class App extends Application {
    private static App instance;
    private static Database database;
    private static Retrofit retrofit;
    private static ItemInterface service;
    private static UsersInterface usersInterface;
    private static User mainUser;

    public static User getMainUser() {
        return mainUser;
    }

    public static void setMainUser(User mainUser) {
        App.mainUser = mainUser;
    }

    public static App getInstance() {
        return instance;
    }

    public static Database getDatabase() {
        return database;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static ItemInterface getItemService() {
        return service;
    }
    public static UsersInterface getUserService() {
        return usersInterface;
    }

    @Override
    public void onCreate() {
        instance = this;
        database = Room.databaseBuilder(this, Database.class, "database").build();
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(ItemInterface.class);
        usersInterface = retrofit.create(UsersInterface.class);
        super.onCreate();
    }
}
