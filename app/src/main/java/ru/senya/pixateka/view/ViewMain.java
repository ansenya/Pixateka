package ru.senya.pixateka.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.senya.pixateka.R;
import ru.senya.pixateka.adapters.RecyclerTouchListener;
import ru.senya.pixateka.adapters.RecyclerViewAdapter;
import ru.senya.pixateka.databinding.ViewMainBinding;
import ru.senya.pixateka.subjects.Item;

public class ViewMain extends RelativeLayout {
    ViewMainBinding binding;
    List<Item> items = new ArrayList<Item>();
    int[] examples = new int[149];
    String[] examplesTXT = new String[149];
    RecyclerView list;
    RecyclerViewAdapter adapter = new RecyclerViewAdapter(items);
    final Random random = new Random();


    public ViewMain(Context context, AttributeSet attrs) {
        super(context, attrs);
        binding = ViewMainBinding.inflate(LayoutInflater.from(getContext()), this, true);
        list = binding.list;
        initPull();
        initRecycler();
        initImages();
    }

    private void initRecycler() {
        list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        list.setAdapter(adapter);
        listener();
    }

    private void initImages() {
        int rand;
        for (int i = 0; i < 148; i++) {
            rand = random.nextInt(148);
            items.add(new Item(examples[i], examplesTXT[i]));
        }
    }

    private void initPull() {
        for (int i = 0; i < 148; i++) {
            examples[i] = R.drawable.a1 + i;
            examplesTXT[i] = 1 + i + "";
        }
    }

    private void listener() {
        binding.list.addOnItemTouchListener(new RecyclerTouchListener(getContext(), binding.list,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        binding.fragment.setVisibility(VISIBLE);
                        binding.fragment.update(items.get(position).getPic(), items.get(position).getName());
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
        binding.refreshButton.setOnClickListener(view ->{
            binding.fragment.setVisibility(GONE);
        });
    }

    public boolean getMyVisibility(){
        if (binding.fragment.getVisibility() == VISIBLE) return true;
        else return false;
    }

    public void back(){
        binding.fragment.setVisibility(INVISIBLE);
        binding.fragment.goUp();
        binding.fragment.setVisibility(GONE);
    }
}
