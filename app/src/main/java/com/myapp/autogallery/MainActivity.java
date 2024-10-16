package com.myapp.autogallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.myapp.autogallery.adapter.SliderAdapter;
import com.myapp.autogallery.fragments.FragmentSlider;
import com.myapp.autogallery.fragments.LowerBar;
import com.myapp.autogallery.fragments.UpperBar;
import com.myapp.autogallery.items.ActivitySection;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static ViewPager2 viewPager;
    private UpperBar upperBar;
    private LowerBar lowerBar;

    public static List<ActivitySection> activitiesSection;


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
        activitiesSection = new ArrayList<>();
        activitiesSection.add(new ActivitySection(1, R.drawable.chiron,
                getString(R.string.hyperCarTitle), getString(R.string.hyperCarText),
                ActivitySection.BIG));

        activitiesSection.add(new ActivitySection(2, R.drawable.bmwe30,
                getString(R.string.rareCarTitle), getString(R.string.rareCarText), ActivitySection.SMALL));
        activitiesSection.add(new ActivitySection(this, 3, R.drawable.dodgechallenger,
                R.string.muscleCarTitle, R.string.muscleCarText, ActivitySection.MEDIUM));
        activitiesSection.add(new ActivitySection(this, 3, R.drawable.dodgechallenger,
                R.string.muscleCarTitle, R.string.muscleCarText, ActivitySection.MEDIUM));


        fragments.add(FragmentSlider.newInstance(activitiesSection));

        FragmentStateAdapter fragment = new SliderAdapter(this, fragments);

        viewPager = findViewById(R.id.sliderPager);
        viewPager.setAdapter(fragment);
        viewPager.setCurrentItem(UpperBar.Tabs.ACTIVITIES.getNumber(), false);
        viewPager.registerOnPageChangeCallback(registerPageSelect());
    }


    public ViewPager2.OnPageChangeCallback registerPageSelect() {
        return new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                UpperBar.Tabs selectTab = (position == UpperBar.Tabs.ACTIVITIES.getNumber())
                        ? UpperBar.Tabs.ACTIVITIES
                        : UpperBar.Tabs.DISCOVER;

                upperBar.selectTab(selectTab);
            }
        };
    }

}