package com.myapp.autogallery.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.autogallery.R;
import com.myapp.autogallery.viewmodels.SharedViewModel;

import java.text.Format;


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

    private SharedViewModel sharedViewModel;

    private TextView textDiscover, textActivities;
    private EditText inputField;
    private ImageView scrollBar;
    private ConstraintLayout.LayoutParams scrollBarParams;
    private ConstraintLayout tabsLayout;

    public Tabs status;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            String enumString = savedInstanceState.getString("statusName");
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
        ImageView searchIcon = view.findViewById(R.id.search);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);


        inputField = view.findViewById(R.id.inputField);
        textDiscover = view.findViewById(R.id.discover);
        textActivities = view.findViewById(R.id.activities);
        tabsLayout = view.findViewById(R.id.tabs);
        scrollBar = view.findViewById(R.id.scrollBar);
        scrollBarParams = (ConstraintLayout.LayoutParams) scrollBar.getLayoutParams();

        textDiscover.setOnClickListener(this::onClick);
        textActivities.setOnClickListener(this::onClick);
        inputField.setOnFocusChangeListener(this::onFocusInputField);
        searchIcon.setOnClickListener(this::onClickSearchButton);
        view.setOnClickListener(this::onClickView);

        sharedViewModel.getScreenClicked().observe(getViewLifecycleOwner(), clicked -> {
            if (clicked) disabledFocus();
        });

        updateTab(status);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("statusName", status.name());
        Log.d("UpperBar.onSaveInstanceState", status.name());
    }

    private void selectTab(int position) {
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
        inputField.requestFocus();
        if (getContext() != null) {
            InputMethodManager inputMethod =
                    (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethod.showSoftInput(inputField, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void onFocusInputField(View view, boolean focus) {
        boolean inputFieldFocus = inputField.hasFocus();
        textActivities.setClickable(!inputFieldFocus);
        textDiscover.setClickable(!inputFieldFocus);

//        sharedViewModel.getScreenClicked().observe(getViewLifecycleOwner(), clicked -> {
//            if (clicked) disabledFocus(view);
//        });

       sharedViewModel.setScreenClick(!inputFieldFocus);
       Log.d(getClass().getName(), ".onFocusInputField");
    }

    public void onClickView(View view) {
        disabledFocus();
    }

    protected void disabledFocus() {
        inputField.clearFocus();
        if (getContext() != null) {
            View currentFocus = getView();
            InputMethodManager inputMethod =
                    (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethod.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }
}
