package com.myapp.autogallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.myapp.autogallery.adapter.ActivityAdapter;
import com.myapp.autogallery.adapter.SliderAdapter;
import com.myapp.autogallery.fragments.FragmentSlider;
import com.myapp.autogallery.fragments.LowerBar;
import com.myapp.autogallery.fragments.UpperBar;
import com.myapp.autogallery.items.ActivitySection;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int ID = 1;

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

        LayerDrawable cardTextGradients = (LayerDrawable) AppCompatResources.getDrawable(this, R.drawable.card_text_gradient);
        List<Fragment> fragments = new ArrayList<>();
        activitiesSection = new ArrayList<>();

        activitiesSection.add(new ActivitySection(0, R.drawable.chiron, R.drawable.icon_speedlimiter,
                getString(R.string.hyperCarTitle), getString(R.string.hyperCarText), ActivitySection.BIG));
        addData(1, R.drawable.bmwe30, R.string.rareCarTitle,
                R.string.rareCarText, ActivitySection.SMALL);
        addData(2, R.drawable.dodgechallenger, R.string.muscleCarTitle,
                R.string.muscleCarText, ActivitySection.MEDIUM);
        addData(3, R.drawable.fordraptor, R.string.largeCarTitle, R.string.largeCarText,
                R.layout.card_medium_2);
        addData(4, R.drawable.regera, R.string.beautifulCarTitle, R.string.beautifulCarText,
                ActivitySection.SMALL);

        activitiesSection.get(0).setColorText(cardTextGradients.getDrawable(0));
        activitiesSection.get(1).setColorText(cardTextGradients.getDrawable(1));
        activitiesSection.get(2).setColorText(cardTextGradients.getDrawable(2));
        activitiesSection.get(3).setColorText(cardTextGradients.getDrawable(3));
        activitiesSection.get(4).setColorText(cardTextGradients.getDrawable(4));

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

    public ActivitySection appendData(int id, int imageResource, int titleResource, int textResource, int size) {
        String title = getString(titleResource);
        String text = getString(textResource);
        return new ActivitySection(id, imageResource, title, text, size);
    }
    public void addData(int id, int imageResource, int titleResource, int textResource, int size) {
        String title = getString(titleResource);
        String text = getString(textResource);
        activitiesSection.add(new ActivitySection(id, imageResource, title, text, size));
    }
    public ActivitySection appendData(int imageResource, int titleResource, int textResource, int size, boolean big) {
        String title = getString(titleResource);
        String text = getString(textResource);
        int newId = ID++;
        return new ActivitySection(newId, imageResource, title, text, size, big);
    }

}