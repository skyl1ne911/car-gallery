package com.myapp.autogallery;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
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

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


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
        LayerDrawable gradients = (LayerDrawable) AppCompatResources.getDrawable(this, R.drawable.card_text_gradient);
        List<ActivitySection> activitiesData = new ArrayList<>();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bmwm3e30);

        activitiesData.add(new ActivitySection.CardBuilder(this, 0)

                .setIcon(R.drawable.icon_speedlimiter)
                .setTitle(R.string.hyperCarTitle)
                .setText(R.string.hyperCarText)
                .build());
        activitiesData.add(new ActivitySection.CardBuilder(this, 1)
                .setImage(R.drawable.bmwm3e30)
                .setTitle(R.string.rareCarTitle)
                .setText(R.string.rareCarText)
                .setTemplate(R.layout.card_small, false)
                .build());
        activitiesData.add(new ActivitySection.CardBuilder(this, 2)
                .setImage(R.drawable.dodgechallenger)
                .setTitle(R.string.muscleCarTitle)
                .setText(R.string.muscleCarText)
                .setTemplate(R.layout.card_medium, false)
                .build());
        activitiesData.add(new ActivitySection.CardBuilder(this, 3)
                .setImage(R.drawable.fordf150)
                .setTitle(R.string.largeCarTitle)
                .setText(R.string.largeCarText)
                .setTemplate(R.layout.card_medium_2, false)
                .build());
        activitiesData.add(new ActivitySection.CardBuilder(this, 4)
                .setImage(R.drawable.regera)
                .setTitle(R.string.beautifulCarTitle)
                .setText(R.string.beautifulCarText)
                .setTemplate(R.layout.card_small, false)
                .build());


        activitiesData.get(0).bitmap = bitmap;


//        Uri uri = saveBitmap(bitmap);
//        activitiesData.get(0).uriImage = uri;

        return activitiesData;
    }

    public List<ActivitySection> setDiscoveryData() {
        List<ActivitySection> discoveryData = new ArrayList<>();
//        discoveryData.add(addData(0, ));
        return discoveryData;
    }

    public Uri saveBitmap(Bitmap bitmap) {
        File file = new File(getCacheDir(), "collage.jpg");
        try(FileOutputStream stream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
        }
        catch (Exception ex) { }

        return FileProvider.getUriForFile(this, "your.package.name.fileprovider", file);
    }
}