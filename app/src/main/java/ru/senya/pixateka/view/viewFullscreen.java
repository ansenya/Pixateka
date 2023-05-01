package ru.senya.pixateka.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;

import ru.senya.pixateka.App;
import ru.senya.pixateka.R;
import ru.senya.pixateka.databinding.ViewFullscreenBinding;
import ru.senya.pixateka.room.ItemEntity;

public class viewFullscreen extends NestedScrollView {

    ViewFullscreenBinding binding;
    Context context;



    public viewFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        binding = ViewFullscreenBinding.inflate(LayoutInflater.from(getContext()), this, true);
//        binding.toolbar.inflateMenu(R.menu.menu);
//        binding.arrowBack.setOnClickListener(v -> {
//
//        });
    }


    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        //binding.getRoot().setVisibility(GONE);
    }





    @Override
    public boolean fullScroll(int direction) {
        return super.fullScroll(direction);
    }


    public void update(ItemEntity item){
        Glide.
                with(context).
                load(item.getPath()).
                into(binding.pic);
        binding.text.setText(item.getName());
    }
    public void goUp(){
        scrollTo(0, 0);
    }

}
