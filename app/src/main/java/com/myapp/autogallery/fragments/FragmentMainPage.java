package com.myapp.autogallery.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.autogallery.R;
import com.myapp.autogallery.adapter.ActivityAdapter;
import com.myapp.autogallery.items.ActivitySection;
import com.myapp.collageview.CollageImageView;

import java.util.ArrayList;
import java.util.List;


public class FragmentMainPage extends Fragment {
    private List<ActivitySection> sections = new ArrayList<>();
    private ActivityAdapter adapter;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slider_recyclerview, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerSectionActivities);
        LayerDrawable cardTextGradients = (LayerDrawable) AppCompatResources.getDrawable(getContext(), R.drawable.card_text_gradient);

        adapter = new ActivityAdapter(getContext(), sections, recyclerView);
        recyclerView.setAdapter(adapter);

        ConstraintLayout cl = view.findViewById(R.id.recyclerViewContainer);
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        TextView text1 = new TextView(getContext());
        text1.setText("Test text");
        cl.addView(text1);
        lp.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        text1.setLayoutParams(lp);
        Bitmap bit = BitmapFactory.decodeResource(getResources(), R.drawable.porsche911gt3_2);


        recyclerView.post(() -> {
            RecyclerView.ViewHolder rv =
                    recyclerView.findViewHolderForAdapterPosition(0);
            if (rv != null) {
                View item = rv.itemView;
                ConstraintLayout cs = item.findViewById(R.id.innerContainer);
                CardView.LayoutParams csLp = (CardView.LayoutParams) cs.getLayoutParams();
                CollageImageView collageImageView = new CollageImageView(getContext(), bit);
                collageImageView.setLineSize(6);
                collageImageView.setLineColor(R.color.white);
                collageImageView.setRotateDegrees(15);
                collageImageView.setLineOrientation(CollageImageView.VERTICAL);
                collageImageView.setResourceSide(CollageImageView.LEFT);
                collageImageView.setLayoutParams(csLp);
                cs.addView(collageImageView);

            } else Log.d(getClass().getName(), "rv is null");
        });


//        ActivitySection sect = new ActivitySection.CardBuilder(getContext(), 1)
//                .setText(R.string.muscleCarText)
//                .build();
//        adapter.updateItem(0, sect);







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

    public void setGradientText(TextView textView, Drawable layerDrawable) {
        GradientDrawable gradient = (GradientDrawable) layerDrawable;
        Shader shader =  new LinearGradient(textView.getWidth() >> 1, 0, textView.getWidth(), 0,
                gradient.getColors(), null, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(shader);
        textView.invalidate();
    }
}
