package com.myapp.autogallery.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.myapp.autogallery.R;

public class FragmentSlider extends Fragment {

    private int pageNumber;

    public static FragmentSlider newInstance(int num) {
        FragmentSlider slider = new FragmentSlider();
        Bundle args = new Bundle();
        args.putInt("num", num);
        slider.setArguments(args);
        return slider;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slider_upperbar, container, false);
        ImageView image = view.findViewById(R.id.slider);

        if(getArguments() != null)
            image.setImageResource(getArguments().getInt("num"));



        return view;
    }


}
