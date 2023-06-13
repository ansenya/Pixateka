package ru.senya.pixateka.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.activities.StartActivity;
import ru.senya.pixateka.activities.Visible;
import ru.senya.pixateka.adapters.RecyclerAdapterProfile;
import ru.senya.pixateka.database.retrofit.itemApi.Item;
import ru.senya.pixateka.database.retrofit.userApi.User;
import ru.senya.pixateka.database.room.ItemEntity;
import ru.senya.pixateka.databinding.NewFragmentProfileBinding;
import ru.senya.pixateka.view.viewFullscreen;


public class FragmentProfile extends Fragment {

    public NewFragmentProfileBinding binding;
    ArrayList<ItemEntity> data;
    User mainUser;
    viewFullscreen vfs;
    Visible visible = new Visible(false);
    androidx.appcompat.widget.Toolbar toolbar;
    int k;
    boolean running = false;

    public FragmentProfile(ArrayList<ItemEntity> data, User mainUser, viewFullscreen vfs, androidx.appcompat.widget.Toolbar toolbar, int k) {
        this.data = data;
        this.mainUser = mainUser;
        this.vfs = vfs;
        this.toolbar = toolbar;
        this.k = k;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = NewFragmentProfileBinding.inflate(inflater, container, false);
        initRecycler();
        binding.buttonEditProfile.setOnClickListener(v -> {
            binding.toolbar.setVisibility(GONE);
            getChildFragmentManager().
                    beginTransaction().
                    replace(binding.fragmentEdit.getId(), new FragmentEditProfile()).commit();

            binding.fragmentEdit.setVisibility(VISIBLE);
            binding.relativeLayout.setVisibility(GONE);
        });
        if (mainUser.avatar != null) {
            Glide.with(getContext()).load(mainUser.avatar).into(binding.pfpImg);
        }
        if (mainUser.background != null) {
            Glide.with(getContext()).load(mainUser.background).into(binding.back);
        }

        binding.name.setText(mainUser.username);
        try {
            if (mainUser.about != null && !mainUser.about.split("\"")[1].isEmpty()) {
                String about = "";
                for (String s : mainUser.about.split("\"")) {
                    for (int i = 0; i < s.split("\\\\n").length; i++) {
                        if (i != 0) about += " \n ";
                        about += s.split("\\\\n")[i];
                    }

                }
                binding.about.setText(about); // backend problem :)
            }
        } catch (NullPointerException e) {
            binding.about.setText("Описание не заполнено");
        }


        if (k == 1) {
            binding.buttonLogout.setVisibility(GONE);
            binding.buttonEditProfile.setVisibility(GONE);
            binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
            binding.toolbar.setNavigationOnClickListener(v -> {
                getActivity().finish();
            });
        }

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> {
            back();
        });
        binding.swipeContainer.setOnRefreshListener(onRefreshListener);
        binding.swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        binding.buttonLogout.setOnClickListener(v -> {
            App.getUserService().logout(App.getMainUser().token, "csrftoken=" + App.getMainUser().token + "; " + "sessionid=" + App.getMainUser().sessionId).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
            new Thread(() -> {
                App.getDatabase().userDAO().deleteUserTable();
                App.getDatabase().itemDAO().delete();
            }).start();
            startActivity(new Intent(getActivity(), StartActivity.class));
            binding = null;
            getActivity().finish();
        });

