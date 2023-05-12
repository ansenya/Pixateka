package ru.senya.pixateka.activities;

import static ru.senya.pixateka.activities.AddActivity.getRealPath;
import static ru.senya.pixateka.database.retrofit.Utils.BASE_URL;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.davemorrissey.labs.subscaleview.ImageSource;

import java.io.File;
import java.io.IOException;
import java.security.KeyPairGenerator;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.adapters.RecyclerAdapterSearch;
import ru.senya.pixateka.database.retrofit.itemApi.Item;
import ru.senya.pixateka.database.retrofit.itemApi.ItemInterface;
import ru.senya.pixateka.database.room.ItemEntity;
import ru.senya.pixateka.databinding.ActvityTestBinding;
import ru.senya.pixateka.fragments.FragmentFullscreen;
import ru.senya.pixateka.fragments.FragmentMain;
import ru.senya.pixateka.fragments.FragmentNotifications;

public class TestActivity extends AppCompatActivity {
    ActvityTestBinding binding;
    Retrofit retrofit;
    ArrayList<ItemEntity> arrayList = new ArrayList<>();
    Uri uri;
    File file;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        binding = ActvityTestBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        binding.b.setOnClickListener(v -> {
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 3);
        });
        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), "asd");
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), "promprog");

//        App.getUserService().login(password, username).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()){
//                    try {
//                        Log.e("MyTag", response.body().string());
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
////                    App.getItemService().deleteItem(5).enqueue(new Callback<ResponseBody>() {
////                        @Override
////                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
////                            if (response.body()==null){
////                                try {
////                                    Log.e("MyTag", response.errorBody().string());
////                                } catch (IOException e) {
////                                    throw new RuntimeException(e);
////                                }
////                            } else {
////                                try {
////                                    Log.e("MyTag", response.body().string());
////                                } catch (IOException e) {
////                                    throw new RuntimeException(e);
////                                }
////                            }
////                        }
////
////                        @Override
////                        public void onFailure(Call<ResponseBody> call, Throwable t) {
////
////                        }
////                    });
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.e("MyTag", "error", t);
//            }
//        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri = data.getData();
        file = new File(getRealPath(getApplicationContext(), uri));
        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), "asd");
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), "promprog");
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), "email@email.com");
        RequestBody first_name = RequestBody.create(MediaType.parse("text/plain"), "first_name");
        RequestBody last_name = RequestBody.create(MediaType.parse("text/plain"), "last_name");
        RequestBody country = RequestBody.create(MediaType.parse("text/plain"), "ru");

        MultipartBody.Part avatar = MultipartBody.Part.createFormData("avatar", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

//        App.getUserService().register(username, avatar, password, email, first_name, last_name, country).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.body()==null){
//                    try {
//                        Log.e("MyTag", response.errorBody().string());
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//                else {
//                    try {
//                        Log.e("MyTag", response.body().string());
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.e("MyTag", "error", t);
//            }
//        });
    }
}
