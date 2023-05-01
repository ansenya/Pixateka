package ru.senya.pixateka.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.senya.pixateka.databinding.ActivityRegistrationBinding;

public class RegistrationActivity extends AppCompatActivity {

    ActivityRegistrationBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        init();
    }


    private void init() {
        binding.buttonEnter.setOnClickListener(view -> {
            if (binding.inputPassword.getInputText().equals(binding.inputRepeatPassword.getInputText())){
                firebaseAuth.createUserWithEmailAndPassword(binding.inputEmail.getInputText(), binding.inputPassword.getInputText())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(RegistrationActivity.this, "Authentication successful.",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, MainActivity.class));


                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });
    }

}
