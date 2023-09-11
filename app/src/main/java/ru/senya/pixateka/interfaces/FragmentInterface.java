package ru.senya.pixateka.interfaces;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface FragmentInterface {

    void initBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);
    void initCLickListeners();
    void initRecyclerView();
    void getData(int page, boolean clear);

}
