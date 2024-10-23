package com.myapp.autogallery.items;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.myapp.autogallery.R;


public class ActivitySection implements Parcelable {

    public static final int SMALL = R.layout.card_small; // height 160dp
    public static final int MEDIUM = R.layout.card_medium; // height 235dp
    public static final int BIG = R.layout.card_big; // height 140dp

    private boolean isBig = false;
    private int template;

    private int imageId, iconId;
    private final int ID;
    private String title, text;

    private int[] colors;

    public ActivitySection(int id, int imageId, String title, String text, int template) {
        if (template == BIG) isBig = true;
        ID = id;
        this.imageId = imageId;
        this.title = title;
        this.text = text;
        this.template = template;
    }

    public ActivitySection(Context context, int id, int imageId, int titleId, int textId, int template) {
        ID = id;
        this.imageId = imageId;
        this.title = context.getString(titleId);
        this.text = context.getString(textId);
        this.template = template;
        if (template == BIG) isBig = true;
    }

    public ActivitySection(int id, int imageId, String title, String text, int template, boolean big) {
        this(id, imageId, title, text, template);
        this.isBig = big;
    }


    public ActivitySection(int id, int imageId, int iconId, String title, String text, int template) {
        this(id, imageId, title, text, template);
        this.iconId = iconId;
    }

    public ActivitySection(int id, int image, int template) {
        ID = id;
        imageId = image;
        this.template = template;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(ID);
        parcel.writeInt(imageId);
        parcel.writeInt(iconId);
        parcel.writeString(title);
        parcel.writeString(text);
        parcel.writeInt(template);
    }

    protected ActivitySection(Parcel in) {
        imageId = in.readInt();
        iconId = in.readInt();
        ID = in.readInt();
        title = in.readString();
        text = in.readString();
        template = in.readInt();
    }

    public static final Creator<ActivitySection> CREATOR = new Creator<ActivitySection>() {
        @Override
        public ActivitySection createFromParcel(Parcel in) {
            return new ActivitySection(in);
        }

        @Override
        public ActivitySection[] newArray(int size) {
            return new ActivitySection[size];
        }
    };

    public void setColorText(Drawable gradientDrawable) {
        colors = ((GradientDrawable) gradientDrawable).getColors();
    }

    public void setImageId(int image) {
        imageId = image;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTemplate(int template) {
        this.template = template;
    }

    public void setBig(boolean isBig) { this.isBig = isBig; }

    public int getImageId() {
        return imageId;
    }

    public int getIconId() { return iconId; }

    public int getId() { return ID; }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public int getTemplate() { return template; }

    public boolean isBig() { return isBig; }

    public int[] getColors() { return colors; }

    @Override
    public int describeContents() { return 0; }

}
