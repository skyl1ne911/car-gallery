package com.myapp.autogallery.items;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.myapp.autogallery.R;


public class ActivitySection implements Parcelable {

    public static final int SMALL = R.layout.card_small; // height 160dp
    public static final int MEDIUM = R.layout.card_medium; // height 235dp
    public static final int BIG = R.layout.card_big; // height 140dp

    private final int template;
    private boolean isBig;

    private final int image;
    private final int icon;
    private final int ID;
    private final String title;
    private final String text;
    private int[] gradientColors;
    private int titleColor, textColor;

    public Uri uriImage;
    private Context context;
    public Bitmap bitmap;

    private ActivitySection(CardBuilder builder) {
        ID = builder.id;
        template = builder.template;
        image = builder.image;
        icon = builder.icon;
        title = builder.title;
        text = builder.text;
        isBig = builder.big;
        textColor = builder.textColor;
        titleColor = builder.titleColor;
        context = builder.context;
    }

    public void setTextColor(int color) { textColor = color; }
    public void setTitleColor(int color) { titleColor = color; }
    public void setGradient(Drawable gradient) {
        gradientColors = ((GradientDrawable) gradient).getColors();
    }

    public static class CardBuilder {
        private final Context context;
        private int template;
        private boolean big = true;

        private final int id;
        private int image, icon;
        private String title, text;
        private int textColor = Color.WHITE, titleColor = Color.WHITE;


        public CardBuilder(Context context, int id) {
            this.context = context;
            this.template = ActivitySection.BIG;
            this.id = id;
        }

        public CardBuilder(Context context, int id, int template) {
            this.context = context;
            this.template = template;
            this.id = id;
        }

        public CardBuilder setTemplate(int template, boolean isBig) {
            this.template = template;
            big = isBig;
            return this;
        }

        public CardBuilder setImage(int image) {
            this.image = image;
            return this;
        }

        public CardBuilder setIcon(int icon) {
            this.icon = icon;
            return this;
        }

        public CardBuilder setTitle(int title) {
            this.title = context.getString(title);
            return this;
        }

        public CardBuilder setText(int text) {
            this.text = context.getString(text);
            return this;
        }

        public CardBuilder setSize(boolean big) {
            this.big = big;
            return this;
        }

        public CardBuilder setTitleColor(int color) {
            this.titleColor = color;
            return this;
        }

        public CardBuilder setTextColor(int color) {
            this.textColor = color;
            return this;
        }

        public ActivitySection build() {
            return new ActivitySection(this);
        }
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(ID);
        parcel.writeInt(image);
        parcel.writeInt(icon);
        parcel.writeString(title);
        parcel.writeString(text);
        parcel.writeInt(template);
        parcel.writeParcelable(uriImage, i);
    }

    protected ActivitySection(Parcel in) {
        image = in.readInt();
        icon = in.readInt();
        ID = in.readInt();
        title = in.readString();
        text = in.readString();
        template = in.readInt();
        uriImage = in.readParcelable(Uri.class.getClassLoader());
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


    public Context getContext() { return context; }
    public int getImage() { return image; }
    public int getIcon() { return icon; }
    public int getId() { return ID; }
    public String getTitle() { return title; }
    public String getText() { return text; }
    public int getTemplate() { return template; }
    public boolean isBig() { return isBig; }
    public int getTitleColor() { return titleColor; }
    public int getTextColor() { return textColor; }
    public int[] getGradientColors() { return gradientColors; }

    @Override
    public int describeContents() { return 0; }
}
