package ru.senya.pixateka.database.retrofit.itemApi;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ItemInterface {
    @Multipart
    @POST("images/create/")
    Call<ResponseBody> uploadImage(
            @Part("user") RequestBody author,
            @Part MultipartBody.Part image,
            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Header("X-CSRFToken") String token,
            @Header("Cookie") String cookie
    );

    @GET("images/")
    Call<ArrayList<Item>> getAllPhotos();

    @GET("users/{id}/images")
    Call<ArrayList<Item>> getPhotosByUserId(@Path("id") int id);

    @DELETE("images/{id}/")
    Call<ResponseBody> deleteItem(@Path("id") int id, @Header("X-CSRFToken") String token, @Header("Cookie") String cookie);
}
