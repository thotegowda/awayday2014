package com.twi.awayday2014;

import com.orm.SugarApp;
import com.twi.awayday2014.services.MyAgendaService;
import com.twi.awayday2014.services.ParseDataService;
import com.twi.awayday2014.services.parse.AgendaParseDataFetcher;
import com.twi.awayday2014.services.parse.BreakoutSessionsParseDataFetcher;
import com.twi.awayday2014.services.parse.PresenterParseDataFetcher;
import com.twi.awayday2014.services.parse.QuestionService;
import com.twi.awayday2014.services.twitter.TwitterPreference;
import com.twi.awayday2014.services.twitter.TwitterService;

import net.danlew.android.joda.JodaTimeAndroid;

public class AwayDayApplication extends SugarApp {

    private TwitterService twitterService;
    private ParseDataService parseDataService;
    private AgendaParseDataFetcher agendaParseDataFetcher;
    private BreakoutSessionsParseDataFetcher breakoutSessionsParseDataFetcher;
    private PresenterParseDataFetcher presenterParseDataFetcher;
    private QuestionService questionService;
    private MyAgendaService myAgendaService;

    @Override
    public void onCreate() {
        super.onCreate();
        parseDataService = new ParseDataService(this);
        JodaTimeAndroid.init(this);
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

    public MyAgendaService getMyAgendaService() {
        if(myAgendaService == null){
            myAgendaService = new MyAgendaService(getBreakoutSessionsParseDataFetcher());
        }
        return myAgendaService;
    }

    public BreakoutSessionsParseDataFetcher getBreakoutSessionsParseDataFetcher() {
        if(breakoutSessionsParseDataFetcher == null){
            breakoutSessionsParseDataFetcher = new BreakoutSessionsParseDataFetcher(this);
        }
        return breakoutSessionsParseDataFetcher;
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

    public QuestionService getQuestionService() {
        if (questionService == null) {
            questionService = new QuestionService();
        }
        return questionService;
    }
}
