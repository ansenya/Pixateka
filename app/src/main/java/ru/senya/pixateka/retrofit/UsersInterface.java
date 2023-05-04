package ru.senya.pixateka.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UsersInterface {

    @GET("api/users/?format=json")
    Call<List<User>> getUsers();

}

