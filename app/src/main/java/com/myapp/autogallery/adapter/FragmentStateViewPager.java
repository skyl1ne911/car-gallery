package com.myapp.autogallery.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

public class FragmentStateViewPager extends FragmentStateAdapter {
    private List<Fragment> fragments;;

    public FragmentStateViewPager(Fragment fa, List<Fragment> fragments) {
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
