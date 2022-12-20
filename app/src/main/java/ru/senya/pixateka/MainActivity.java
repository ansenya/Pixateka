package ru.senya.pixateka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

import ru.senya.pixateka.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView list = findViewById(R.id.list);

        List<Item> items = new ArrayList<Item>();

        items.add(new Item(R.drawable.example1));
        items.add(new Item(R.drawable.example2));
        items.add(new Item(R.drawable.example3));

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new MyAdapter(getApplicationContext(), items));
    }
}