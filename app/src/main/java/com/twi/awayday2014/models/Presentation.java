package com.twi.awayday2014.models;


import com.orm.SugarRecord;

public class Presentation extends SugarRecord<Presentation> {

    private Presenter presenter;
    private String title;
    private String date;

    public Presentation() {

    }

    public Presentation(Presenter presenter, String title, String date) {
        this.presenter = presenter;
        this.title = title;
        this.date = date;
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

    public String description() {
        return "This is the session description holder, where you can place details about the session and its where abouts" +
                "This is the session description holder, where you can place details about the session and its where abouts" +
                "This is the session description holder, where you can place details about the session and its where abouts" +
                "This is the session description holder, where you can place details about the session and its where abouts";
    }
}
