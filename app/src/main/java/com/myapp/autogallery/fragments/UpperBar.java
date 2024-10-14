package com.myapp.autogallery.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import com.myapp.autogallery.MainActivity;
import com.myapp.autogallery.R;


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
    private ConstraintLayout tabsLayout;

    public static Tabs status = Tabs.ACTIVITIES;
    boolean moved = false;

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
        tabsLayout = view.findViewById(R.id.tabs);
        scrollBar = view.findViewById(R.id.scrollBar);
        scrollBarParams = (ConstraintLayout.LayoutParams) scrollBar.getLayoutParams();


        if (status.equals(Tabs.DISCOVER))
            updateTabStats(Tabs.DISCOVER, R.id.discover);
        else
            updateTabStats(Tabs.ACTIVITIES, R.id.activities);

        scrollBar.setLayoutParams(scrollBarParams);

        textDiscover.setOnClickListener(this::onClick);
        textActivities.setOnClickListener(this::onClick);

        return view;
    }

    public void selectTab(Tabs tab) {
        ConstraintSet constraintSet = new ConstraintSet();
        status = tab;
        int tabId = (tab == Tabs.ACTIVITIES) ? R.id.activities : R.id.discover;
        int pageNumber = tab.number;

        updateTabStats(status, tabId);
        if (MainActivity.viewPager != null)
            MainActivity.viewPager.setCurrentItem(pageNumber);

        constraintSet.clone(tabsLayout);
        constraintSet.connect(R.id.scrollBar, ConstraintSet.LEFT, tabId, ConstraintSet.RIGHT, 0);
        constraintSet.connect(R.id.scrollBar, ConstraintSet.LEFT, tabId, ConstraintSet.LEFT, 0);


        AutoTransition transition = new AutoTransition();
        transition.setDuration(200);

        TransitionManager.beginDelayedTransition(tabsLayout, transition);
        constraintSet.applyTo(tabsLayout);

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
        selectTab((view.getId() == R.id.discover) ? Tabs.DISCOVER: Tabs.ACTIVITIES);
    }
}
