package com.twi.awayday2014.models;


public class Presenter {
    private String name;
    private int profileResourceId;

    public Presenter(String name, int profileResourceId) {
        this.name = name;
        this.profileResourceId = profileResourceId;
    }

    public int profileResource() {
        return profileResourceId;
    }

    public String name() {
        return name;
    }
}
