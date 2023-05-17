package ru.senya.pixateka.database.retrofit.userApi;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UsersInterface {

    @GET("users/?format=json")
    Call<List<User>> getUsers();

    @GET("users/{id}/")
    Call<User> getUser(@Path("id") int id, @Header("X-CSRFToken") String token, @Header("Cookie") String cookie);


    @Multipart
    @POST("users/register/")
    Call<User> register(
            @Part("username") RequestBody username,
            @Part MultipartBody.Part avatar,
            @Part("password") RequestBody password,
            @Part("email") RequestBody email,
            @Part("first_name") RequestBody first_name,
            @Part("last_name") RequestBody last_name,
            @Part("country") RequestBody country
    );

    @Multipart
    @POST("login/")
    Call<User> login(@Part("password") RequestBody password,
                     @Part("username") RequestBody username);

    @POST("login/")
    Call<ResponseBody> logout(@Header("X-CSRFToken") String token, @Header("Cookie") String cookie);

    @Multipart
    @PUT("users/{id}/edit/")
    Call<User> editUser(@Path("id") int id,
                        @Part("first_name") String first_name,
                        @Part("last_name") String last_name,
                        @Part("country") String country,
                        @Part("about") String about,
                        MultipartBody.Part avatar);

    @Multipart
//    @Headers("Content-Type: multipart/form-data")
    @POST("users/{id}/edit/")
    Call<ResponseBody> editUserAvatar(@Path("id") int id,
                                      @Header("X-CSRFToken") String token,
                                      @Header("Cookie") String cookie,
                                      @Part MultipartBody.Part avatar);

}

