package com.myapp.autogallery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Outline;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.myapp.autogallery.R;
import com.myapp.autogallery.effects.BlurBuilder;
import com.myapp.autogallery.items.ActivitySection;

import java.util.List;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderEffectBlur;
import eightbitlab.com.blurview.RenderScriptBlur;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.CardHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<ActivitySection> activitySections;

    public ActivityAdapter(Context context, List<ActivitySection> activitiesCard) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        activitySections = activitiesCard;
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View sectionItems = inflater.inflate(viewType, parent, false);
        return new CardHolder(sectionItems);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();

        if (activitySections.get(position).isBig()) layoutParams.setFullSpan(true);
        holder.bind(activitySections.get(position));
        holder.itemView.setLayoutParams(layoutParams);

    }

    public GradientDrawable applyGradientDrawable(GradientDrawable gradient, int position) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        boolean isLastPosition = position == getItemCount() - 1;
        boolean isSecondPosition = position == getItemCount() - 2;
        boolean isBigContent = activitySections.get(position).isBig();

        if (position == 0 || position == 1)
            return setGradientForFirstPositions(gradient, isBigContent, position);
        else
            gradientDrawable.setColor(gradient.getColors()[1]);

        if (isLastPosition) {
           gradientDrawable = setGradientForLastPositions(gradient);
        }
        else if (isSecondPosition && position % 2 == 1) {
            gradientDrawable = setGradientForLastPositions(gradient);
        }
        return gradientDrawable;
    }

    public GradientDrawable setGradientForLastPositions(GradientDrawable gradient) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setGradientRadius(500);
            gradientDrawable.setColors(new int[] {
                    gradient.getColors()[0],
                    gradient.getColors()[1]
            });
        return gradientDrawable;
    }
    public GradientDrawable setGradientForFirstPositions(GradientDrawable gradient, boolean isBig, int position) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColors(new int[] {
                gradient.getColors()[1],
                gradient.getColors()[2]
        });
        float fullGradient = 500;

        if (position == 0) {
            if (isBig) gradientDrawable.setGradientRadius(1300);
            else if (activitySections.get(position + 1).isBig()) gradientDrawable.setGradientRadius(fullGradient); // если 2 элемент большой
            else gradientDrawable.setGradientRadius(1500); // если 2 элемент маленький или средний
        }
        else {
            if (isBig || activitySections.get(position - 1).isBig()) gradientDrawable.setGradientRadius(3000); // если элемент большой или предыдущий элемент большой
            else gradientDrawable.setGradientRadius(fullGradient); // если элемент маленький или средний и предыдущий элемент маленький или средний
        }
        return gradientDrawable;
    }


    @Override
    public int getItemCount() {
        return activitySections.size();
    }

    @Override
    public int getItemViewType(int position) {
        return activitySections.get(position).getTemplate();
    }

    public static final class CardHolder extends RecyclerView.ViewHolder {
        public ImageView imageId, iconId;
        public TextView title, text;
        public BlurView blurView;

        public CardHolder(View itemView) {
            super(itemView);
            imageId = itemView.findViewById(R.id.image);
            iconId = itemView.findViewById(R.id.bigCardIcon);
            title = itemView.findViewById(R.id.cardTitle);
            text = itemView.findViewById(R.id.cardText);
        }

        public void bind(ActivitySection section) {
            if (section == null) return;
            imageId.setImageResource(section.getImageId());
            title.setText(section.getTitle());
            text.setText(section.getText());

            if (section.getIconId() != 0)
                iconId.setImageResource(section.getIconId());

            if (section.getColors() != null) {
                Shader shader = new LinearGradient(0, 0, 400, 0,
                        section.getColors(), null, Shader.TileMode.CLAMP);
                title.getPaint().setShader(shader);
                Log.d("shader", String.valueOf(section.getId()));
            }
        }
    }
}

