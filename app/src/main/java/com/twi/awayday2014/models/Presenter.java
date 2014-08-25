package com.twi.awayday2014.models;


public class Presenter {
    private String name;
    private int image;
    private int profileResourceId;

    public Presenter(String name, int image, int profileResourceId) {
        this.name = name;
        this.image = image;
        this.profileResourceId = profileResourceId;
    }

    public int profileResource() {
        return profileResourceId;
    }

    public String name() {
        return name;
    }
}
