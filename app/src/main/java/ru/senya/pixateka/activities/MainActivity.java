package ru.senya.pixateka.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.senya.pixateka.adapters.RecyclerTouchListener;
import ru.senya.pixateka.databinding.ActivityMainBinding;
import ru.senya.pixateka.fragments.FragmentFullscreen;
import ru.senya.pixateka.subjects.Item;
import ru.senya.pixateka.adapters.MyAdapter;
import ru.senya.pixateka.R;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentFullscreen fragmentFullscreen = new FragmentFullscreen();
    final Random random = new Random();
    protected boolean click = true;
    List<Item> items = new ArrayList<Item>();
    private final byte COLUMNS = 2;
    MyAdapter adapter = new MyAdapter(items);
    int[] examples = {R.drawable.example1, R.drawable.example2, R.drawable.example3};
    String[] examplesTXT = {"1", "2", "3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        initRecycler();
        initFragments();
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

    private void initRecycler() {
        RecyclerView list = binding.list;
        list.setLayoutManager(new StaggeredGridLayoutManager(COLUMNS, StaggeredGridLayoutManager.VERTICAL));
        list.setAdapter(adapter);
        initImages();
        initClickListener();
    }

    public void initClickListener() {

        binding.list.addOnItemTouchListener(new RecyclerTouchListener(this, binding.list,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        if (click) {
                            fragmentFullscreen.setImageResource(items.get(position).getPic());
                            fragmentFullscreen.setTextResource(items.get(position).getName());
                            binding.fragment.setVisibility(View.VISIBLE);
                            click = !click;
                        } else {
                            binding.fragment.setVisibility(View.GONE);
                            click = !click;
                        }
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        Toast.makeText(getBaseContext(), "*long клик*", Toast.LENGTH_SHORT).show();
                    }
                }));
        binding.refreshButton.setOnClickListener(view -> {
            initImages();
            adapter.notifyDataSetChanged();
        });
    }

    private void initFragments() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment, fragmentFullscreen);
        fragmentTransaction.commit();
    }

}