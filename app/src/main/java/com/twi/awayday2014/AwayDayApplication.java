package com.twi.awayday2014;

import com.orm.SugarApp;
import com.parse.*;
import com.twi.awayday2014.service.AgendaService;
import com.twi.awayday2014.ui.HomeActivity;

public class AwayDayApplication extends SugarApp {
    private static final String CHANNEL_AWAYDAY = "AwayDay";
    private static AgendaService agendaService;
    private TwitterPreference twitterPreference;
    private Tweeter tweeter;
    private SessionsOrganizer sessionsOrganizer;

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


    public static AgendaService agendaService(){
        if(agendaService == null){
            agendaService = new AgendaService();
        }
        return agendaService;
    }

}
