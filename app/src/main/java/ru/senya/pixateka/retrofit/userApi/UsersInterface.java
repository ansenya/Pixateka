package ru.senya.pixateka.retrofit.userApi;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UsersInterface {


    @GET("auth/token")
    Call<UserToken> login(@Header("Authorization") String credentials);

}

