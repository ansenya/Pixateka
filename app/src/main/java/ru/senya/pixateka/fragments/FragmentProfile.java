package ru.senya.pixateka.fragments;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.adapters.RecyclerTouchListener;
import ru.senya.pixateka.adapters.RecyclerViewAdapter;
import ru.senya.pixateka.adapters.RecyclerViewAdapterRoom;
import ru.senya.pixateka.databinding.FragmentProfileBinding;
import ru.senya.pixateka.databinding.NewFragmentProfileBinding;
import ru.senya.pixateka.room.ItemEntity;
import ru.senya.pixateka.room.UserEntity;
import ru.senya.pixateka.subjects.Item;

public class FragmentProfile extends Fragment {

//    FragmentProfileBinding binding;
    NewFragmentProfileBinding binding;
    private int start = 0;
    List<ItemEntity> items;
    RecyclerViewAdapterRoom adapter;

    public FragmentProfile(List<ItemEntity> items) {
        this.items = items;
        adapter = new RecyclerViewAdapterRoom(this.items);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = NewFragmentProfileBinding.inflate(inflater, container, false);
        initRecycler();
        return binding.getRoot();
    }



    private void initRecycler() {
        new Thread(() -> {
            try {
                binding.back.setImageResource(App.getDatabase().userDAO().getId(1).back);
                binding.pfpImg.setImageResource(App.getDatabase().userDAO().getId(1).pfp);
            } catch (Exception e){}
        }).start();
        binding.buttonEditProfile.setOnClickListener(view ->{
            Toast.makeText(getContext(), "edit", Toast.LENGTH_SHORT).show();
        });
        binding.recyclerList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.recyclerList.setAdapter(adapter);
    }


    public boolean visible() {
        return false;
    }

    public void myNotify(){
        adapter.notifyDataSetChanged();
    }

    public void back() {
//        binding.fragment.goUp();
//        binding.fragment.setVisibility(GONE);
       // binding.relativeLayout.setVisibility(VISIBLE);
    }
}
