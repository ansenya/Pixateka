package ru.senya.pixateka.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.activities.AddActivity;
import ru.senya.pixateka.adapters.NewRecyclerAdapter;
import ru.senya.pixateka.models.Page;
import ru.senya.pixateka.databinding.FragmentMainBinding;
import ru.senya.pixateka.models.ImageEntity;

public class FragmentMain extends Fragment {

    FragmentMainBinding binding;
    LinkedList<ImageEntity> data = new LinkedList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String jwtToken;
    int page = 0;
    int totalPages = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);

        sharedPreferences = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        jwtToken = sharedPreferences.getString("jwt_key", "");

        getData(page, false);

        adjustRecycler();
        adjustClickListeners();

        return binding.getRoot();
    }

    private void getData(int numPage, boolean clear) {
        if (numPage < totalPages) {
            page++;
            App.getItemService().getAll("Bearer " + jwtToken, numPage).enqueue(new Callback<Page<ImageEntity>>() {
                @Override
                public void onResponse(@NonNull Call<Page<ImageEntity>> call, @NonNull Response<Page<ImageEntity>> response) {
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


    private void adjustRecycler() {
        binding.recycler.setAdapter(new NewRecyclerAdapter(data, getActivity()));
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

    private void adjustClickListeners() {
        binding.fab.setOnClickListener(view -> startActivity(new Intent(getContext(), AddActivity.class)));

        binding.swipeContainer.setOnRefreshListener(() -> {
            page = 0;
            getData(page, true);
        });
    }


}
