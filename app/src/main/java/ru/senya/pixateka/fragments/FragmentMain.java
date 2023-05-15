package ru.senya.pixateka.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static ru.senya.pixateka.database.retrofit.Utils.BASE_URL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.activities.AddActivity;
import ru.senya.pixateka.adapters.RecyclerAdapterMain;
import ru.senya.pixateka.database.retrofit.itemApi.Item;
import ru.senya.pixateka.database.retrofit.itemApi.ItemInterface;
import ru.senya.pixateka.database.room.ItemEntity;
import ru.senya.pixateka.databinding.FragmentMainBinding;

public class FragmentMain extends Fragment {

    public FragmentMainBinding binding;
    RecyclerView list;
    List<ItemEntity> data = new LinkedList<>();
    RecyclerAdapterMain adapter;
    Retrofit retrofit;
    ItemInterface service;
    Boolean in_order = true;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    public FragmentMain(List<ItemEntity> items, Context context) {
        this.data = items;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = preferences.edit();
        if (!preferences.contains("order")) {
            editor.putBoolean("order", true);
            editor.commit();
        } else {
            in_order = preferences.getBoolean("order", true);
        }
        binding = FragmentMainBinding.inflate(LayoutInflater.from(getContext()), container, false);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(ItemInterface.class);
        onRefreshListener.onRefresh();
        list = binding.mainRecyclerView;
        adapter = new RecyclerAdapterMain(getActivity(),
                data,
                getContext(),
                onClickListener,
                binding.fragment,
                binding.mainRecyclerView,
                binding.mainToolbar,
                binding.fab,
                binding.toolbar,
                binding.swipeContainer);
        initRecycler();


        binding.toolbar.setTitle("Photo");
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white, requireContext().getTheme()));
        binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        binding.mainToolbar.inflateMenu(R.menu.switch_menu);
        binding.mainToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.in_order:
                    in_order = true;
                    data.clear();
                    onRefreshListener.onRefresh();
                    editor.putBoolean("order", true);
                    editor.commit();
                    return true;
                case R.id.random:
                    in_order = false;
                    data.clear();
                    onRefreshListener.onRefresh();
                    editor.putBoolean("order", false);
                    editor.commit();
                    return true;
            }
            return false;
        });
        binding.toolbar.setNavigationOnClickListener(v -> {
            back();
        });
        upToolbar();
        binding.swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        return binding.getRoot();
    }


    private void upToolbar() {

    }

    public void fullUpdate() {
        binding.fragment.fullUpdate();
        binding.fragment.setVisibility(GONE);
        binding.mainToolbar.setVisibility(VISIBLE);
        binding.toolbar.setVisibility(GONE);
        binding.mainRecyclerView.setVisibility(VISIBLE);
        binding.fab.setVisibility(VISIBLE);
        binding.swipeContainer.setVisibility(VISIBLE);
    }

    public void back() {
        if (binding.fragment.pop()) {
            binding.fragment.setVisibility(GONE);
            binding.mainToolbar.setVisibility(VISIBLE);
            binding.toolbar.setVisibility(GONE);
            binding.mainRecyclerView.setVisibility(VISIBLE);
            binding.fab.setVisibility(VISIBLE);
            binding.swipeContainer.setVisibility(VISIBLE);
        }
    }

    private void initRecycler() {
        list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        list.setAdapter(adapter);
        listener();
    }

    private void listener() {
        binding.fab.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), AddActivity.class));
        });
        binding.swipeContainer.setOnRefreshListener(onRefreshListener);
    }

    public void myNotify() {
        adapter.notifyDataSetChanged();
    }

    public void myNotify(int i) {
        adapter.notifyItemChanged(i);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            binding.fragment.setVisibility(VISIBLE);
            binding.mainRecyclerView.setVisibility(GONE);
            binding.mainToolbar.setVisibility(View.INVISIBLE);
            binding.toolbar.setVisibility(VISIBLE);
            binding.fab.setVisibility(GONE);
        }
    };

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            boolean connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);

            if (connected) {
                new Thread(() -> {
                    ArrayList<ItemEntity> arrayList = new ArrayList<>();
                    arrayList.addAll(App.getDatabase().itemDAO().getAll());
                    Call<ArrayList<Item>> call = service.getAllPhotos();
                    call.enqueue(new Callback<ArrayList<Item>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                            if (response.body() != null && response.body().size() > 0) {
                                if (!in_order) Collections.shuffle(data);
                                new Thread(() -> {
                                    ArrayList<Item> items = response.body();
                                    for (Item item : items) {
                                        boolean contains = false;
                                        for (ItemEntity itemEntity : data) {
                                            if (item.id == itemEntity.id) {
                                                contains = true;
                                                break;
                                            }
                                        }
                                        if (!contains) {
                                            ItemEntity entity = new ItemEntity(item.id, item.author, item.image, item.name, item.description, item.author, item.tags);
                                            entity.setColor(item.color);
                                            entity.setTags(item.tags);
                                            entity.setHeight(item.height);
                                            entity.setWidth(item.width);
                                            data.add(0, entity);
                                            getActivity().runOnUiThread(() -> {
                                                myNotify(data.size() - 1);
                                            });

                                            try {
                                                App.getDatabase().itemDAO().save(entity);
                                            } catch (SQLiteConstraintException e) {

                                            }

                                        }
                                    }
                                    ArrayList<ItemEntity> delete = new ArrayList<ItemEntity>();
                                    for (ItemEntity itemEntity : data) {
                                        boolean deleted = true;
                                        for (Item item : items) {
                                            if (itemEntity.id == item.id) {
                                                deleted = false;
                                            }
                                        }
                                        if (deleted) {
                                            new Thread(() -> {
                                                App.getDatabase().itemDAO().deleteByUserId(itemEntity.id);
                                            }).start();
                                            delete.add(itemEntity);
                                        }
                                    }
                                    for (ItemEntity itemEntity : delete) {
                                        data.remove(itemEntity);
                                        getActivity().runOnUiThread(() -> {
                                            binding.mainRecyclerView.getAdapter().notifyItemChanged(data.size());
                                        });
                                    }
                                    getActivity().runOnUiThread(() -> {
                                        binding.mainRecyclerView.getAdapter().notifyDataSetChanged();
                                        binding.swipeContainer.setRefreshing(false);
                                    });
                                }).start();
                                getActivity().runOnUiThread(() -> {
                                    binding.mainRecyclerView.getAdapter().notifyDataSetChanged();
                                });
                            } else if (response.body() != null && response.body().size() == 0) {
                                Toast.makeText(getContext(), "db is empty", Toast.LENGTH_SHORT).show();
                                binding.swipeContainer.setRefreshing(false);
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Item>> call, Throwable t) {
                            binding.swipeContainer.setRefreshing(false);
                            Toast.makeText(getContext(), "smth bad happened", Toast.LENGTH_SHORT).show();
                        }
                    });


                }).start();
            } else {
                Toast.makeText(getContext(), "you don't have internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    };
}