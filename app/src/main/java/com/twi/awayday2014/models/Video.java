package com.twi.awayday2014.models;

public class Video {
    private final String name;
    private final String link;
    private final int thumbnail;

    public Video(String name, String link, int thumbnail) {
        this.name = name;
        this.link = link;
        this.thumbnail = thumbnail;
    }

    public String name() {
        return name;
    }

    public String link() {
        return link;
    }

    public int thumbnail() {
        return thumbnail;
    }

}
