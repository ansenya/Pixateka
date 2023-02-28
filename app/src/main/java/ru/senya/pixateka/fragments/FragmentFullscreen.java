package ru.senya.pixateka.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.senya.pixateka.R;
import ru.senya.pixateka.databinding.FragmentFullscreenBinding;
import ru.senya.pixateka.databinding.ViewItemBinding;

public class FragmentFullscreen extends Fragment {

    FragmentFullscreenBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFullscreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void setImageResource(int imageResource) {
        binding.pic.setImageResource(imageResource);
    }
    public void setTextResource(String text) {
        binding.text.setText(text);
    }

}
