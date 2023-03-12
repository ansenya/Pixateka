package ru.senya.pixateka.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;

import ru.senya.pixateka.R;
import ru.senya.pixateka.databinding.ViewFullscreenBinding;

public class viewFullscreen extends ScrollView  {

    ViewFullscreenBinding binding;



    public viewFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBottomEdgeEffectColor(getResources().getColor(R.color.blue));
        binding = ViewFullscreenBinding.inflate(LayoutInflater.from(getContext()), this, true);
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


    @Override
    public void setBottomEdgeEffectColor(int color) {
        super.setBottomEdgeEffectColor(color);
    }

    public void update(int imageResource, String text){
        binding.pic.setImageResource(imageResource);
        binding.text.setText(text);
        binding.getRoot().setVisibility(VISIBLE);
    }
    public void update(Uri uri, String text){
        binding.pic.setImageURI(uri);
        binding.text.setText(text);
        binding.getRoot().setVisibility(VISIBLE);
    }
    public void goUp(){
        fullScroll(View.FOCUS_UP);
    }

}
