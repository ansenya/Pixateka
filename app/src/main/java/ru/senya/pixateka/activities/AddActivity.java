package ru.senya.pixateka.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import ru.senya.pixateka.R;
import ru.senya.pixateka.databinding.ActivityAddBinding;

public class AddActivity extends AppCompatActivity {

    ActivityAddBinding binding;
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageMetadata storageMetadata;
    Uri uri;

    String path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 3);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


        Log.e("MyTag", storageRef.toString());

        binding.button.setOnClickListener(v -> {
            if (binding.name.getInputText().isEmpty()) {
                binding.wrongName.setVisibility(View.VISIBLE);
                new Thread(() -> {
                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    runOnUiThread(() -> {
                        binding.wrongName.setVisibility(View.INVISIBLE);
                    });

                }).start();
            } else {
                StorageReference uploadRef = storageRef.child("images/"
                        + FirebaseAuth.getInstance().getCurrentUser().getUid()
                        + ":"
                        + java.time.LocalDateTime.now());

                storageMetadata = new StorageMetadata.Builder().
                        setContentType("image").
                        setCustomMetadata("userUid", FirebaseAuth.getInstance().getCurrentUser().getUid()).
                        setCustomMetadata("userEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail()).
                        setCustomMetadata("name", binding.name.getInputText()).
                        setCustomMetadata("description", binding.description.getInputText()).
                        setCustomMetadata("time", java.time.LocalTime.now().toString()).
                        build();
                binding.progressCircular.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.v1)));
                binding.progressCircular.setVisibility(View.VISIBLE);


                uploadRef.putFile(uri).
                        addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("MyTag", e.toString());
                                Toast.makeText(AddActivity.this, "upload failed", Toast.LENGTH_SHORT).show();
                                binding.progressCircular.setVisibility(View.INVISIBLE);
                            }
                        }).
                        addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                uploadRef.updateMetadata(storageMetadata).
                                        addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                            @Override
                                            public void onSuccess(StorageMetadata storageMetadata) {
                                                Log.e("MyTag", storageMetadata.getBucket());
                                                Toast.makeText(AddActivity.this, "upload successful", Toast.LENGTH_SHORT).show();
                                                binding.progressCircular.setVisibility(View.INVISIBLE);
                                                onBackPressed();
                                            }
                                        }).
                                        addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("MyTag", e.toString());
                                                Toast.makeText(AddActivity.this, "upload unsuccessful", Toast.LENGTH_SHORT).show();
                                                binding.progressCircular.setVisibility(View.INVISIBLE);
                                            }
                                        });
                            }
                        });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == RESULT_OK && data != null) {
            path = getRealPath(getApplicationContext(), data.getData());
            uri = data.getData();
            Glide.
                    with(this).
                    load(data.getData()).
                    into(binding.selectedPhoto);

        }
    }

    private static String getRealPath(Context context, Uri contentUri) {
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

}
