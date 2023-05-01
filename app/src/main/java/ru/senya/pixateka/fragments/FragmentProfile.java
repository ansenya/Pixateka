package ru.senya.pixateka.fragments;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.adapters.RecyclerTouchListener;

import ru.senya.pixateka.databinding.NewFragmentProfileBinding;


public class FragmentProfile extends Fragment {

    NewFragmentProfileBinding binding;


    public FragmentProfile(Object o, Object a) {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = NewFragmentProfileBinding.inflate(inflater, container, false);
        initRecycler();
        binding.buttonEditProfile.setOnClickListener(v -> {
            getChildFragmentManager().
                    beginTransaction().
                    replace(binding.fragmentEdit.getId(), new FragmentEditProfile()).commit();

            binding.fragmentEdit.setVisibility(VISIBLE);
            binding.relativeLayout.setVisibility(GONE);
        });
        return binding.getRoot();
    }


    private void initRecycler() {

//        binding.recyclerList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        binding.recyclerList.setAdapter(adapter);
//        binding.recyclerList.addOnItemTouchListener(new RecyclerTouchListener(getContext(), binding.recyclerList,
//                new RecyclerTouchListener.ClickListener() {
//
//                    @Override
//                    public void onClick(View view, int position) {
//                        binding.fragment.setVisibility(VISIBLE);
//                        //binding.fragment.update(items.get(position).path, items.get(position).name);
//                        binding.relative.setVisibility(GONE);
//                    }
//
//                    @Override
//                    public void onLongClick(View view, int position) {
//
//                    }
//                }));
    }

    public boolean visible() {
        if (binding.fragment.getVisibility()==VISIBLE){
            return true;
        }
        return false;
    }

    public boolean isEditVisible(){
        if (binding.fragmentEdit.getVisibility()==VISIBLE){
            return true;
        }
        return false;
    }

    public void myNotify() {
        new Thread(() -> {
        }).start();
//        adapter.notifyDataSetChanged();
    }

    public void back() {
        if (binding.fragmentEdit.getVisibility() == VISIBLE){
            binding.fragmentEdit.setVisibility(GONE);
            binding.relativeLayout.setVisibility(VISIBLE);
        } else {
            binding.fragment.goUp();
            binding.fragment.setVisibility(GONE);
            binding.relative.setVisibility(VISIBLE);
        }

    }
}
