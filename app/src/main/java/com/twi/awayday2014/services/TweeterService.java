package com.twi.awayday2014.services;

import com.twi.awayday2014.models.Tweet;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class TweeterService {

    public List<Tweet> getTweets(){
        return new ArrayList<Tweet>(){{
           add(new Tweet("This app is so nice, i try to install it twice.", "Baba Blacksheep", "babaBlackSheep",
                   new DateTime().withHourOfDay(16).withMinuteOfHour(45).withDayOfMonth(13).withMonthOfYear(8).withYear(2014)));
            add(new Tweet("This app is so nice, i try to install it twice.", "Baba Blacksheep", "babaBlackSheep",
                    new DateTime().withHourOfDay(16).withMinuteOfHour(45).withDayOfMonth(13).withMonthOfYear(8).withYear(2014)));
            add(new Tweet("This app is so nice, i try to install it twice.", "Baba Blacksheep", "babaBlackSheep",
                    new DateTime().withHourOfDay(16).withMinuteOfHour(45).withDayOfMonth(13).withMonthOfYear(8).withYear(2014)));
            add(new Tweet("This app is so nice, i try to install it twice.", "Baba Blacksheep", "babaBlackSheep",
                    new DateTime().withHourOfDay(16).withMinuteOfHour(45).withDayOfMonth(13).withMonthOfYear(8).withYear(2014)));
            add(new Tweet("This app is so nice, i try to install it twice.", "Baba Blacksheep", "babaBlackSheep",
                    new DateTime().withHourOfDay(16).withMinuteOfHour(45).withDayOfMonth(13).withMonthOfYear(8).withYear(2014)));
            add(new Tweet("This app is so nice, i try to install it twice.", "Baba Blacksheep", "babaBlackSheep",
                    new DateTime().withHourOfDay(16).withMinuteOfHour(45).withDayOfMonth(13).withMonthOfYear(8).withYear(2014)));
            add(new Tweet("This app is so nice, i try to install it twice.", "Baba Blacksheep", "babaBlackSheep",
                    new DateTime().withHourOfDay(16).withMinuteOfHour(45).withDayOfMonth(13).withMonthOfYear(8).withYear(2014)));
            add(new Tweet("This app is so nice, i try to install it twice.", "Baba Blacksheep", "babaBlackSheep",
                    new DateTime().withHourOfDay(16).withMinuteOfHour(45).withDayOfMonth(13).withMonthOfYear(8).withYear(2014)));
        }};
    }

}
