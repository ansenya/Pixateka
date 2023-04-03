package ru.senya.pixateka.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.adapters.RecyclerTouchListener;
import ru.senya.pixateka.adapters.RecyclerViewAdapterRoom;
import ru.senya.pixateka.databinding.FragmentMainBinding;
import ru.senya.pixateka.room.ItemEntity;

public class FragmentMain extends Fragment {

    FragmentMainBinding binding;
    RecyclerView list;
    List<ItemEntity> items = new ArrayList<>();
    RecyclerViewAdapterRoom adapter;


    public FragmentMain(List<ItemEntity> items) {
        new Thread(()->{
            this.items = items;
            adapter = new RecyclerViewAdapterRoom(items);
        }).start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(LayoutInflater.from(getContext()), container, false);
        list = binding.mainRecyclerView;
        initRecycler();
        return binding.getRoot();
    }

    public boolean visible() {
        if (binding.fragment.getVisibility() == VISIBLE) return true;
        return false;
    }

    public void back() {
        binding.fragment.goUp();
        binding.refreshButton.setVisibility(VISIBLE);
        binding.fragment.setVisibility(GONE);
        binding.mainRecyclerView.setVisibility(VISIBLE);
        binding.a.setVisibility(VISIBLE);
    }

    private void initRecycler() {
        list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        list.setAdapter(adapter);
        listener();
    }

    private void listener() {
        list.addOnItemTouchListener(new RecyclerTouchListener(getContext(), list,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        binding.fragment.setVisibility(VISIBLE);
                        binding.mainRecyclerView.setVisibility(GONE);
                        binding.a.setVisibility(GONE);
                        binding.refreshButton.setVisibility(GONE);
                        if (items.get(position).getPic() == 0) {
                            binding.fragment.update(items.get(position).getPath(), items.get(position).getName());
                        } else {
                            binding.fragment.update(items.get(position).getPic(), items.get(position).getName());
                        }

                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
        binding.refreshButton.setOnClickListener(view -> {
            Collections.shuffle(items);
            list.getAdapter().notifyDataSetChanged();
            binding.mainRecyclerView.smoothScrollToPosition(0);
        });
    }

    public void myNotify() {
        adapter.notifyDataSetChanged();
    }

}
