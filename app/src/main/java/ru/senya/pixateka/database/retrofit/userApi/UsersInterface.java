package ru.senya.pixateka.database.retrofit.userApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UsersInterface {

    @GET("users/?format=json")
    Call<List<User>> getUsers();

}

