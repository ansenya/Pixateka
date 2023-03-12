package ru.senya.pixateka.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.senya.pixateka.R;
import ru.senya.pixateka.adapters.RecyclerViewAdapter;
import ru.senya.pixateka.subjects.Item;

public class ListItem extends RelativeLayout {
    //FragmentMainBinding binding;
    List<Item> items = new ArrayList<Item>();
    int[] examples = {R.drawable.example1, R.drawable.example2, R.drawable.example3};
    String[] examplesTXT = {"первая фотка", "вторая фотка", "третья фотка"};
    RecyclerViewAdapter adapter;
    final Random random = new Random();

    public ListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        //binding = FragmentMainBinding.inflate(LayoutInflater.from(context), this, true);
        initRecycler();
    }


    private void initRecycler() {
//        adapter = new RecyclerViewAdapter(items);
//        RecyclerView main = binding.main;
//        main.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        main.setAdapter(adapter);
//        initImages();
    }

    private void initImages() {
        items.clear();
        int rand;
        for (int i = 0; i < 100; i++) {
            rand = random.nextInt(3);
            items.add(new Item(examples[rand], examplesTXT[rand]));
        }
        adapter.notifyDataSetChanged();
    }
}
