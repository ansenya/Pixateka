package ru.senya.pixateka.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static ru.senya.pixateka.database.retrofit.Utils.BASE_URL;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
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
import ru.senya.pixateka.databinding.FragmentMainBinding;
import ru.senya.pixateka.database.room.ItemEntity;

public class FragmentMain extends Fragment {

    public FragmentMainBinding binding;
    RecyclerView list;
    List<ItemEntity> data = new ArrayList<>();
    RecyclerAdapterMain adapter;
    Toolbar toolbar;
    Retrofit retrofit;
    ItemInterface service;


    public FragmentMain(List<ItemEntity> items, Context context) {
        this.data = items;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> {
            back();
        });

        toolbar = binding.mainToolbar;
        upToolbar();
        binding.swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        return binding.getRoot();
    }


    private void upToolbar() {

    }

    public void back() {
        binding.fragment.goUp();
        binding.fragment.setVisibility(GONE);
        binding.mainToolbar.setVisibility(VISIBLE);
        binding.toolbar.setVisibility(GONE);
        binding.mainRecyclerView.setVisibility(VISIBLE);
        binding.fab.setVisibility(VISIBLE);
        binding.swipeContainer.setVisibility(VISIBLE);
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

    public void refresh() {
        binding.swipeContainer.post(() -> {
            binding.swipeContainer.setRefreshing(true);
            onRefreshListener.onRefresh();
        });
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

                    FirebaseStorage.getInstance().getReference().child("images").listAll().
                            addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                @Override
                                public void onSuccess(ListResult listResult) {

                                    if (listResult.getItems().size() == 0) {
                                        new Thread(() -> {
                                            App.getDatabase().itemDAO().delete();
                                            data.clear();
                                        }).start();

                                        binding.swipeContainer.setRefreshing(false);
                                    } else {
                                        for (StorageReference storageReference : listResult.getItems()) {
                                            storageReference.getDownloadUrl().
                                                    addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            storageReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                                                @Override
                                                                public void onSuccess(StorageMetadata storageMetadata) {
                                                                    binding.swipeContainer.setRefreshing(false);
                                                                    boolean contains = false;

                                                                    ItemEntity item = new ItemEntity(1,
                                                                            storageMetadata.getCustomMetadata("userUid"),
                                                                            uri.toString(),
                                                                            storageMetadata.getCustomMetadata("name"),
                                                                            storageMetadata.getCustomMetadata("descriprtion"),
                                                                            storageMetadata.getCustomMetadata("userEmail"),
                                                                            "");

                                                                    for (ItemEntity itemEntity : arrayList) {
                                                                        if (itemEntity.getPath().equals(item.getPath())) {
                                                                            contains = true;
                                                                        }
                                                                    }
                                                                    if (!contains) {
                                                                        new Thread(() -> {
                                                                            App.getDatabase().itemDAO().save(item);
                                                                        }).start();
                                                                        data.add(item);
                                                                        getActivity().runOnUiThread(() -> {
                                                                            adapter.notifyItemChanged(data.size() - 1);
                                                                        });
                                                                    }
                                                                    binding.swipeContainer.setRefreshing(false);
                                                                }
                                                            });
                                                        }
                                                    }).
                                                    addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getContext(), "unsuccessful refresh, try again", Toast.LENGTH_SHORT).show();
                                                            binding.swipeContainer.setRefreshing(false);
                                                            Log.e("MyTag", e.toString());
                                                        }
                                                    });
                                        }
                                    }

                                }

                            }).
                            addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "unsuccessful refresh, try again", Toast.LENGTH_SHORT).show();
                                    binding.swipeContainer.setRefreshing(false);
                                    Log.e("MyTag", e.toString());
                                }
                            });
                });

                Call<ArrayList<Item>> call = service.getAllPhotos();

                call.enqueue(new Callback<ArrayList<Item>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                        if (response.body() != null && response.body().size() > 0) {

                            new Thread(() -> {

                                ArrayList<Item> items = response.body();
                                for (Item item : items) {

                                    boolean contains = false;

                                    for (ItemEntity itemEntity : data) {
                                        if (item.id==itemEntity.id){
                                            contains = true;
                                            break;
                                        }
                                    }

                                    if (!contains){
                                        ItemEntity entity = new ItemEntity(item.id, item.author, item.image, item.name, item.description, item.author, item.tags);
                                        data.add(entity);
                                        getActivity().runOnUiThread(()->{
                                            myNotify(data.size() - 1);
                                        });
                                        App.getDatabase().itemDAO().save(entity);
                                    }
//
//                                    if (!contains) {
//                                        ItemEntity entity = new ItemEntity(item.id, item.author, item.image, item.name, item.description, item.author, item.tags);
//                                        data.add(entity);
//                                        getActivity().runOnUiThread(()->{
//                                            myNotify(data.size() - 1);
//                                        });
//                                        App.getDatabase().itemDAO().save(entity);
//                                    }

                                }
                                getActivity().runOnUiThread(() -> {
                                    binding.swipeContainer.setRefreshing(false);
                                });
                            }).start();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Item>> call, Throwable t) {
                        binding.swipeContainer.setRefreshing(false);
                        Toast.makeText(getContext(), "smth bad happened", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getContext(), "you don't have internet connection", Toast.LENGTH_SHORT).show();
                binding.swipeContainer.setRefreshing(false);
            }
        }
    };
}