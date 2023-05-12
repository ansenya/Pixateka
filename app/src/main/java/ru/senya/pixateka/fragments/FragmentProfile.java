package ru.senya.pixateka.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.senya.pixateka.App;
import ru.senya.pixateka.adapters.RecyclerAdapterMain;
import ru.senya.pixateka.adapters.RecyclerAdapterProfile;
import ru.senya.pixateka.database.retrofit.itemApi.Item;
import ru.senya.pixateka.database.retrofit.userApi.User;
import ru.senya.pixateka.database.room.ItemEntity;
import ru.senya.pixateka.databinding.NewFragmentProfileBinding;


public class FragmentProfile extends Fragment {

    NewFragmentProfileBinding binding;
    ArrayList<ItemEntity> data;
    User mainUser;


    public FragmentProfile(ArrayList<ItemEntity> data, User mainUser) {
        this.data = data;
        this.mainUser = mainUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = NewFragmentProfileBinding.inflate(inflater, container, false);
        initRecycler();
        binding.buttonEditProfile.setOnClickListener(v -> {
            getChildFragmentManager().
                    beginTransaction().
                    replace(binding.fragmentEdit.getId(), new FragmentEditProfile()).commit();

            binding.fragmentEdit.setVisibility(VISIBLE);
            binding.relativeLayout.setVisibility(GONE);
        });
        if (mainUser.avatar != null) {
            Glide.with(getContext()).load(App.getMainUser().avatar).into(binding.pfpImg);
        }

        binding.name.setText(mainUser.username);
        binding.name.setText(mainUser.first_name+" "+mainUser.last_name);


        binding.swipeContainer.setOnRefreshListener(onRefreshListener);
        binding.swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        return binding.getRoot();
    }


    private void initRecycler() {
        binding.recyclerList.setAdapter(new RecyclerAdapterProfile(data, getContext(), binding.fragment, null, getActivity(), binding.fragment, binding.nestedScrollView));
        binding.recyclerList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    public boolean visible() {
        if (binding.fragment.getVisibility() == VISIBLE) {
            return true;
        }
        return false;
    }

    public boolean isEditVisible() {
        if (binding.fragmentEdit.getVisibility() == VISIBLE) {
            return true;
        }
        return false;
    }

    public void myNotify() {
        binding.recyclerList.getAdapter().notifyDataSetChanged();
    }

    public void myNotify(int i) {
        binding.recyclerList.getAdapter().notifyItemChanged(i);
    }

    public void back() {
        if (binding.fragmentEdit.getVisibility() == VISIBLE) {
            binding.fragmentEdit.setVisibility(GONE);
            binding.relativeLayout.setVisibility(VISIBLE);
        } else {
            binding.fragment.goUp();
            binding.fragment.setVisibility(GONE);
            binding.relative.setVisibility(VISIBLE);
            binding.nestedScrollView.setVisibility(VISIBLE);
        }

    }

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
                    Call<ArrayList<Item>> call = App.getItemService().getPhotosByUserId(App.getMainUser().id);
                    call.enqueue(new Callback<ArrayList<Item>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                            if (response.body() != null && response.body().size() > 0) {
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
                                            data.add(entity);
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
                                    }
                                    getActivity().runOnUiThread(() -> {
                                        binding.recyclerList.getAdapter().notifyDataSetChanged();
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


                }).start();
            } else {
                Toast.makeText(getContext(), "you don't have internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
