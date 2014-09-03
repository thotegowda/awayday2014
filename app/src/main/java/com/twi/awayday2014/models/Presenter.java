package com.twi.awayday2014.models;

import com.orm.SugarRecord;
import com.twi.awayday2014.R;

public class Presenter extends SugarRecord<Presenter> {
    private String name;
    private String title;
    private String description;

    public Presenter() {
    }

    public Presenter(String name, String title, String description) {
        this.name = name;
        this.title = title;
        this.description = description;
    }

    public int profileResource() {
        return R.drawable.notifications_image_sessions;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
