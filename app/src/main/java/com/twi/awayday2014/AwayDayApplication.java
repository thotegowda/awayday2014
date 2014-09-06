package com.twi.awayday2014;

import com.orm.SugarApp;
import com.twi.awayday2014.services.ParseDataService;
import com.twi.awayday2014.services.parse.AgendaParseDataFetcher;
import com.twi.awayday2014.services.parse.PresenterParseDataFetcher;
import com.twi.awayday2014.services.twitter.TwitterPreference;
import com.twi.awayday2014.services.twitter.TwitterService;

public class AwayDayApplication extends SugarApp {

    private TwitterService twitterService;
    private ParseDataService parseDataService;
    private AgendaParseDataFetcher agendaParseDataFetcher;
    private PresenterParseDataFetcher presenterParseDataFetcher;

    @Override
    public void onCreate() {
        super.onCreate();
        parseDataService = new ParseDataService(this);
    }

    public TwitterService getTwitterService() {
        if (twitterService == null) {
            twitterService = new TwitterService(new TwitterPreference(this));
        }
        return twitterService;
    }

    public AgendaParseDataFetcher getAgendaParseDataFetcher() {
        if(agendaParseDataFetcher == null){
            agendaParseDataFetcher = new AgendaParseDataFetcher(this);
        }
        return agendaParseDataFetcher;
    }

    public PresenterParseDataFetcher getPresenterParseDataFetcher() {
        if(presenterParseDataFetcher == null){
            presenterParseDataFetcher = new PresenterParseDataFetcher(this);
        }
        return presenterParseDataFetcher;
    }

    public ParseDataService getParseDataService() {
        return parseDataService;
    }
}
