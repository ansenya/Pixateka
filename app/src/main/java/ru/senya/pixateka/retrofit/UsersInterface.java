package ru.senya.pixateka.retrofit;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import ru.senya.pixateka.models.UserEntityToken;

public interface UsersInterface {


    @GET("auth/token")
    Call<UserEntityToken> login(@Header("Authorization") String credentials);

}

