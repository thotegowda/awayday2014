package com.twi.awayday2014.models;


import com.orm.SugarRecord;
import com.twi.awayday2014.R;

import java.util.ArrayList;
import java.util.List;

public class Session extends SugarRecord<Session> {

    private Presenter presenter;
    private String title;
    private String date;
    private String time;
    private String description;
    private String location;
    private boolean scheduled;

    public Session() {

    }

    public Session(Presenter presenter, String title, String date, String time, String description, String location) {
        this.presenter = presenter;
        this.title = title;
        this.date = date;
        this.time = time;
        this.description = description;
        this.location = location;
    }

    public Presenter getPresenter() {
        return presenter;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    public int getSessionResource() {
        return R.drawable.notifications_image_sessions;
    }
}
