package ru.senya.pixateka.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

import ru.senya.pixateka.activities.AddActivity;
import ru.senya.pixateka.adapters.NewRecyclerAdapter;
import ru.senya.pixateka.database.room.ItemEntity;
import ru.senya.pixateka.databinding.FragmentMainBinding;

public class FragmentMain extends Fragment {

    FragmentMainBinding binding;

    ArrayList<ItemEntity> data;

    public FragmentMain(ArrayList<ItemEntity> data) {
        this.data = data;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);

        adjustRecycler();
        adjustClickListeners();

        return binding.getRoot();
    }

    private void adjustRecycler() {
        binding.recycler.setAdapter(new NewRecyclerAdapter());
        binding.recycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    private void adjustClickListeners() {
        binding.fab.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), AddActivity.class));
        });

        binding.swipeContainer.setOnRefreshListener(() -> {

        });
    }


}
