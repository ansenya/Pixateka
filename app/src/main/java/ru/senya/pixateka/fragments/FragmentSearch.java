package ru.senya.pixateka.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.senya.pixateka.App;
import ru.senya.pixateka.adapters.RecyclerAdapter;
import ru.senya.pixateka.databinding.FragmentSearchBinding;
import ru.senya.pixateka.models.ImageEntity;
import ru.senya.pixateka.models.Page;


public class FragmentSearch extends BaseFragment<FragmentSearchBinding> {

    private String fullQuery;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void initBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
    }

    @Override
    public void getData(int numPage, boolean clear) {
        if (numPage < totalPages) {
            page++;
            String token = "Bearer " + App.getSharedPreferences().getString("jwt_key", "");
            App.getItemService().search(token, fullQuery, numPage).enqueue(new Callback<Page<ImageEntity>>() {
                @Override
                public void onResponse(@NonNull Call<Page<ImageEntity>> call, @NonNull Response<Page<ImageEntity>> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (clear) {
                            data.clear();
                        }
                        data.addAll(Arrays.asList(response.body().getContent()));
                        binding.recycler.getAdapter().notifyDataSetChanged();
                        totalPages = response.body().getTotalPages();
                        if (totalPages < 2) {
                            totalPages = 1;
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Page<ImageEntity>> call, @NonNull Throwable t) {

                }
            });
        }

    }

    @Override
    public void initRecyclerView() {
        binding.recycler.setAdapter(new RecyclerAdapter(data, getActivity()));
        binding.recycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    @Override
    public void initCLickListeners() {
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
}

