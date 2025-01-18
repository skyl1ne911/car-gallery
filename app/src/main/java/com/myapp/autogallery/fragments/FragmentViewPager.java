package com.myapp.autogallery.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.myapp.autogallery.R;
import com.myapp.autogallery.adapter.FragmentStateViewPager;
import com.myapp.autogallery.interfaces.ScrollCallback;
import com.myapp.autogallery.items.ActivitySection;
import com.myapp.autogallery.viewmodels.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentViewPager extends Fragment {
    private ScrollCallback scrollCallback;
    private SharedViewModel sharedViewModel;

    public static ViewPager2 viewPager;
    private FragmentStateViewPager adapter;
    private List<Fragment> fragments = new ArrayList<>();
    private List<ActivitySection> sections;

    public static FragmentViewPager newInstance(List<ActivitySection> data) {
        FragmentViewPager fragment = new FragmentViewPager();
        Bundle args = new Bundle();
        args.putParcelableArrayList("sections", new ArrayList<>(data));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ScrollCallback)
            scrollCallback = (ScrollCallback) context;
        else
            throw new RuntimeException(context + " must implement scrollCallback");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sections = getArguments().getParcelableArrayList("sections");
            fragments.add(FragmentMainPage.newInstance(sections));
            fragments.add(FragmentMainPage.newInstance(sections));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_pager_fragment, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        sharedViewModel.getScreenClicked().observe(getViewLifecycleOwner(), clicked -> {
            viewPager.setUserInputEnabled(clicked);
        });

        adapter = new FragmentStateViewPager(this, fragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(UpperBar.Tabs.ACTIVITIES.getPosition(), false);
        viewPager.registerOnPageChangeCallback(registerPageSelect());
        viewPager.setOffscreenPageLimit(2);
//        viewPager.post(() -> viewPager.setCurrentItem(UpperBar.Tabs.ACTIVITIES.getNumber(), false));


        Log.d("FragmentViewPager.log", "onCreateView");
        return view;
    }

    public ViewPager2.OnPageChangeCallback registerPageSelect() {
        return new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                UpperBar.Tabs tabPos;
                if (position == UpperBar.Tabs.ACTIVITIES.getPosition())
                    tabPos = UpperBar.Tabs.ACTIVITIES;
                else
                    tabPos = UpperBar.Tabs.DISCOVER;

                scrollCallback.selectedPage(tabPos);
                Log.d("selected page", String.valueOf(tabPos));
            }
        };
    }
}
