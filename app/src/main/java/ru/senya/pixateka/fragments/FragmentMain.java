package ru.senya.pixateka.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.senya.pixateka.R;

import ru.senya.pixateka.adapters.MyAdapter;
import ru.senya.pixateka.databinding.FragmentFullscreenBinding;
import ru.senya.pixateka.databinding.FragmentMainBinding;
import ru.senya.pixateka.databinding.ViewItemBinding;
import ru.senya.pixateka.subjects.Item;

public class FragmentMain extends Fragment {
    FragmentMainBinding binding;
    List<Item> items = new ArrayList<Item>();
    int[] examples = {R.drawable.example1, R.drawable.example2, R.drawable.example3};
    String[] examplesTXT = {"первая фотка", "вторая фотка", "третья фотка"};
    MyAdapter adapter;
    final Random random = new Random();;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        initRecycler();
        return binding.getRoot();
    }

    private void initRecycler() {
        adapter = new MyAdapter(items);
        RecyclerView main = binding.main;
        main.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        main.setAdapter(adapter);
        initImages();
    }
    private void initImages() {
        items.clear();
        int rand;
        for (int i = 0; i < 1000; i++) {
            rand = random.nextInt(3);
            items.add(new Item(examples[rand], examplesTXT[rand]));
            adapter.notifyDataSetChanged();
        }
    }
}
