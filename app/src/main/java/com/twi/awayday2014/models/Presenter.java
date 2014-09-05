package com.twi.awayday2014.models;

import com.twi.awayday2014.R;

public class Presenter{
    private String id;
    private String imageUrl;
    private String imageId;
    private String writeUp;
    private String name;

    public Presenter() {
    }

    public Presenter(String id, String name, String writeUp) {
        this.id = id;
        this.writeUp = name;
        this.name = writeUp;
    }

    public int profileResource() {
        return R.drawable.notifications_image_sessions;
    }

    public String getName() {
        return writeUp;
    }

    public String getWriteUp() {
        return name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.writeUp = name;
    }

    public void setWriteUp(String writeUp) {
        this.name = writeUp;
    }

    public String getId() {
        return id;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
