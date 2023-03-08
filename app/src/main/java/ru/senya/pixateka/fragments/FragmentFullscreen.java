package ru.senya.pixateka.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.senya.pixateka.R;
import ru.senya.pixateka.databinding.FragmentFullscreenBinding;
import ru.senya.pixateka.interfaces.MyInterface;
import ru.senya.pixateka.view.InputField;
import ru.senya.pixateka.view.ViewItem;

public class FragmentFullscreen extends Fragment implements MyInterface {

    FragmentFullscreenBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFullscreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void setResources(int imageResource, String text) {
        binding.fragment.update(imageResource, text);
    }

}
