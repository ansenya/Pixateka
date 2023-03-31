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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import ru.senya.pixateka.App;
import ru.senya.pixateka.adapters.RecyclerTouchListener;
import ru.senya.pixateka.adapters.RecyclerViewAdapter;
import ru.senya.pixateka.adapters.RecyclerViewAdapterRoom;
import ru.senya.pixateka.databinding.FragmentSearchBinding;
import ru.senya.pixateka.room.ItemEntity;
import ru.senya.pixateka.subjects.Item;

public class FragmentSearch extends Fragment {

    FragmentSearchBinding binding;

    List<ItemEntity> items = new ArrayList<>();
    List<ItemEntity> itemsSearch = new ArrayList<>();

    public FragmentSearch(List<ItemEntity> items) {
        this.items = items;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(LayoutInflater.from(getContext()), container, false);
        binding.button.setOnClickListener(view -> {
            itemsSearch.clear();
            String text = binding.search.getText().toString();
//            new Thread(() ->{
//                Log.e("MyTag3", text+"\n" +itemsSearch.toString());
//               itemsSearch.add(App.getDatabase().itemDAO().search(text));
//               binding.list.getAdapter().notifyDataSetChanged();
//            }).start();
            binding.list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            binding.list.setAdapter(new RecyclerViewAdapterRoom(itemsSearch));
            for (ItemEntity item : items) {
                if (item.getName().equals(text)) {
                    itemsSearch.add(item);
                    if (itemsSearch.size() > 1) {
                        binding.list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    } else
                        binding.list.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                }
                binding.list.getAdapter().notifyDataSetChanged();
            }
        });
        binding.list.addOnItemTouchListener(new RecyclerTouchListener(getContext(), binding.list,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        if (itemsSearch.get(position).getPic() == 0) {
                            binding.fragment.update(itemsSearch.get(position).getPath(), itemsSearch.get(position).getName());
                        } else
                            binding.fragment.update(itemsSearch.get(position).getPic(), itemsSearch.get(position).getName());
                        binding.fragment.setVisibility(View.VISIBLE);
                        binding.relativeLayout.setVisibility(GONE);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
        return binding.getRoot();
    }

    public boolean visible() {
        if (binding.fragment.getVisibility() == VISIBLE) return true;
        return false;
    }

    public void back() {
        binding.fragment.goUp();
        binding.fragment.setVisibility(GONE);
        binding.relativeLayout.setVisibility(VISIBLE);
    }


}
