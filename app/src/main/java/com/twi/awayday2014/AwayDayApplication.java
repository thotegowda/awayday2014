package com.twi.awayday2014;

import com.orm.SugarApp;
import com.parse.*;
import com.twi.awayday2014.models.ShortNotification;
import com.twi.awayday2014.view.HomeActivity;

import static com.twi.awayday2014.models.ShortNotification.*;

public class AwayDayApplication extends SugarApp {

    private static final String YOUR_APPLICATION_ID = "yiKETlRYko1LrZEgrilwJbp2XHmFeVSpAFkrOGGK";
    private static final String YOUR_CLIENT_KEY = "kR3esD6RWpRixzwKblqQux73nMRVXRqBkprv80rU";
    private static final String CHANNEL_AWAYDAY = "AwayDay";

    @Override
    public void onCreate() {
        super.onCreate();

        setupParse();

        setupDefaultNotifications();
    }

    private void setupDefaultNotifications() {
        if (!findAll(ShortNotification.class).hasNext()) {
            new ShortNotification("Cool news: Android app is getting ready to help you experience away day better", "now").save();
            new ShortNotification("We'd like to welcome you to the 2014 India Away Day group on myThoughtWorks!", "25 Mins ago").save();
            new ShortNotification("We're super excited to announce the Away Day 2014 Logo Competition! That's right." +
                    "We want YOUR imagination and creativity for a fabulous logo for  Away Day!", "1 hour ago").save();
            new ShortNotification("Travelling plans are: going by train, coming back by Air", "10 hours ago").save();
            new ShortNotification("This time away day happens at Hyderabad", "5 days ago").save();
            new ShortNotification("Away day starts on 19th Sep and lasts lasts till 21st Sep", "12 days ago").save();
        }
    }

    private void setupParse() {
        Parse.initialize(getApplicationContext(), YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);


        PushService.subscribe(this, CHANNEL_AWAYDAY, HomeActivity.class);

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }


}
