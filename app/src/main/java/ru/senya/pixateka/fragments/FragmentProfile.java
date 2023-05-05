package ru.senya.pixateka.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

import ru.senya.pixateka.adapters.RecyclerAdapterMain;
import ru.senya.pixateka.database.room.ItemEntity;
import ru.senya.pixateka.databinding.NewFragmentProfileBinding;


public class FragmentProfile extends Fragment {

    NewFragmentProfileBinding binding;
    ArrayList<ItemEntity> data;


    public FragmentProfile(ArrayList<ItemEntity> data) {
        this.data = data;
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
        binding.recyclerList.setAdapter(new RecyclerAdapterMain(getActivity(), data, getContext(), null, binding.fragment, binding.recyclerList, null, null, null, null));
        binding.recyclerList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
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
