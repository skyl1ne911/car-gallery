package com.myapp.autogallery.items;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


public class ActivitySection implements Parcelable {

    public static final int BIG = 2;
    public static final int MEDIUM = 1;
    public static final int SMALL = 0;


    private int imageId;
    private final int ID;
    private String title, text;
    private final int tag;

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

    public int getTag() { return tag; }

    @Override
    public int describeContents() { return 0; }

}
