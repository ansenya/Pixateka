package ru.senya.pixateka.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.LinkedList;

import ru.senya.pixateka.adapters.RecyclerAdapterSecondary;
import ru.senya.pixateka.database.room.ItemEntity;
import ru.senya.pixateka.databinding.FragmentOtherPhotosBinding;

public class FragmentOtherPhotos extends Fragment {
    public FragmentOtherPhotosBinding binding;
    public RecyclerAdapterSecondary adapter;
    public LinkedList<ItemEntity> data = new LinkedList<>();
    private FragmentActivity activity;


    public FragmentOtherPhotos(RecyclerAdapterSecondary adapter, LinkedList<ItemEntity> data) {
        this.adapter = adapter;
        this.data = data;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOtherPhotosBinding.inflate(inflater, container, false);
        binding.list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.list.setAdapter(adapter);
        return binding.getRoot();
    }
}
