package com.twi.awayday2014.models;

import org.joda.time.DateTime;

public class Tweet {
    private String tweet;
    private String username;
    private String twitterHandle;
    private DateTime dateTime;

    public Tweet(String tweet, String username, String twitterHandle, DateTime dateTime) {
        this.tweet = tweet;
        this.username = username;
        this.twitterHandle = twitterHandle;
        this.dateTime = dateTime;
    }

    public String getTweet() {
        return tweet;
    }

    public String getUsername() {
        return username;
    }

    public String getTwitterHandle() {
        return twitterHandle;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public String getDisplayDetails() {
        return "- " + username + " (@" + twitterHandle + ") " +
                dateTime.toString("dd MMM yyyy");
    }
}
