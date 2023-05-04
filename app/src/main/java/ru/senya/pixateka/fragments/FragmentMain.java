package ru.senya.pixateka.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
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
import java.util.Iterator;
import java.util.List;

import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.activities.AddActivity;
import ru.senya.pixateka.adapters.RecyclerViewAdapterRoom;
import ru.senya.pixateka.databinding.FragmentMainBinding;
import ru.senya.pixateka.room.ItemEntity;

public class FragmentMain extends Fragment {

    public FragmentMainBinding binding;
    RecyclerView list;
    List<ItemEntity> items = new ArrayList<>();
    RecyclerViewAdapterRoom adapter;
    Toolbar toolbar;


    public FragmentMain(List<ItemEntity> items, Context context) {
        this.items = items;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(LayoutInflater.from(getContext()), container, false);
        list = binding.mainRecyclerView;
        adapter = new RecyclerViewAdapterRoom(getActivity(),
                items,
                getContext(),
                onClickListener,
                binding.fragment,
                binding.mainRecyclerView,
                binding.mainToolbar,
                binding.fab,
                binding.toolbar);
        initRecycler();
        toolbar = binding.mainToolbar;
        upToolbar();
        binding.swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return binding.getRoot();
    }


    private void upToolbar() {
        toolbar.setTitleTextColor(getResources().getColor(R.color.text_color2));
        toolbar.setTitle(R.string.app_name);
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_color, getContext().getTheme()));
        toolbar.inflateMenu(R.menu.menu);
    }

    public void back() {
        binding.fragment.goUp();
        binding.fragment.setVisibility(GONE);
        binding.mainToolbar.setVisibility(VISIBLE);
        binding.toolbar.setVisibility(GONE);
        binding.mainRecyclerView.setVisibility(VISIBLE);
        binding.fab.setVisibility(VISIBLE);
    }

    private void initRecycler() {
        list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        list.setAdapter(adapter);
        listener();
    }

    private void listener() {
        binding.arrowBack.setOnClickListener(v -> {
            back();
        });
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
                                            items.clear();
                                        }).start();

                                        binding.swipeContainer.setRefreshing(false);
                                    }
                                    else {
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

                                                                    ItemEntity item = new ItemEntity(
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
                                                                        items.add(item);
                                                                        getActivity().runOnUiThread(() -> {
                                                                            adapter.notifyItemChanged(items.size() - 1);
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
                }).start();
            } else {
                Toast.makeText(getContext(), "you don't have internet connection", Toast.LENGTH_SHORT).show();
                binding.swipeContainer.setRefreshing(false);
            }
        }
    };

}
