package com.myapp.autogallery.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.myapp.autogallery.R;
import com.myapp.autogallery.fragments.FragmentSlider;
import com.myapp.autogallery.items.ActivitySection;

import java.util.List;

public class SliderAdapter extends FragmentStateAdapter {

    private List<Fragment> fragments;

    public SliderAdapter(FragmentActivity fa, List<Fragment> fragments) {
        super(fa);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() { return fragments.size(); }

}
