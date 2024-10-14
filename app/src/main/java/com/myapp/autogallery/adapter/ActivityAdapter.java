package com.myapp.autogallery.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.autogallery.R;
import com.myapp.autogallery.items.ActivitySection;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;

    private static List<ActivitySection> activitiesSection;

    public ActivityAdapter(Context context, List<ActivitySection> activitiesCard) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        activitiesSection = activitiesCard;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View sectionItems = inflater.inflate(R.layout.big_card, parent, false);
        return new ViewHolder(sectionItems);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageId.setImageResource(activitiesSection.get(position).getImageId());
        holder.title.setText(activitiesSection.get(position).getTitle());
        holder.text.setText(activitiesSection.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return activitiesSection.size();
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageId;
        public TextView title, text;

        public ViewHolder(View itemView) {
            super(itemView);
            imageId = itemView.findViewById(R.id.bigCardImage);
            title = itemView.findViewById(R.id.bigCardTitle);
            text = itemView.findViewById(R.id.bigCardText);
        }

    }

}
