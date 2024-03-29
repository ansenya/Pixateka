package ru.senya.pixateka.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
import ru.senya.pixateka.activities.SubSampActivity;
import ru.senya.pixateka.adapters.RecyclerAdapter;
import ru.senya.pixateka.models.ImageEntity;
import ru.senya.pixateka.models.Page;
import ru.senya.pixateka.models.UserEntity;
import ru.senya.pixateka.databinding.FragmentProfileBinding;


public class FragmentProfile extends BaseFragment<FragmentProfileBinding> {

    UserEntity userEntity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        initPfpAndBack();
        page = 0;
        getData(page, true);
    }

    @Override
    public void getData(int numPage, boolean clear) {
        if (numPage < totalPages) {
            page++;
            App.getItemService().getAllByUserId("Bearer " + jwtToken, numPage, userId).enqueue(new Callback<Page<ImageEntity>>() {
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
                        Objects.requireNonNull(binding.recycler.getAdapter()).notifyItemRangeChanged(data.size() - page.getContent().length, page.getContent().length - 1);
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
                    Snackbar.make(binding.getRoot(), getString(R.string.error_server_unavailable), Snackbar.LENGTH_LONG).show();
                    binding.swipeContainer.setRefreshing(false);
                }
            });
        }
    }

    @Override
    public void initRecyclerView() {
        binding.recycler.setAdapter(new RecyclerAdapter(data, getActivity()));
        binding.recycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    public void initBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
    }

    @Override
    public void initCLickListeners() {
        binding.fab.setOnClickListener(view -> startActivity(new Intent(getContext(), AddActivity.class)));
        binding.editProfileButton.setOnClickListener(view -> {

        });

        binding.swipeContainer.setOnRefreshListener(() -> {
            initPfpAndBack();
            getData(page, true);
            binding.swipeContainer.setRefreshing(false);
        });

        binding.profileImage.setOnClickListener(view -> {
            Bundle extras = new Bundle();

            extras.putString("path", userEntity.getPfp());

            Intent intent = new Intent(getActivity(), SubSampActivity.class);
            intent.putExtras(extras);

            startActivity(intent);
        });

        binding.background.setOnClickListener(view -> {
            Bundle extras = new Bundle();

            extras.putString("path", userEntity.getBack());

            Intent intent = new Intent(getActivity(), SubSampActivity.class);
            intent.putExtras(extras);

            startActivity(intent);
        });
    }

    public void initPfpAndBack() {
        App.getUserService().getUser(auth, userId).enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(@NonNull Call<UserEntity> call, @NonNull Response<UserEntity> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null)
                        return;

                    Animation pulseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.wave_animation);
                    binding.background.startAnimation(pulseAnimation);
                    binding.profileImage.startAnimation(pulseAnimation);

                    userEntity = response.body();

                    binding.profileName.setText(userEntity.getName());

                    binding.profileBio.setText(userEntity.getBio());

                    Glide.with(binding.profileImage)
                            .load(userEntity.getPfp())
                            .placeholder(R.drawable.pfp)
                            .addListener(getListener(binding.profileImage))
                            .into(binding.profileImage);

                    if (userEntity.getBack() != null) {
                        Glide.with(binding.background)
                                .load(userEntity.getBack())
                                .placeholder(R.drawable.holder_gradient)
                                .addListener(getListener(binding.background))
                                .into(binding.background);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserEntity> call, @NonNull Throwable t) {

            }
        });
    }


    public RequestListener<Drawable> getListener(View view) {
        RequestListener<Drawable> listener = new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                view.clearAnimation();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                view.clearAnimation();
                return false;
            }
        };
        return listener;
    }
}
