package com.twi.awayday2014;

import android.database.sqlite.SQLiteException;
import com.orm.SugarApp;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.models.Session;
import com.twi.awayday2014.services.AgendaService;
import com.twi.awayday2014.services.ParseDataService;
import com.twi.awayday2014.services.twitter.TwitterPreference;
import com.twi.awayday2014.services.twitter.TwitterService;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class AwayDayApplication extends SugarApp {

    private static final String YOUR_APPLICATION_ID = "cRkqJtkVvjyuC5pvKRzLNz8CFm6WgrbPqX0uKX7a";
    private static final String YOUR_CLIENT_KEY = "o4Dr0m1oV8PBWw0DQSeaYmd9T3LSayKIPJCkbIxd";


    private static AgendaService agendaService;
    private TwitterService twitterService;
    private ParseDataService parseDataService;

    @Override
    public void onCreate() {
        super.onCreate();

        setupParse();
    }

    public AgendaService getAgendaService(){
        if(agendaService == null){
            agendaService = new AgendaService();
        }
        return agendaService;
    }

    private void setupParse() {
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);

        ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }


    public TwitterService getTwitterService() {
        if (twitterService == null) {
            twitterService = new TwitterService(new TwitterPreference(this));
        }
        return twitterService;
    }

    public ParseDataService getParseDataService(){
        if(parseDataService == null){
            parseDataService = new ParseDataService();
        }
        return parseDataService;
    }
}
