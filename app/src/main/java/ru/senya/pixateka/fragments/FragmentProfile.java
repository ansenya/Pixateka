package ru.senya.pixateka.fragments;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.senya.pixateka.R;
import ru.senya.pixateka.adapters.RecyclerTouchListener;
import ru.senya.pixateka.adapters.RecyclerViewAdapter;
import ru.senya.pixateka.databinding.FragmentProfileBinding;
import ru.senya.pixateka.subjects.Item;

public class FragmentProfile extends Fragment {

    FragmentProfileBinding binding;
    private int start = 0;
    int a, b, rand;
    int[] examples = new int[149];
    String[] examplesTXT = new String[149];
    List<Item> items = new ArrayList<>();
    RecyclerViewAdapter adapter;
    final Random random = new Random();

    public FragmentProfile(List<Item> items) {
        this.items = items;
        adapter = new RecyclerViewAdapter(this.items);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        initRecycler();
        if (start == 0){
            a = examples[random.nextInt(148)];
            b = examples[random.nextInt(148)];
        }
        start = 1;
        binding.back.setImageResource(a);
        binding.pfpImg.setImageResource(b);
        if (items.size() == 0) binding.nothing.setVisibility(VISIBLE);

        return binding.getRoot();
    }

    private void initRecycler() {
        initPull();
        binding.profileRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.profileRecyclerView.setAdapter(adapter);
        listener();
    }
    private void listener() {
        binding.profileRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), binding.profileRecyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        binding.fragment.setVisibility(VISIBLE);
                        binding.relativeLayout.setVisibility(GONE);
                        if (items.get(position).getPic() == 0){
                            binding.fragment.update(items.get(position).getUri(), items.get(position).getName());
                        } else binding.fragment.update(items.get(position).getPic(), items.get(position).getName());
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }

                }));
        binding.edit.setOnClickListener(view -> {
            initImages();
            binding.profileRecyclerView.getAdapter().notifyItemChanged(items.size() - 1);
            binding.nothing.setVisibility(GONE);
        });
    }
    private void initImages() {
        rand = random.nextInt(148);
        items.add(new Item(examples[rand], examplesTXT[rand]));
    }
    private void initPull() {
        for (int i = 0; i < 148; i++) {
            examples[i] = R.drawable.a1 + i;
            examplesTXT[i] = 1 + i + "";
        }
    }
    public boolean visible() {
        if (binding.fragment.getVisibility() == VISIBLE && start == 1) return true;
        return false;
    }
    public void back() {
        binding.fragment.goUp();
        binding.fragment.setVisibility(GONE);
        binding.relativeLayout.setVisibility(VISIBLE);
    }
}
