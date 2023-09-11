package ru.senya.pixateka.retrofit;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import ru.senya.pixateka.models.UserEntity;
import ru.senya.pixateka.models.UserEntityToken;

public interface UsersInterface {


    @GET("auth/token")
    Call<UserEntityToken> login(@Header("Authorization") String credentials);

    @GET("u/getOne")
    Call<UserEntity> getUser(@Header("Authorization") String credentials, @Query("id") String id);

}

