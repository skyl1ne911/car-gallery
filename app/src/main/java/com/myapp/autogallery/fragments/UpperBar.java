package com.myapp.autogallery.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.myapp.autogallery.R;


public class UpperBar extends Fragment {
    public enum Tabs {
        DISCOVER(0, R.id.discover),
        ACTIVITIES(1, R.id.activities);

        private final int position, id;

        Tabs(int pos, int id) {
            this.position = pos;
            this.id = id;
        }

        public int getPosition() { return position; }
        public int getId() { return id; }
    }

    private TextView textDiscover, textActivities;
    private ImageView scrollBar;
    private ConstraintLayout.LayoutParams scrollBarParams;
    private ConstraintLayout tabsLayout;

    public Tabs status;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            String enumString = savedInstanceState.getString("tagName");
            status = Tabs.valueOf(enumString);
        }
        else {
            status = Tabs.ACTIVITIES;
        }
        Log.d("UpperBar_status", status.name());
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upper_bar, container, false);

        textDiscover = view.findViewById(R.id.discover);
        textActivities = view.findViewById(R.id.activities);
        tabsLayout = view.findViewById(R.id.tabs);
        scrollBar = view.findViewById(R.id.scrollBar);
        scrollBarParams = (ConstraintLayout.LayoutParams) scrollBar.getLayoutParams();

        ImageView searchIcon = view.findViewById(R.id.search);
        TextView inputField = view.findViewById(R.id.inputField);

        textDiscover.setOnClickListener(this::onClick);
        textActivities.setOnClickListener(this::onClick);
//        searchIcon.setOnClickListener(this::onClickSearchButton);
//        inputField.setOnClickListener(this::onClickSearchButton);

        updateTab(status);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tagName", status.name());
        Log.d("UpperBar.onSaveInstanceState", status.name());
    }

    public void selectTab(int position) {
        if (FragmentViewPager.viewPager != null)
            FragmentViewPager.viewPager.setCurrentItem(position);

        AutoTransition transition = new AutoTransition();
        transition.setDuration(200);
        TransitionManager.beginDelayedTransition(tabsLayout, transition);
    }

    public void updateTab(Tabs tab) {
        float inactiveAlpha = 0.5F, activeAlpha = 1;

        textDiscover.setAlpha(tab.id == Tabs.DISCOVER.id ? activeAlpha : inactiveAlpha);
        textActivities.setAlpha(tab.id == Tabs.ACTIVITIES.id ? activeAlpha : inactiveAlpha);

        scrollBarParams.rightToRight = tab.id;
        scrollBarParams.leftToLeft = tab.id;
        scrollBar.setLayoutParams(scrollBarParams);

        if (tab != status) status = tab;
        selectTab(tab.position);
    }

    public void onClick(View view) {
        if (view.getId() == Tabs.DISCOVER.id) status = Tabs.DISCOVER;
        else status = Tabs.ACTIVITIES;
        updateTab(status);
    }

    public void onClickSearchButton(View view) {
        // TODO
    }
}
