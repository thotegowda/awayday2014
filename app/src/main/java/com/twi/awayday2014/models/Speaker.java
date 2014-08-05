package com.twi.awayday2014.models;

import com.orm.SugarRecord;

public class Speaker extends SugarRecord<Speaker> {
    private final String name;
    private final String shortBio;
    private final String detailedBio;
    private final int imageResourceId;

    public Speaker(String name, String shortBio, String detailedBio, int imageResourceId) {
        this.name = name;
        this.shortBio = shortBio;
        this.detailedBio = detailedBio;
        this.imageResourceId = imageResourceId;
    }

    public String name() {
        return name;
    }

    public String shortBio() {
        return shortBio;
    }

    public String detailedBio() {
        return detailedBio;
    }

    public int imageResourceId() {
        return imageResourceId;
    }

}
