package com.myapp.autogallery.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.AlignSelf;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.myapp.autogallery.R;
import com.myapp.autogallery.items.ActivitySection;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private static List<ActivitySection> activitiesSection;

    private Context context;
    private int count = 0;

    public ActivityAdapter(Context context, List<ActivitySection> activitiesCard) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        activitiesSection = activitiesCard;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View sectionItems;

        switch (viewType) {
            case 2:
                sectionItems = inflater.inflate(R.layout.card_big, parent, false);
                return new BigCardHolder(sectionItems);
            case 1:
                sectionItems = inflater.inflate(R.layout.card_medium, parent, false);
                return new MediumCardHolder(sectionItems);
            case 0:
                sectionItems = inflater.inflate(R.layout.card_small, parent, false);
                return new SmallCardHolder(sectionItems);
        }
        throw new RuntimeException("TAG ERROR");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BaseCardHolder cardHolder;
        CardView cardView;
        ViewGroup.MarginLayoutParams cardLayoutParams;

        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        float density = context.getResources().getDisplayMetrics().density;
        int px = Math.round(15 * density);

        if (holder instanceof BigCardHolder) {
//            BigCardHolder bigHolder = (BigCardHolder) holder;
            cardHolder = (BaseCardHolder) holder;
//            setData(bigHolder, position);

            layoutParams.setFullSpan(true);
            cardView = (CardView) cardHolder.itemView.findViewById(R.id.cardView);
            count++;
        }
        else if (holder instanceof MediumCardHolder) {
//            MediumCardHolder mediumHolder = (MediumCardHolder) holder;
            cardHolder = (MediumCardHolder) holder;
//            setData(mediumHolder, position);
            layoutParams.setFullSpan(false);
            cardView = (CardView) cardHolder.itemView.findViewById(R.id.cardView);
        }
        else {
//            SmallCardHolder smallHolder = (SmallCardHolder) holder;
            cardHolder = (SmallCardHolder) holder;
//            setData(smallHolder, position);
            layoutParams.setFullSpan(false);
            cardView = (CardView) cardHolder.itemView.findViewById(R.id.cardView);
        }
        cardHolder.bind(activitiesSection, position);

        cardLayoutParams = (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();

        if ((position - count) % 2 == 0) {

        }

        holder.itemView.setLayoutParams(layoutParams);
    }

    private boolean setData(BaseCardHolder holder, int position) {
        ActivitySection activity = activitiesSection.get(position);
        if (activity == null) return false;

        holder.imageId.setImageResource(activitiesSection.get(position).getImageId());
        holder.title.setText(activitiesSection.get(position).getTitle());
        holder.text.setText(activitiesSection.get(position).getText());
        return true;
    }

    @Override
    public int getItemCount() {
        return activitiesSection.size();
    }

    @Override
    public int getItemViewType(int position) {
        return activitiesSection.get(position).getTag();
    }

    public static final class BigCardHolder extends BaseCardHolder {
        public BigCardHolder(View itemView) {
            super(itemView, R.id.bigCardImage, R.id.bigCardTitle, R.id.bigCardText);
        }
    }

    static final class MediumCardHolder extends BaseCardHolder {
        public MediumCardHolder(View itemView) {
            super(itemView, R.id.mediumCardImage, R.id.mediumCardTitle, R.id.mediumCardText);
        }
    }

    static final class SmallCardHolder extends BaseCardHolder {
        public SmallCardHolder(View itemView) {
            super(itemView, R.id.smallCardImage, R.id.smallCardTitle, R.id.smallCardText);
        }
    }
}


abstract class BaseCardHolder extends RecyclerView.ViewHolder {
    public ImageView imageId;
    public TextView title, text;

    public BaseCardHolder(View itemView, int imageID, int titleId, int textId) {
        super(itemView);
        imageId = itemView.findViewById(imageID);
        title = itemView.findViewById(titleId);
        text = itemView.findViewById(textId);
    }

    public void bind(List<ActivitySection> sections, int position) {
        ActivitySection activity = sections.get(position);
        if (activity == null) return;

        imageId.setImageResource(activity.getImageId());
        title.setText(activity.getTitle());
        text.setText(activity.getText());
    }
}
