package ru.senya.pixateka.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.senya.pixateka.adapters.RecyclerTouchListener;
import ru.senya.pixateka.subjects.Item;
import ru.senya.pixateka.adapters.MyAdapter;
import ru.senya.pixateka.R;
import ru.senya.pixateka.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    final Random random = new Random();
    List<Item> items = new ArrayList<Item>();
    private final byte COLUMNS = 2;
    MyAdapter adapter = new MyAdapter(items);
    int[] examples = {R.drawable.example1, R.drawable.example2, R.drawable.example3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        initImages();
        initRecycler();
        initClickListener();
    }

    private void initImages(){
        items.clear();
        int rand;
        for (int i = 0; i < 1000; i++) {
            rand = random.nextInt(3);
            items.add(new Item(examples[rand]));
            adapter.notifyDataSetChanged();

        }
    }
    private void initRecycler(){
        RecyclerView list = binding.list;
        list.setLayoutManager(new StaggeredGridLayoutManager(COLUMNS, StaggeredGridLayoutManager.VERTICAL));
        list.setAdapter(adapter);
    }
    public void initClickListener(){
        binding.list.addOnItemTouchListener(new RecyclerTouchListener(this, binding.list,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        binding.fragment.setVisibility(View.VISIBLE);
                        Toast.makeText(getBaseContext(), "*клик*", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onLongClick(View view, int position) {
                        Toast.makeText(getBaseContext(), "*long лоик*", Toast.LENGTH_SHORT).show();

                    }
                }));
        binding.refreshButton.setOnClickListener(view -> {
            initRecycler();
            adapter.notifyDataSetChanged();
        });
    }
}