package com.twi.awayday2014.models;


import java.util.List;

public class BreakoutSession extends Session {

    private String stream;
    private String trackColor;

    public BreakoutSession(String id, List<String> presenters, String title, String startTime, String endTime, String date, String description, String location, String imageId, String stream, String trackColor) {
        super(id, presenters, title, startTime, endTime, date, description, location, imageId,false);
        this.stream = stream;
        this.trackColor = trackColor;
    }

    public String getStream() {
        return stream;
    }

    public String getTrackColor() {
        return trackColor;
    }
}
