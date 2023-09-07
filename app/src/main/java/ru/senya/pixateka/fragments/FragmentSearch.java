package ru.senya.pixateka.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.adapters.NewRecyclerAdapter;
import ru.senya.pixateka.adapters.RecyclerAdapterSearch;
import ru.senya.pixateka.database.room.ItemEntity;
import ru.senya.pixateka.databinding.FragmentSearchBinding;
import ru.senya.pixateka.models.ImageEntity;
import ru.senya.pixateka.models.Page;


public class FragmentSearch extends Fragment {

    private FragmentSearchBinding binding;
    private String fullQuery;
    private final List<ImageEntity> data = new LinkedList<>();
    private int page = 0, totalPages = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(LayoutInflater.from(getContext()), container, false);
        setupClickListeners();
        setupRecycler();

        return binding.getRoot();
    }

    private void setupClickListeners(){
        binding.button.setOnClickListener(view -> {
            page = 0;
            getData(page, true);
        });
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fullQuery = query;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fullQuery = newText;
                return false;
            }
        });
    }

    private void setupRecycler(){
        binding.list.setAdapter(new NewRecyclerAdapter(data, getActivity()));
        binding.list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                assert layoutManager != null;
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int[] firstVisibleItemPositions = layoutManager.findFirstVisibleItemPositions(null);
                int firstVisibleItemPosition = firstVisibleItemPositions[0];


                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                    getData(page, false);
                }
            }
        });
    }

    private void getData(int numPage, boolean clear){
        if (numPage < totalPages){
            page++;
            String token = "Token " + App.getSharedPreferences().getString("jwt_key", "");
            App.getItemService().search(token, fullQuery, numPage).enqueue(new Callback<Page<ImageEntity>>() {
                @Override
                public void onResponse(@NonNull Call<Page<ImageEntity>> call, @NonNull Response<Page<ImageEntity>> response) {
                    if (response.isSuccessful()){
                        assert response.body() != null;
                        if (clear){
                            data.clear();
                        }
                        data.addAll(Arrays.asList(response.body().getContent()));
                        binding.list.getAdapter().notifyDataSetChanged();
                        totalPages = response.body().getTotalPages();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Page<ImageEntity>> call, @NonNull Throwable t) {

                }
            });
        }

    }

}

