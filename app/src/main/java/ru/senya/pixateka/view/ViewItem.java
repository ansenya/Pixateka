package ru.senya.pixateka.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import ru.senya.pixateka.databinding.ViewItemBinding;

public class ViewItem extends RelativeLayout {

    ViewItemBinding binding;

    public ViewItem(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        binding = ViewItemBinding.inflate(LayoutInflater.from(getContext()), this, true);
    }
}
