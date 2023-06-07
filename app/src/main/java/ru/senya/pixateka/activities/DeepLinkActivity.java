package ru.senya.pixateka.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.util.List;

import ru.senya.pixateka.R;
import ru.senya.pixateka.databinding.ActvityTestBinding;

public class DeepLinkActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Uri deeplinkUri = intent.getData();
        if (deeplinkUri == null) {
            startActivity(new Intent(this, StartActivity.class));
        } else {
            Log.e("asd", deeplinkUri.getPath());
            startActivity(new Intent(this, StartActivity.class).putExtra("link", deeplinkUri.getPath()));
        }
    }

}
