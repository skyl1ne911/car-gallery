package com.myapp.autogallery.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.myapp.autogallery.R;
import com.myapp.autogallery.fragments.FragmentSlider;

public class SliderAdapter extends FragmentStateAdapter {

    public int[] images = new int[2];

    public SliderAdapter(FragmentActivity fa) {
        super(fa);
        images[0] = R.drawable.a1dbaaf7babc03ac39e0968b12021a1c;
        images[1] = R.drawable.devushka_voennyj_ushki_872540_3840x2400;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return FragmentSlider.newInstance(images[position]);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }
}
