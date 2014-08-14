package com.twi.awayday2014.models;


public class Session {

    private final Presenter[] presenter;
    private final String title;
    private final String date;
    private final String time;
    private final String description;

    public Session(Presenter[] presenter, String title, String date, String time, String description) {
        this.presenter = presenter;
        this.title = title;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    public Presenter[] getPresenter() {
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
}
