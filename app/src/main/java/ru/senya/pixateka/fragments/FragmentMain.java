package ru.senya.pixateka.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ru.senya.pixateka.adapters.RecyclerTouchListener;
import ru.senya.pixateka.adapters.RecyclerViewAdapter;
import ru.senya.pixateka.databinding.FragmentMainBinding;
import ru.senya.pixateka.subjects.Item;

public class FragmentMain extends Fragment {

    FragmentMainBinding binding;
    RecyclerView list;
    List<Item> items;
    RecyclerViewAdapter adapter;
   List<Item> newList = new ArrayList<Item>();
    Random random = new Random();

    public FragmentMain(List<Item> items) {
        this.items = items;
        adapter = new RecyclerViewAdapter(this.items);
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
                        if (items.get(position).getPic() == 0){
                            binding.fragment.update(items.get(position).getUri(), items.get(position).getName());
                        } else binding.fragment.update(items.get(position).getPic(), items.get(position).getName());
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
        binding.refreshButton.setOnClickListener(view ->{
            Collections.shuffle(items);
            list.getAdapter().notifyDataSetChanged();
        });
    }


}
