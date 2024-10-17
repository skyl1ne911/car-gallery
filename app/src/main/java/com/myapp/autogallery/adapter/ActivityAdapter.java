package com.myapp.autogallery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.myapp.autogallery.R;
import com.myapp.autogallery.items.ActivitySection;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<ActivitySection> activitiesSection;
    private int spanCount = 0;

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
        ConstraintLayout constraintLayout;
        ConstraintLayout.MarginLayoutParams marginLayoutParams;
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        holder.itemView.setLayoutParams(layoutParams);

        int leftRightMargin = context.getResources().getDimensionPixelSize(R.dimen.marginLeftRightCard);
        int topBottomMargin = context.getResources().getDimensionPixelSize(R.dimen.marginTopCard);

        if (holder instanceof BigCardHolder) {
            cardHolder = (BigCardHolder) holder;
            layoutParams.setFullSpan(true);
            spanCount = 0;
        }
        else if (holder instanceof MediumCardHolder) {
            cardHolder = (MediumCardHolder) holder;
            layoutParams.setFullSpan(false);
            spanCount++;
        }
        else {
            cardHolder = (SmallCardHolder) holder;
            layoutParams.setFullSpan(false);
            spanCount++;
        }
        cardHolder.bind(activitiesSection, position);


        constraintLayout = (ConstraintLayout) cardHolder.itemView.findViewById(R.id.container);
        if (constraintLayout != null)  {
            marginLayoutParams = (ViewGroup.MarginLayoutParams) constraintLayout.getLayoutParams();
            int marginLeft, marginRight;
            boolean isLastItem = (position == getItemCount() - 1);

            if (spanCount == 0) {
                marginLeft = leftRightMargin;
                marginRight = leftRightMargin;
            }
            else if (spanCount % 2 == 0) {
                marginLeft = leftRightMargin / 2;
                marginRight = leftRightMargin;
            }
            else {
                marginLeft = leftRightMargin;
                marginRight = leftRightMargin / 2;
            }

            if (isLastItem) {
                marginLayoutParams.setMargins(marginLeft, topBottomMargin, marginRight, topBottomMargin);
            }
            else {
                marginLayoutParams.setMarginStart(marginLeft);
                marginLayoutParams.setMarginEnd(marginRight);
            }
            constraintLayout.setLayoutParams(marginLayoutParams);
        }
    }

    @Override
    public int getItemCount() {
        return activitiesSection.size();
    }

    @Override
    public int getItemViewType(int position) {
        return activitiesSection.get(position).tag;
    }

    static final class BigCardHolder extends BaseCardHolder {
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

    public void setPattern(int titlePattern, int textPattern) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) title.getLayoutParams();
        ConstraintSet constraintSet = new ConstraintSet();

        switch (titlePattern) {
            case 0:
                layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
//                constraintSet.clear(R.id.text);
                break;
        }
    }
}