        return binding.getRoot();
    }


    private void initRecycler() {
        binding.recyclerList.setAdapter(new RecyclerAdapterProfile(data, getContext(), vfs, toolbar, getActivity(), binding.nestedScrollView, binding, visible, onRefreshListener));
        binding.recyclerList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.recyclerList.getAdapter().notifyDataSetChanged();
        binding.recyclerList.getAdapter().notifyDataSetChanged();
    }

    public boolean visible() {
        if (vfs.getVisibility() == VISIBLE) {
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
        if (visible.getVisible()) {
            vfs.setVisibility(VISIBLE);
            toolbar.setVisibility(VISIBLE);
        }
    }

    public void myNotify(int i) {
        binding.recyclerList.getAdapter().notifyItemChanged(i);
    }

    public void back() {
        if (binding.fragmentEdit.getVisibility() == VISIBLE) {
            binding.fragmentEdit.setVisibility(GONE);
            binding.relativeLayout.setVisibility(VISIBLE);
            binding.toolbar.setVisibility(VISIBLE);
            onRefreshListener.onRefresh();
        } else {
            if (vfs.pop()) {
                toolbar.setVisibility(GONE);
                vfs.setVisibility(GONE);
                visible.setVisible(false);
                binding.relative.setVisibility(VISIBLE);
                binding.nestedScrollView.setVisibility(VISIBLE);
            }
        }

    }

    public SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            boolean connected = connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected() && connectivityManager.getActiveNetworkInfo().isAvailable();
            if (connected) {
                new Thread(() -> {
                    String cookie = "csrftoken=" + App.getMainUser().token + "; " + "sessionid=" + App.getMainUser().sessionId;
                    App.getUserService().getUser(mainUser.id, App.getMainUser().token, cookie).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful() && !running) {
                                running = true;
                                new Thread(() -> {
                                    User user = response.body();
                                    user.setSessionId(App.getMainUser().sessionId);
                                    user.setToken(App.getMainUser().token);

                                    App.getDatabase().userDAO().deleteUserTable();
                                    App.getDatabase().userDAO().save(user);
                                    App.setMainUser(user);
                                    mainUser = user;
                                    getActivity().runOnUiThread(() -> {
                                        binding.name.setText(mainUser.username);
                                        if (mainUser.about != null && !mainUser.about.split("\"")[1].isEmpty()) {
                                            String about = "";
                                            for (String s : mainUser.about.split("\"")) {
                                                for (int i = 0; i < s.split("\\\\n").length; i++) {
                                                    if (i != 0) about += " \n ";
                                                    about += s.split("\\\\n")[i];
                                                }

                                            }
                                            binding.about.setText(about); // backend problem :)


                                        }
                                        Glide.with(getContext()).load(user.avatar).into(binding.pfpImg);
                                        if (mainUser.background != null) {
                                            Glide.with(getContext()).load(mainUser.background).into(binding.back);
                                        }
                                        running = false;
                                    });
                                }).start();
                            } else {
                                try {
                                    Log.e("RefreshP", response.errorBody().string());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Snackbar.make(binding.getRoot(), "Не получилось достучаться до сервера", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }).start();
                new Thread(() -> {
                    ArrayList<ItemEntity> arrayList = new ArrayList<>();
                    arrayList.addAll(App.getDatabase().itemDAO().getAll());
                    Call<ArrayList<Item>> call = App.getItemService().getPhotosByUserId(mainUser.id);
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
                                            entity.setWidth(item.width);
                                            entity.setHeight(item.height);
                                            entity.setColor(item.color);
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
                                    }
                                    getActivity().runOnUiThread(() -> {
                                        binding.recyclerList.getAdapter().notifyDataSetChanged();
                                        binding.swipeContainer.setRefreshing(false);
                                    });
                                }).start();
                            }
                            if (response.body() != null && response.body().size() == 0) {
                                binding.swipeContainer.setRefreshing(false);
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Item>> call, Throwable t) {
                            binding.swipeContainer.setRefreshing(false);
                            Snackbar.make(binding.getRoot(), "Не получилось достучаться до сервера", Snackbar.LENGTH_SHORT).show();
                        }
                    });


                }).start();
            } else {
                binding.swipeContainer.setRefreshing(false);
                Snackbar.make(binding.getRoot(), "Нет доступа в интернет", Snackbar.LENGTH_SHORT).show();
            }
        }
    };
}
