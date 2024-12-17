package com.myapp.autogallery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.myapp.autogallery.R;
import com.myapp.autogallery.items.ActivitySection;

import java.io.IOException;
import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.CardHolder> {
    public static final int TITLE = 0;
    public static final int TEXT = 1;

    private final LayoutInflater inflater;
    private Context context;
    private final List<ActivitySection> activitySections;
    private final RecyclerView recyclerView;


    public ActivityAdapter(Context context, List<ActivitySection> activitiesCard, RecyclerView recyclerView) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        activitySections = activitiesCard;
        this.recyclerView = recyclerView;
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
        try {
            holder.bind(activitySections.get(position));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        holder.itemView.setLayoutParams(layoutParams);
    }

    public TextView getTextViewAtPosition(int position, int typeTextView) {
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder != null) {
            CardHolder cardHolder = (CardHolder) viewHolder;
            if (typeTextView == TITLE) return cardHolder.title;
            else if (typeTextView == TEXT) return cardHolder.text;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return activitySections.size();
    }

    @Override
    public int getItemViewType(int position) {
        return activitySections.get(position).getTemplate();
    }

    public ActivitySection getItem(int index) {
        return activitySections.get(index);
    }

    public void updateItem(int index, ActivitySection updatedItem) {
        activitySections.set(index, updatedItem);
        notifyItemChanged(index);
    }

    public static final class CardHolder extends RecyclerView.ViewHolder {
        public ImageView imageId, iconId;
        public TextView title, text;

        public CardHolder(View itemView) {
            super(itemView);
            imageId = itemView.findViewById(R.id.image);
            iconId = itemView.findViewById(R.id.bigCardIcon);
            title = itemView.findViewById(R.id.cardTitle);
            text = itemView.findViewById(R.id.cardText);
        }

        public void bind(ActivitySection section) throws IOException {
            if (section == null) return;

            if (section.uriImage == null) imageId.setImageResource(section.getImage());
            else {
                Bitmap bitmap =
                        MediaStore.Images.Media.getBitmap(section.getContext().getContentResolver(), section.uriImage);
                imageId.setImageBitmap(bitmap);
            }
            title.setText(section.getTitle());
            text.setText(section.getText());

            title.setTextColor(section.getTitleColor());
            text.setTextColor(section.getTextColor());

            if (section.getIcon() != 0) iconId.setImageResource(section.getIcon());

            if (section.getGradientColors() != null) {
                Shader shader = new LinearGradient(0, 0, 400, 0, section.getGradientColors(), null, Shader.TileMode.CLAMP);
                title.getPaint().setShader(shader);
            }
        }
    }
}

