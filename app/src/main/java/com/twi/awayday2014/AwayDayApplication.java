package com.twi.awayday2014;

import android.graphics.Bitmap;
import com.orm.SugarApp;
import com.parse.*;
import com.twi.awayday2014.service.AgendaService;
import com.twi.awayday2014.service.NotificationsService;
import com.twi.awayday2014.service.SessionsOrganizer;
import com.twi.awayday2014.service.twitter.Tweeter;
import com.twi.awayday2014.service.twitter.TwitterPreference;
import com.twi.awayday2014.components.activities.HomeActivity;
import com.twi.awayday2014.utils.DevEnvironment;
import com.twi.awayday2014.utils.DeveloperKeys;

public class AwayDayApplication extends SugarApp {
    private static final String CHANNEL_AWAYDAY = "AwayDay";
    private static AgendaService agendaService;
    private static NotificationsService notificationsService;
    private TwitterPreference twitterPreference;
    private Tweeter tweeter;
    private SessionsOrganizer sessionsOrganizer;
    private Bitmap homeActivityScreenshot;

    @Override
    public void onCreate() {
        super.onCreate();

        setupParse();

        new DevEnvironment().setup();
    }

    private void setupParse() {
        Parse.initialize(getApplicationContext(), null, null);
        Parse.initialize(getApplicationContext(), DeveloperKeys.PARSE_APPLICATION_ID, DeveloperKeys.PARSE_CLIENT_KEY);

        ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        PushService.setDefaultPushCallback(this, HomeActivity.class);

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public Tweeter getTwitterService() {
        if (tweeter == null) {
            twitterPreference = new TwitterPreference(this);
            tweeter = new Tweeter(twitterPreference);
        }
        return tweeter;
    }

    public SessionsOrganizer getSessionOrganizer() {
        if (sessionsOrganizer == null) {
            sessionsOrganizer = new SessionsOrganizer();
        }
        return sessionsOrganizer;
    }


    public static AgendaService agendaService() {
        if (agendaService == null) {
            agendaService = new AgendaService();
        }
        return agendaService;
    }

    public static NotificationsService notificationsService() {
        if (notificationsService == null) {
            return new NotificationsService();
        }
        return notificationsService;
    }


    public Bitmap getHomeActivityScreenshot() {
        return homeActivityScreenshot;
    }

    public void setHomeActivityScreenshot(Bitmap homeActivityScreenshot) {
        this.homeActivityScreenshot = homeActivityScreenshot;
    }

}
