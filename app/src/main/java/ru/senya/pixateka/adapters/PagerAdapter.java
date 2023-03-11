package ru.senya.pixateka.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import ru.senya.pixateka.R;
import ru.senya.pixateka.fragments.FragmentFullscreen;

public class PagerAdapter extends FragmentStatePagerAdapter {

    final Fragment[] fragments = new Fragment[2];

    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        //fragments[0] = new FragmentMain();
        fragments[1] = new FragmentFullscreen();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "Хуй" : "Пизда";
    }
}
