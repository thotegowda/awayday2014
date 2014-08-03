package com.twi.awayday2014.models;


import com.orm.SugarRecord;

public class Presentation extends SugarRecord<Presentation> {

    private Presenter presenter;
    private String title;
    private String date;
    private boolean isKeynoteSession;
    private boolean isScheduled;

    public Presentation() {

    }

    public Presentation(Presenter presenter, String title, String date, boolean isKeynoteSession) {
        this.presenter = presenter;
        this.title = title;
        this.date = date;
        this.isKeynoteSession = isKeynoteSession;
        this.isScheduled = false;
    }

    public Presenter presenter() {
        return presenter;
    }

    public String formattedDate() {
        return date;
    }

    public String title() {
        return title;
    }

    public boolean isScheduled() {
        return isScheduled;
    }

    public String description() {
        return "This is the session description holder, where you can place details about the session and its where abouts" +
                "This is the session description holder, where you can place details about the session and its where abouts" +
                "This is the session description holder, where you can place details about the session and its where abouts" +
                "This is the session description holder, where you can place details about the session and its where abouts.";
    }

    public void setScheduled(boolean isScheduled) {
        this.isScheduled = isScheduled;
    }
}
