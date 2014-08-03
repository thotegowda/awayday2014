package com.twi.awayday2014;

import android.database.sqlite.SQLiteException;
import com.orm.SugarApp;
import com.parse.*;
import com.twi.awayday2014.models.Presentation;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.models.ShortNotification;
import com.twi.awayday2014.ui.HomeActivity;

public class AwayDayApplication extends SugarApp {
    private static final String CHANNEL_AWAYDAY = "AwayDay";
    private Properties properties;
    private Tweeter tweeter;
    private SessionsOrganizer sessionsOrganizer;

    @Override
    public void onCreate() {
        super.onCreate();

        setupParse();

        setupDefaultNotifications();
        setupPresentations();
    }

    private void setupParse() {
        Parse.initialize(getApplicationContext(), DeveloperKeys.PARSE_APPLICATION_ID, DeveloperKeys.PARSE_CLIENT_KEY);

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);


        PushService.subscribe(this, CHANNEL_AWAYDAY, HomeActivity.class);

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    private void setupDefaultNotifications() {
        if (!hasShortNotificationRecords()) {
            new ShortNotification("Cool news: Android app is getting ready to help you experience away day better", "now").save();
            new ShortNotification("We'd like to welcome you to the 2014 India Away Day group on myThoughtWorks!", "25 Mins ago").save();
            new ShortNotification("We're super excited to announce the Away Day 2014 Logo Competition! That's right." +
                    "We want YOUR imagination and creativity for a fabulous logo for  Away Day!", "1 hour ago").save();
            new ShortNotification("Travelling plans are: going by train, coming back by Air", "10 hours ago").save();
            new ShortNotification("This time away day happens at Hyderabad", "5 days ago").save();
            new ShortNotification("Away day starts on 19th Sep and lasts lasts till 21st Sep", "12 days ago").save();
        }
    }

    private void setupPresentations() {
        //if (!hasPresentationRecords()) {
//        new Presenter("Martin Fowler", R.drawable.speaker_00).save();

//          new Presentation(, "keynote title1", "29/09/14 10.00 - 11.00").save();
//            new Presentation(new Presenter("Presenter name2", R.drawable.speaker_01), "keynote title2", "29/09/14 12.00 - 1.00").save();
//            new Presentation(new Presenter("Presenter name3", R.drawable.speaker_02), "keynote title3", "29/09/14 1.00 - 4.00").save();
//            new Presentation(new Presenter("Presenter name1", R.drawable.speaker_00), "session title1", "29/09/14 10.00 - 11.00").save();
//            new Presentation(new Presenter("Presenter name2", R.drawable.speaker_01), "session title2", "29/09/14 12.00 - 1.00").save();
//            new Presentation(new Presenter("Presenter name3", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00").save();
//            new Presentation(new Presenter("Presenter name4", R.drawable.speaker_00), "session title1", "29/09/14 10.00 - 11.00").save();
//            new Presentation(new Presenter("Presenter name5", R.drawable.speaker_01), "session title2", "29/09/14 12.00 - 1.00").save();
//            new Presentation(new Presenter("Presenter name6", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00").save();
//            new Presentation(new Presenter("Presenter name7", R.drawable.speaker_00), "session title1", "29/09/14 10.00 - 11.00").save();
//            new Presentation(new Presenter("Presenter name8", R.drawable.speaker_01), "session title2", "29/09/14 12.00 - 1.00").save();
//            new Presentation(new Presenter("Presenter name9", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00").save();
//            new Presentation(new Presenter("Presenter name10", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00").save();
//            new Presentation(new Presenter("Presenter name1", R.drawable.speaker_00), "keynote title1", "29/09/14 10.00 - 11.00").save();
//            new Presentation(new Presenter("Presenter name2", R.drawable.speaker_01), "keynote title2", "29/09/14 12.00 - 1.00").save();
//            new Presentation(new Presenter("Presenter name3", R.drawable.speaker_02), "keynote title3", "29/09/14 1.00 - 4.00").save();
//            new Presentation(new Presenter("Presenter name1", R.drawable.speaker_00), "session title1", "29/09/14 10.00 - 11.00").save();
//            new Presentation(new Presenter("Presenter name2", R.drawable.speaker_01), "session title2", "29/09/14 12.00 - 1.00").save();
//            new Presentation(new Presenter("Presenter name3", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00").save();
//            new Presentation(new Presenter("Presenter name4", R.drawable.speaker_00), "session title1", "29/09/14 10.00 - 11.00").save();
//            new Presentation(new Presenter("Presenter name5", R.drawable.speaker_01), "session title2", "29/09/14 12.00 - 1.00").save();
//            new Presentation(new Presenter("Presenter name6", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00").save();
//            new Presentation(new Presenter("Presenter name7", R.drawable.speaker_00), "session title1", "29/09/14 10.00 - 11.00").save();
//            new Presentation(new Presenter("Presenter name8", R.drawable.speaker_01), "session title2", "29/09/14 12.00 - 1.00").save();
//            new Presentation(new Presenter("Presenter name9", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00").save();
//            new Presentation(new Presenter("Presenter name10", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00").save();
        //}
    }

    private boolean hasPresentationRecords() {
        try {
            return Presentation.findAll(Presentation.class).hasNext();
        } catch(SQLiteException e) {
            return false;
        }
    }

    private boolean hasShortNotificationRecords() {
        try {
            return ShortNotification.findAll(ShortNotification.class).hasNext();
        } catch(SQLiteException e) {
            return false;
        }

    }

    public Tweeter getTwitterService() {
        if (tweeter == null) {
            properties = new Properties(this);
            tweeter = new Tweeter(properties);
        }
        return tweeter;
    }

    public SessionsOrganizer getSessionOrganizer() {
        if (sessionsOrganizer == null) {
            sessionsOrganizer = new SessionsOrganizer();
        }
        return sessionsOrganizer;
    }

}
