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

    private static AgendaService agendaService;
    private TwitterService twitterService;
    private ParseDataService parseDataService;

    @Override
    public void onCreate() {
        super.onCreate();

        parseDataService = new ParseDataService(this);
    }

    public AgendaService getAgendaService() {
        if (agendaService == null) {
            agendaService = new AgendaService();
        }
        return agendaService;
    }

    public TwitterService getTwitterService() {
        if (twitterService == null) {
            twitterService = new TwitterService(new TwitterPreference(this));
        }
        return twitterService;
    }

    public ParseDataService getParseDataService() {
        return parseDataService;
    }
}
