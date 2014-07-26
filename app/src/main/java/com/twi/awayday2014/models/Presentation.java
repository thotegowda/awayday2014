package com.twi.awayday2014.models;


public class Presentation {

    private final Presenter presenter;
    private final String title;
    private final String date;
    private int profileResourceId;

    public Presentation(Presenter presenter, String title, String date) {
        this.presenter = presenter;
        this.title = title;
        this.date = date;
    }

    public Presenter presenter() {
        return presenter;
    }

    public String formatedDate() {
        return date;
    }

    public String title() {
        return title;
    }
}
