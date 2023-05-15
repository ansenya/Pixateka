package ru.senya.pixateka.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

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
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageMetadata storageMetadata;
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
        binding.button.setOnClickListener(clickListener);
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
            binding.wrongPic.setVisibility(View.INVISIBLE);
            path = getRealPath(getApplicationContext(), data.getData());
            uri = data.getData();
            Glide.
                    with(this).
                    load(data.getData()).
                    into(binding.selectedPhoto);
        } else if (data == null) {
            Toast.makeText(this, "Вы не выбрали фото!", Toast.LENGTH_SHORT).show();
            binding.wrongPic.setVisibility(View.VISIBLE);
        }
    }

    public static String getRealPath(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;
        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    private View.OnClickListener clickListener = v -> {
        i++;
        String input = "43083945";
        if (!binding.name.getInputText().trim().isEmpty()){
            input = binding.name.getInputText();
        }
        binding.progressCircular.setVisibility(View.VISIBLE);

        File file = new File(getRealPath(getApplicationContext(), uri));

        RequestBody authorBody = RequestBody.create(MediaType.parse("text/plain"), "admin");
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), input);
        RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), binding.description.getInputText());
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

        Call<ResponseBody> call = service.uploadImage(authorBody, imagePart, nameBody, descriptionBody, App.getMainUser().token, "csrftoken=" + App.getMainUser().token + "; " + "sessionid=" + App.getMainUser().sessionId);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    binding.button.setOnClickListener(clickListener);
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AddActivity.this, "upload failed", Toast.LENGTH_SHORT).show();
                binding.progressCircular.setVisibility(View.INVISIBLE);
                binding.button.setOnClickListener(clickListener);
                Log.e("MyTag", call.toString(), t);
            }
        });


    };


}