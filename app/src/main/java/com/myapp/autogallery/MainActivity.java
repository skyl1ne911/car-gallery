package com.myapp.autogallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.myapp.autogallery.adapter.ActivityAdapter;
import com.myapp.autogallery.adapter.SliderAdapter;
import com.myapp.autogallery.effects.BlurBuilder;
import com.myapp.autogallery.fragments.FragmentSlider;
import com.myapp.autogallery.fragments.LowerBar;
import com.myapp.autogallery.fragments.UpperBar;
import com.myapp.autogallery.items.ActivitySection;

import java.util.ArrayList;
import java.util.List;

import eightbitlab.com.blurview.BlurView;


public class MainActivity extends AppCompatActivity {
    private int ID = 1;

    public static ViewPager2 viewPager;
    private UpperBar upperBar;
    private LowerBar lowerBar;

    public static List<ActivitySection> activitiesSection;
    public static List<ActivitySection> discoverySection;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.backgroundBar));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.navigationBar));

        if (savedInstanceState == null) {
            upperBar = new UpperBar();
            lowerBar = new LowerBar();
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentUpperBar, upperBar).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentLowerBar, lowerBar).commit();
        }

        List<Fragment> fragments = new ArrayList<>();
        activitiesSection = setActivitiesData();



        fragments.add(FragmentSlider.newInstance(activitiesSection));
        fragments.add(FragmentSlider.newInstance(activitiesSection));
        FragmentStateAdapter fragment = new SliderAdapter(this, fragments);

        viewPager = findViewById(R.id.sliderPager);
        viewPager.setAdapter(fragment);
//        viewPager.post(() -> viewPager.setCurrentItem(UpperBar.Tabs.ACTIVITIES.getNumber(), false));
        viewPager.setCurrentItem(UpperBar.Tabs.ACTIVITIES.getNumber(), false);
        viewPager.registerOnPageChangeCallback(registerPageSelect());
        viewPager.setOffscreenPageLimit(2);

//        BlurView blurView = findViewById(R.id.blurView);
//        ConstraintLayout constraintLayout = findViewById(R.id.main);
//        blurView.setupWith(viewPager).setBlurRadius(5f);

    }

    public ViewPager2.OnPageChangeCallback registerPageSelect() {
        return new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                UpperBar.Tabs selectTab;
                int tabId;
                if (position == UpperBar.Tabs.ACTIVITIES.getNumber()) {
                    selectTab = UpperBar.Tabs.ACTIVITIES;
                    tabId = R.id.activities;
                }
                else {
                    selectTab = UpperBar.Tabs.DISCOVER;
                    tabId = R.id.discover;
                }
                upperBar.selectTab(selectTab, tabId);
            }
        };
    }

    private List<ActivitySection> setActivitiesData() {
        LayerDrawable cardTextGradients = (LayerDrawable) AppCompatResources.getDrawable(this, R.drawable.card_text_gradient);
        List<ActivitySection> activitiesData = new ArrayList<>();

        activitiesData.add(new ActivitySection(0, R.drawable.chiron, R.drawable.icon_speedlimiter,
                getString(R.string.hyperCarTitle), getString(R.string.hyperCarText), ActivitySection.BIG));
        activitiesData.add(addData(1, R.drawable.bmwe30, R.string.rareCarTitle,
                R.string.rareCarText, ActivitySection.SMALL));
        activitiesData.add(addData(2, R.drawable.dodgechallenger, R.string.muscleCarTitle,
                R.string.muscleCarText, ActivitySection.MEDIUM));
        activitiesData.add(addData(3, R.drawable.fordraptor, R.string.largeCarTitle,
                R.string.largeCarText,
                R.layout.card_medium_2));
        activitiesData.add(addData(4, R.drawable.regera, R.string.beautifulCarTitle, R.string.beautifulCarText,
                ActivitySection.SMALL));

        activitiesData.get(0).setColorText(cardTextGradients.getDrawable(0));
        activitiesData.get(1).setColorText(cardTextGradients.getDrawable(1));
        activitiesData.get(2).setColorText(cardTextGradients.getDrawable(2));
        activitiesData.get(3).setColorText(cardTextGradients.getDrawable(3));
        activitiesData.get(4).setColorText(cardTextGradients.getDrawable(4));

        return activitiesData;
    }

    public List<ActivitySection> setDiscoveryData() {
        List<ActivitySection> discoveryData = new ArrayList<>();

//        discoveryData.add(addData(0, ));


        return discoveryData;
    }


    public ActivitySection addData(int id, int imageResource, int titleResource, int textResource, int size) {
        String title = getString(titleResource);
        String text = getString(textResource);
        return new ActivitySection(id, imageResource, title, text, size);
    }
    public ActivitySection addData(int imageResource, int titleResource, int textResource, int size, boolean big) {
        String title = getString(titleResource);
        String text = getString(textResource);
        int newId = ID++;
        return new ActivitySection(newId, imageResource, title, text, size, big);
    }

}