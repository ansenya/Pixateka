package ru.senya.pixateka.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import ru.senya.pixateka.databinding.FragmentNotificationsBinding;

public class FragmentNotifications extends Fragment {

    FragmentNotificationsBinding binding;

    private ImageView imageView;
    private ScaleGestureDetector scaleGestureDetector;
    private float scale = 1f;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(LayoutInflater.from(getContext()), container, false);
        scaleGestureDetector = new ScaleGestureDetector(getActivity(), new ScaleListener());
        return binding.getRoot();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale *= detector.getScaleFactor();
            scale = Math.max(1.0f, Math.min(scale, 5.0f));

            imageView.setScaleX(scale);
            imageView.setScaleY(scale);
            return true;
        }
    }
}
