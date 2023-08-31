package ru.senya.pixateka.database.retrofit.itemApi;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import ru.senya.pixateka.database.retrofit.Page;
import ru.senya.pixateka.models.ImageEntity;

public interface ItemInterface {

    @GET("img/getAll")
    Call<Page<ImageEntity>> getAll(@Header("Authorization") String token, @Query("page") int page);

    @Multipart
    @POST("img/upload")
    Call<ImageEntity> upload(@Header("Authorization") String token, @Part MultipartBody.Part file);

    @Multipart
    @POST("img/upload")
    Call<ImageEntity> upload(@Header("Authorization") String token, @Part MultipartBody.Part file, @Part("name") String name);
}
