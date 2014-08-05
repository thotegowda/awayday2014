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

        new DevEnvironment().setup();
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
