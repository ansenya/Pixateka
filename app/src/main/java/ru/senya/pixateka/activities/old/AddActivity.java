package ru.senya.pixateka.activities.old;

import static ru.senya.pixateka.database.retrofit.Utils.getRealPath;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.database.retrofit.itemApi.ItemInterface;
import ru.senya.pixateka.databinding.ActivityAddBinding;

public class AddActivity extends AppCompatActivity {

    ActivityAddBinding binding;
    Uri uri;
    Retrofit retrofit;
    ItemInterface service;

    String path;
    String wrong = "";
    int i = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 3);
        retrofit = App.getRetrofit();
        service = App.getItemService();
        binding.submitButton.setOnClickListener(clickListener);
        binding.selectedPhoto.setOnClickListener(v -> {
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 3);
        });
        binding.mainToolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        binding.mainToolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        binding.mainToolbar.setTitle("Выбрать фото");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        setContentView(binding.getRoot());
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            binding.wrongPic.setVisibility(View.INVISIBLE);
            path = getRealPath(getApplicationContext(), data.getData());
            uri = data.getData();
            Glide.
                    with(this).
                    load(data.getData()).
                    into(binding.selectedPhoto);
        } else if (data == null) {
            Snackbar.make(binding.getRoot(), "Вы не выбрали фото!", Snackbar.LENGTH_SHORT).show();
//            binding.wrongPic.setVisibility(View.VISIBLE);
        }
    }



    private View.OnClickListener clickListener = v -> {
        i++;
        String input = "43083945";
//        if (!binding.name.getInputText().trim().isEmpty()){
//            input = binding.name.getInputText();
//        }
        binding.progressCircular.setVisibility(View.VISIBLE);

        File file = new File(getRealPath(getApplicationContext(), uri));

        RequestBody authorBody = RequestBody.create(MediaType.parse("text/plain"), "admin");
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), input);
        RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), binding.inputDescription.getInputText());
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

//        Call<ResponseBody> call = service.uploadImage(authorBody, imagePart, nameBody, descriptionBody, App.getMainUser().token, "csrftoken=" + App.getMainUser().token + "; " + "sessionid=" + App.getMainUser().sessionId);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    binding.submitButton.setOnClickListener(clickListener);
//                    onBackPressed();
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Snackbar.make(binding.getRoot(), "Загрузить не получилось :(", Snackbar.LENGTH_SHORT).show();
//                binding.progressCircular.setVisibility(View.INVISIBLE);
//                binding.submitButton.setOnClickListener(clickListener);
//                Log.e("MyTag", call.toString(), t);
//            }
//        });


    };


}