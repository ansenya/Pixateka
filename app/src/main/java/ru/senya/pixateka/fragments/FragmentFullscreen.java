package ru.senya.pixateka.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import ru.senya.pixateka.database.room.ItemEntity;
import ru.senya.pixateka.databinding.FragmentFullscreenBinding;

public class FragmentFullscreen extends Fragment  {

    FragmentFullscreenBinding binding;
    ItemEntity item;


    public FragmentFullscreen(ItemEntity item, FragmentActivity activity) {
        this.item = item;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFullscreenBinding.inflate(inflater, container, false);
        binding.view.fullScroll(View.FOCUS_UP);
        binding.fragment.update(item, getActivity());
        return binding.getRoot();
    }


}
