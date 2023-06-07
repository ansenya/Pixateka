package ru.senya.pixateka.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.LinkedList;

import ru.senya.pixateka.R;
import ru.senya.pixateka.adapters.RecyclerAdapterSearch;
import ru.senya.pixateka.database.room.ItemEntity;
import ru.senya.pixateka.databinding.FragmentSearchBinding;


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
        binding.nothingWasFound.setVisibility(VISIBLE);
        initListeners();
        return binding.getRoot();
    }

    public boolean visible() {
        if (binding.fragment.getVisibility() == VISIBLE) return true;
        return false;
    }

    private void initListeners() {
        binding.search.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onQueryTextChange(String newText) {
                itemsSearch.clear();
                if (newText.trim().toLowerCase().length() == 0) {
                    binding.list.getAdapter().notifyDataSetChanged();
                    binding.nothingWasFound.setVisibility(VISIBLE);
                    return true;
                }
                new Thread(() -> {
                    for (ItemEntity item : items) {
                        String s = item.name + item.description + item.tags;
                        s = s.toLowerCase();
                        s = s.trim();
                        if (s.contains(newText.toLowerCase().trim())) {
                            getActivity().runOnUiThread(() -> {
                                itemsSearch.add(item);
                                binding.nothingWasFound.setVisibility(View.INVISIBLE);
                            });
                        }
                        getActivity().runOnUiThread(() -> binding.list.getAdapter().notifyDataSetChanged());

                        if (itemsSearch.size() == 0) {
                            getActivity().runOnUiThread(() -> {
                                binding.list.getAdapter().notifyDataSetChanged();
                                binding.nothingWasFound.setVisibility(VISIBLE);
                            });

                        }
                    }
                }).start();

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

