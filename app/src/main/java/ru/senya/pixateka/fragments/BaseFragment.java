package ru.senya.pixateka.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

import ru.senya.pixateka.App;
import ru.senya.pixateka.interfaces.FragmentInterface;
import ru.senya.pixateka.models.ImageEntity;

public abstract class BaseFragment<T extends ViewBinding> extends Fragment implements FragmentInterface {

    int page = 0, totalPages = 1;
    final List<ImageEntity> data = new LinkedList<>();
    protected T binding;
    protected String jwtToken, auth, userId;

    Gson gson = new Gson();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        jwtToken = App.getSharedPreferences().getString("jwt_key", "");
        userId = App.getSharedPreferences().getString("user_id", "");
        auth = "Bearer " + jwtToken;

        initBinding(inflater, container);

        initRecyclerView();
        initCLickListeners();
        getData(page, false);


        return binding.getRoot();
    }
}
