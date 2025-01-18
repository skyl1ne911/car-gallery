package com.myapp.autogallery;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.myapp.autogallery.fragments.FragmentViewPager;
import com.myapp.autogallery.fragments.LowerBar;
import com.myapp.autogallery.fragments.UpperBar;
import com.myapp.autogallery.interfaces.ScrollCallback;
import com.myapp.autogallery.items.ActivitySection;
import com.myapp.autogallery.viewmodels.SharedViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ScrollCallback {
    private UpperBar upperBar;
    private LowerBar lowerBar;
    private FragmentViewPager fragmentPager;

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

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.backgroundBar));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.navigationBar));

        activitiesSection = setActivitiesData();

        if (savedInstanceState == null) {
            fragmentPager = FragmentViewPager.newInstance(activitiesSection);
            lowerBar = new LowerBar();
            upperBar = new UpperBar();
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentUpperBar, upperBar).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentViewPager, fragmentPager).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentLowerBar, lowerBar).commit();
        }
        else {
            upperBar = (UpperBar) getSupportFragmentManager().findFragmentById(R.id.fragmentUpperBar);
        }


        Log.d("activitiesSection", activitiesSection.getClass().toString());
        Log.d("MainActivity.log", "onCreate");
    }

    private List<ActivitySection> setActivitiesData() {
        List<ActivitySection> activitiesData = new ArrayList<>();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bmwm3e30);

        activitiesData.add(new ActivitySection.CardBuilder(this, 0)
                .setImage(R.drawable.bugattichiron)
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


//        activitiesData.get(0).bitmap = bitmap;

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
        catch (Exception ignored) { }
        return FileProvider.getUriForFile(this, "your.package.name.fileprovider", file);
    }

    @Override
    public void selectedPage(UpperBar.Tabs tab) {
        if (upperBar != null) upperBar.updateTab(tab);
    }

}