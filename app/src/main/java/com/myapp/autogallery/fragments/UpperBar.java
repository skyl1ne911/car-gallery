package com.myapp.autogallery.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.LevelListDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.myapp.autogallery.MainActivity;
import com.myapp.autogallery.R;
import com.myapp.autogallery.adapter.SliderAdapter;

public class UpperBar extends Fragment {

    public enum Tabs {
        DISCOVER(0),
        ACTIVITIES(1);

        private final int number;

        Tabs(int number) { this.number = number; }

        public int getNumber() { return number; }
    }

    private TextView textDiscover, textActivities;;
    private ImageView scrollBar;
    private ConstraintLayout.LayoutParams scrollBarParams;

    public static Tabs status = Tabs.ACTIVITIES;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upper_bar, container, false);

        textDiscover = view.findViewById(R.id.discover);
        textActivities = view.findViewById(R.id.activities);

        scrollBar = view.findViewById(R.id.scrollBar);
        scrollBarParams = (ConstraintLayout.LayoutParams) scrollBar.getLayoutParams();

        if (status.equals(Tabs.DISCOVER)) {
            scrollBarParams.leftToLeft = R.id.discover;
            scrollBarParams.rightToRight = R.id.discover;
        }
        else {
            scrollBarParams.leftToLeft = R.id.activities;
            scrollBarParams.rightToRight = R.id.activities;
        }
        scrollBar.setLayoutParams(scrollBarParams);

        textDiscover.setOnClickListener(this::onClick);
        textActivities.setOnClickListener(this::onClick);

        return view;
    }

    public void selectTab(Tabs tab) {
        status = tab;
        int tabId = (tab == Tabs.ACTIVITIES) ? R.id.activities : R.id.discover;
        int pageNumber = tab.number;

        updateTabStats(status, tabId);

        if (MainActivity.viewPager != null)
            MainActivity.viewPager.setCurrentItem(pageNumber);

    }

    public void updateTabStats(Tabs tab, int activityTabId) {
        float inactiveAlpha = 0.5F, activeAlpha = 1;
        
        textDiscover.setAlpha((tab.equals(Tabs.DISCOVER)) ? activeAlpha : inactiveAlpha);
        textActivities.setAlpha((tab.equals(Tabs.ACTIVITIES)) ? activeAlpha : inactiveAlpha);

        scrollBarParams.rightToRight = activityTabId;
        scrollBarParams.leftToLeft = activityTabId;

        scrollBar.setLayoutParams(scrollBarParams);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.discover) selectTab(Tabs.DISCOVER);
        else if (view.getId() == R.id.activities) selectTab(Tabs.ACTIVITIES);

    }
}
