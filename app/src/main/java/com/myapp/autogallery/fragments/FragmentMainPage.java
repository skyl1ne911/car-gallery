package com.myapp.autogallery.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.myapp.autogallery.R;
import com.myapp.autogallery.adapter.ActivityAdapter;
import com.myapp.autogallery.customViews.CustomGridLayoutManager;
import com.myapp.autogallery.items.ActivitySection;
import com.myapp.autogallery.viewmodels.SharedViewModel;
import com.myapp.collageview.CollageImageView;

import java.util.ArrayList;
import java.util.List;


public class FragmentMainPage extends Fragment {
    private List<ActivitySection> sections = new ArrayList<>();
    private ActivityAdapter adapter;

    private SharedViewModel sharedViewModel;

    public static FragmentMainPage newInstance(List<ActivitySection> section) {
        FragmentMainPage slider = new FragmentMainPage();
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slider_recyclerview, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerSectionActivities);
        LayerDrawable cardTextGradients = (LayerDrawable) AppCompatResources.getDrawable(getContext(), R.drawable.card_text_gradient);
        CustomGridLayoutManager layoutManager = new CustomGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getScreenClicked().observe(getViewLifecycleOwner(), layoutManager::setScrollEnable);

        adapter = new ActivityAdapter(getContext(), sections, recyclerView);
        recyclerView.setAdapter(adapter);

        recyclerView.setOnTouchListener(this::setOnTouchListener);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            RenderEffect renderEffect = RenderEffect.createBlurEffect(30, 30, Shader.TileMode.DECAL);
//            view.setRenderEffect(renderEffect);
//        }
//        TODO вылет при скролле:
//        view.post(() -> {
//            TextView title1 = adapter.getTextViewAtPosition(0, ActivityAdapter.TITLE);
//            TextView title2 = adapter.getTextViewAtPosition(1, ActivityAdapter.TITLE);
//            TextView title3 = adapter.getTextViewAtPosition(2, ActivityAdapter.TITLE);
//            TextView title4 = adapter.getTextViewAtPosition(3, ActivityAdapter.TITLE);
//            TextView title5 = adapter.getTextViewAtPosition(4, ActivityAdapter.TITLE);
//            assert cardTextGradients != null;
//            setGradientText(title1, cardTextGradients.getDrawable(0));
//            setGradientText(title2, cardTextGradients.getDrawable(1));
//            setGradientText(title3, cardTextGradients.getDrawable(2));
//            setGradientText(title4, cardTextGradients.getDrawable(3));
//            setGradientText(title5, cardTextGradients.getDrawable(4));
//        });

        return view;
    }

    private boolean setOnTouchListener(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            sharedViewModel.setScreenClick(true);
            Log.d(getClass().getName() + ".setOnTouchListener", "click");
        }
        return false;
    }

    public void setGradientText(TextView textView, Drawable layerDrawable) {
        GradientDrawable gradient = (GradientDrawable) layerDrawable;
        Shader shader =  new LinearGradient(textView.getWidth() >> 1, 0, textView.getWidth(), 0,
                gradient.getColors(), null, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(shader);
        textView.invalidate();
    }
}
