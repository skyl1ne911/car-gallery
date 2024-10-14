package com.myapp.autogallery.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.autogallery.R;
import com.myapp.autogallery.adapter.ActivityAdapter;
import com.myapp.autogallery.items.ActivitySection;

import java.util.ArrayList;
import java.util.List;

public class FragmentSlider extends Fragment {

    public int pageNumber;
    private List<ActivitySection> activitiesSection = new ArrayList<>();

    public static FragmentSlider newInstance(List<ActivitySection> section) {
        FragmentSlider slider = new FragmentSlider();
        Bundle args = new Bundle();

        for (ActivitySection card : section) {
            args.putInt("id", card.getId());
            args.putInt("imageId", card.getImageId());
            args.putString("title", card.getTitle());
            args.putString("text", card.getText());
        }

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

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            activitiesSection.add(new ActivitySection(
                    bundle.getInt("id"),
                    bundle.getInt("imageId"),
                    bundle.getString("title"),
                    bundle.getString("text")
            ));

            ActivityAdapter adapter = new ActivityAdapter(getContext(), activitiesSection);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

}
