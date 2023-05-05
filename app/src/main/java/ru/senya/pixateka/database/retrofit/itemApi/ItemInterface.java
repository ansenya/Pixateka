package ru.senya.pixateka.database.retrofit.itemApi;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ItemInterface {



    @Multipart
    @POST("images/create/")
    Call<ResponseBody> uploadImage(
            @Part("author") RequestBody author,
            @Part MultipartBody.Part image,
            @Part("name") RequestBody name,
            @Part("description") RequestBody description
    );

//    @POST("images/create/")
////    Call<ModelResponse> uploadImage(@Body Model model);


    @GET("images/")
    Call<ArrayList<Item>> getAllPhotos();

    @GET("images/{id}")
    Call<ArrayList<Item>> getPhotosByUserId(@Path("id") String id);
}
