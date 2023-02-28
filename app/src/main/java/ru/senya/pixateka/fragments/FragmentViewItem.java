package ru.senya.pixateka.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.senya.pixateka.databinding.ViewItemBinding;

public class FragmentViewItem extends Fragment {

    ViewItemBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ViewItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
