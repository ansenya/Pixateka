package ru.senya.pixateka.database.retrofit.userApi;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.senya.pixateka.database.retrofit.Page;
import ru.senya.pixateka.database.room.UserEntity;

public interface UsersInterface {

    @GET("usr/get")
    Call<Page<UserEntity>> getUsers(@Query("page") int page, @Query("pageSize") int pageSize);
}

