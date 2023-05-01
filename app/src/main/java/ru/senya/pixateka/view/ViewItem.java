package ru.senya.pixateka.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import ru.senya.pixateka.R;
import ru.senya.pixateka.databinding.ViewItemBinding;


public class ViewItem extends RelativeLayout {

    ViewItemBinding viewItemBinding;

    public ViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    private void initView() {
        viewItemBinding = ViewItemBinding.inflate(LayoutInflater.from(getContext()),
                this, true);
    }


}
