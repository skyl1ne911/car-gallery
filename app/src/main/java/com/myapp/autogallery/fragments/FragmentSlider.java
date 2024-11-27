package com.myapp.autogallery.fragments;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.autogallery.R;
import com.myapp.autogallery.adapter.ActivityAdapter;
import com.myapp.autogallery.items.ActivitySection;

import java.util.ArrayList;
import java.util.List;


public class FragmentSlider extends Fragment {
    private List<ActivitySection> sections = new ArrayList<>();
    private ActivityAdapter adapter;

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
        if (getArguments() != null)
            sections = getArguments().getParcelableArrayList("sections");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slider_upperbar, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerSectionActivities);
        LayerDrawable cardTextGradients = (LayerDrawable) AppCompatResources.getDrawable(getContext(), R.drawable.card_text_gradient);

        adapter = new ActivityAdapter(getContext(), sections, recyclerView);
        recyclerView.setAdapter(adapter);

        view.post(() -> {
            TextView title1 = adapter.getTextViewAtPosition(0, ActivityAdapter.TITLE);
            TextView title2 = adapter.getTextViewAtPosition(1, ActivityAdapter.TITLE);
            TextView title3 = adapter.getTextViewAtPosition(2, ActivityAdapter.TITLE);
            TextView title4 = adapter.getTextViewAtPosition(3, ActivityAdapter.TITLE);
            TextView title5 = adapter.getTextViewAtPosition(4, ActivityAdapter.TITLE);
            assert cardTextGradients != null;
            setGradientText(title1, cardTextGradients.getDrawable(0));
            setGradientText(title2, cardTextGradients.getDrawable(1));
            setGradientText(title3, cardTextGradients.getDrawable(2));
            setGradientText(title4, cardTextGradients.getDrawable(3));
            setGradientText(title5, cardTextGradients.getDrawable(4));
        });

        return view;
    }

    public void setGradientText(TextView textView, Drawable layerDrawable) {
        GradientDrawable gradient = (GradientDrawable) layerDrawable;
        Shader shader =  new LinearGradient(textView.getWidth() / 2, 0, textView.getWidth(), 0,
                gradient.getColors(), null, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(shader);
        textView.invalidate();
    }
}
