package com.myapp.autogallery.items;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


public class ActivitySection implements Parcelable {

    public static final int SMALL = 0;
    public static final int MEDIUM = 1;
    public static final int BIG = 2;

    public static final int CONTENT_UPLEFT = 0;
    public static final int CONTENT_UPRIGHT = 1;
    public static final int CONTENT_LOWLEFT = 2;
    public static final int CONTENT_LOWRIGHT = 3;


    private int imageId;
    private final int ID;
    private String title, text;
    public final int tag;
    private int titlePattern, textPattern;

    public ActivitySection(int id, int imageId, String title, String text, int tag) {
        ID = id;
        this.imageId = imageId;
        this.title = title;
        this.text = text;
        this.tag = tag;
    }

    public ActivitySection(Context context, int id, int imageId, int titleId, int textId, int tag) {
        ID = id;
        this.imageId = imageId;
        this.title = context.getString(titleId);
        this.text = context.getString(textId);
        this.tag = tag;
    }

    public ActivitySection(int id, int image, int tag) {
        ID = id;
        imageId = image;
        this.tag = tag;
    }

    public ActivitySection(int id, int imageId, String title, String text, int tag,
                           int titlePattern, int textPattern) throws IllegalArgumentException {
        this(id, imageId, title, text, tag);

        if (titlePattern > 3 || textPattern > 3)
            throw new IllegalArgumentException("WRONG PATTERN VALUE");

        this.titlePattern = titlePattern;
        this.textPattern = textPattern;
    }


    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(ID);
        parcel.writeInt(imageId);
        parcel.writeString(title);
        parcel.writeString(text);
        parcel.writeInt(tag);
    }

    protected ActivitySection(Parcel in) {
        imageId = in.readInt();
        ID = in.readInt();
        title = in.readString();
        text = in.readString();
        tag = in.readInt();
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

    public void setTextPattern(int number) {
        if (number > 3 || number < 0) throw new IllegalArgumentException("WRONG VALUE");
        textPattern = number;
    }

    public void setTitlePattern(int number) {
        if (number > 3 || number < 0) throw new IllegalArgumentException("WRONG VALUE");
        titlePattern = number;
    }

    public int getImageId() {
        return imageId;
    }

    public int getId() { return ID;}

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public int getTextPattern() { return textPattern; }

    public int getTitlePattern() { return titlePattern; }

    @Override
    public int describeContents() { return 0; }

}
