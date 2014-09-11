package com.twi.awayday2014.models;


import java.util.List;

public class BreakoutSession extends Session {

    private String stream;

    public BreakoutSession(String id, List<String> presenters, String title, String startTime,
                           String endTime, String date, String description, String location,
                           String imageId, String stream) {
        super(id, presenters, title, startTime, endTime, date, description, location, imageId, false);
        this.stream = stream;
    }

    public String getStream() {
        return stream;
    }
}
