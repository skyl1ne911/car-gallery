package com.myapp.autogallery.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.flexbox.AlignContent;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.myapp.autogallery.R;
import com.myapp.autogallery.adapter.ActivityAdapter;
import com.myapp.autogallery.items.ActivitySection;

import java.util.ArrayList;
import java.util.List;

public class FragmentSlider extends Fragment {

    public int pageNumber;
    private List<ActivitySection> sections = new ArrayList<>();

    public static FragmentSlider newInstance(List<ActivitySection> section) {
        FragmentSlider slider = new FragmentSlider();
        Bundle args = new Bundle();
        args.putParcelableArrayList("sections", new ArrayList<>(section));
        slider.setArguments(args);
        return slider;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments() != null ? getArguments().getInt("imageId") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slider_upperbar, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerSectionActivities);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        if (getArguments() != null) {
            sections = getArguments().getParcelableArrayList("sections");
            ActivityAdapter adapter = new ActivityAdapter(getContext(), sections);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

}
