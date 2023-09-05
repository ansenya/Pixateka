package ru.senya.pixateka.retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import ru.senya.pixateka.database.room.ItemEntity;
import ru.senya.pixateka.models.Page;
import ru.senya.pixateka.models.ImageEntity;

public interface ItemInterface {

    @GET("img/getAll")
    Call<Page<ImageEntity>> getAll(@Header("Authorization") String token, @Query("page") int page);

    @Multipart
    @POST("img/upload")
    Call<ImageEntity> upload(@Header("Authorization") String token,
                             @Part MultipartBody.Part file,
                             @Part("name") RequestBody name);

    @DELETE("img/delete")
    Call<Void> delete(@Header("Authorization") String token, @Query("id") String id);

    @GET("img/getInfo")
    Call<ImageEntity> getById(@Header("Authorization") String token, @Query("id") String id);

    @GET("es/getAlike")
    Call<Page<ImageEntity>> getAlike(@Header("Authorization") String token, @Query("id") String id, @Query("page") int page);

    @GET("img/getByUserId")
    Call<Page<ImageEntity>> getByUid(@Header("Authorization") String token, @Query("uid") String uid, @Query("id") String id, @Query("page") int page);

    @GET("img/getByUserId")
    Call<Page<ImageEntity>> getByUid(@Header("Authorization") String token, @Query("uid") String uid, @Query("page") int page);

}
