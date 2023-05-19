package ru.senya.pixateka.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ru.senya.pixateka.R;
import ru.senya.pixateka.adapters.RecyclerAdapterMain;
import ru.senya.pixateka.adapters.RecyclerAdapterSearch;
import ru.senya.pixateka.adapters.RecyclerAdapterSecondary;
import ru.senya.pixateka.adapters.RecyclerTouchListener;
import ru.senya.pixateka.databinding.FragmentSearchBinding;
import ru.senya.pixateka.database.room.ItemEntity;


public class FragmentSearch extends Fragment {

    FragmentSearchBinding binding;
    ArrayList<ItemEntity> items;
    LinkedList<ItemEntity> itemsSearch = new LinkedList<>();

    public FragmentSearch(ArrayList<ItemEntity> items) {
        this.items = items;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(LayoutInflater.from(getContext()), container, false);
        binding.list.setAdapter(new RecyclerAdapterSearch(itemsSearch, binding.fragment, getActivity(), binding.container, binding.toolbar));
        binding.list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white, requireContext().getTheme()));
        binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        initListeners();
        return binding.getRoot();
    }

    public boolean visible() {
        if (binding.fragment.getVisibility() == VISIBLE) return true;
        return false;
    }

    private void initListeners() {
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemsSearch.clear();
                if (newText.trim().toLowerCase().length() == 0) {
                    binding.list.getAdapter().notifyDataSetChanged();
                    binding.nothingWasFound.setVisibility(VISIBLE);
                    return true;
                }
                for (ItemEntity item : items) {
                    String s = item.name + item.description + item.tags;
                    s = s.toLowerCase();
                    s = s.trim();
                    if (s.contains(newText.toLowerCase().trim())) {
                        itemsSearch.add(item);
                        binding.nothingWasFound.setVisibility(View.INVISIBLE);
                    }
                    binding.list.getAdapter().notifyDataSetChanged();
                    if (itemsSearch.size() == 0) {
                        binding.list.getAdapter().notifyDataSetChanged();
                        binding.nothingWasFound.setVisibility(VISIBLE);
                    }
                }
                return false;
            }
        });

        binding.toolbar.setNavigationOnClickListener(v -> {
            back();
        });

    }

    public void back() {
        binding.fragment.goUp();
        binding.fragment.setVisibility(GONE);
        binding.container.setVisibility(VISIBLE);
        binding.toolbar.setVisibility(GONE);
    }
}

