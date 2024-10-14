package com.myapp.autogallery.items;

public class ActivitySection {

    private int imageId, id;
    private String title, text;

    public ActivitySection(int id, int link, String title, String text) {
        this.id = id;
        this.imageId = link;
        this.title = title;
        this.text = text;
    }

    public int getImageId() {
        return imageId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
