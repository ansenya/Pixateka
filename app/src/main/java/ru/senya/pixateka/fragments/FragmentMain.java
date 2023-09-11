package ru.senya.pixateka.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.activities.AddActivity;
import ru.senya.pixateka.activities.LoginActivity;
import ru.senya.pixateka.adapters.RecyclerAdapter;
import ru.senya.pixateka.models.Page;
import ru.senya.pixateka.databinding.FragmentMainBinding;
import ru.senya.pixateka.models.ImageEntity;

public class FragmentMain extends BaseFragment<FragmentMainBinding> {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
    }

    @Override
    public void getData(int numPage, boolean clear) {
        if (numPage < totalPages) {
            page++;
            App.getItemService().getAll("Bearer " + jwtToken, numPage).enqueue(new Callback<Page<ImageEntity>>() {
                @Override
                public void onResponse(@NonNull Call<Page<ImageEntity>> call, @NonNull Response<Page<ImageEntity>> response) {
                    Log.e("MyTag", String.valueOf(response.code()));
                    if (response.isSuccessful()) {
                        Page<ImageEntity> page = response.body();
                        assert page != null;
                        if (clear) {
                            data.clear();
                        }
                        data.addAll(Arrays.asList(page.getContent()));
                        Objects.requireNonNull(binding.recycler.getAdapter()).notifyItemRangeChanged(data.size() - page.getContent().length, page.getContent().length);
                        totalPages = page.getTotalPages();
                        Log.e("MyTag", numPage + " " + totalPages);
                        binding.swipeContainer.setRefreshing(false);
                    } else if (response.code() == 401) {
                        binding.swipeContainer.setRefreshing(false);
                        startActivity(new Intent(getActivity(), LoginActivity.class).putExtra("auth", ""));
                        getActivity().finish();
                    } else {
                        binding.swipeContainer.setRefreshing(false);
                        Snackbar.make(binding.getRoot(), getString(R.string.error_server_unavailable), Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Page<ImageEntity>> call, @NonNull Throwable t) {
                    Log.e("MyTag", "err", t);
                    binding.swipeContainer.setRefreshing(false);
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
        binding.fab.setOnClickListener(view -> startActivity(new Intent(getContext(), AddActivity.class)));

        binding.swipeContainer.setOnRefreshListener(() -> {
            page = 0;
            getData(page, true);
        });
    }

}
