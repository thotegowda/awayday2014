package com.twi.awayday2014.models;


import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Comparator;
import java.util.List;

public class Session {

    private String id;
    private List<String> presenters;
    private String title;
    private String startTime;
    private String endTime;
    private String date;
    private String description;
    private String location;
    private String imageId;
    private String imageUrl;
    private DateTimeFormatter dateTimeParser;
    private Boolean canAskQuestions;

    public Session(String id, List<String> presenters, String title, String startTime, String endTime, String date,
                   String description, String location, String image, Boolean canAskQuestions) {
        this.id = id;
        this.presenters = presenters;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.description = description;
        this.location = location;
        this.imageId = imageId;
        dateTimeParser = ISODateTimeFormat.dateTimeParser();
        this.canAskQuestions = canAskQuestions;
    }

    public List<String> getPresenters() {
        return presenters;
    }

    public String getTitle() {
        return title;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageId() {
        return imageId;
    }

    public boolean canAskQuestions() {
        return canAskQuestions;
    }

    public String getDisplayTime(){
        DateTime startTime = dateTimeParser.parseDateTime(this.startTime);
        DateTime endTime = dateTimeParser.parseDateTime(this.endTime);
        return startTime.toString("HH:mm") + " - " + endTime.toString("HH:mm");
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getId() {
        return id;
    }

    public static class SessionsComparator implements Comparator<Session> {
        DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();
        @Override
        public int compare(Session lhs, Session rhs) {
            DateTime lhsStartTime = parser.parseDateTime(lhs.startTime);
            DateTime rhsStartTime = parser.parseDateTime(rhs.startTime);
            if (lhsStartTime.isAfter(rhsStartTime)) {
                return 1;
            } else if (lhsStartTime.isBefore(rhsStartTime)) {
                return -1;
            }
            return 0;
        }
    }
}
