package ru.senya.pixateka.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.senya.pixateka.activities.EnterActivity;
import ru.senya.pixateka.activities.RegistrationActivity;
import ru.senya.pixateka.databinding.FragmentEnterBinding;
import ru.senya.pixateka.activities.MainActivity;
import ru.senya.pixateka.databinding.FragmentRegistratrationBinding;

public class FragmentEnter extends Fragment {

    FragmentEnterBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEnterBinding.inflate(inflater, container, false);
        binding.wrong.setVisibility(View.GONE);
        binding.button.setOnClickListener(view -> {
            if (checker()){
                startActivity(new Intent(binding.getRoot().getContext(), MainActivity.class));
            }
            else {
                binding.wrong.setVisibility(View.VISIBLE);
            }
        });
        binding.switcher.setOnClickListener(view ->
                startActivity(new Intent
                        (binding.getRoot().getContext(), RegistrationActivity.class))
        );
        return binding.getRoot();
    }

    private boolean checker(){
        if (binding.inputEmail.getInputText().toString().equals("mail@mail.com") &&
                binding.inputPassword.getInputText().toString().equals("123456")){
            return true;
        }
        return false;
    }
}
