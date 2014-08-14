package com.twi.awayday2014.services;

import com.twi.awayday2014.models.Notification;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import static com.twi.awayday2014.models.NotificationType.APP_UPDATE;
import static com.twi.awayday2014.models.NotificationType.FOOD;
import static com.twi.awayday2014.models.NotificationType.INFO;
import static com.twi.awayday2014.models.NotificationType.SESSIONS;
import static com.twi.awayday2014.models.NotificationType.TRAVEL;

public class NotificationsService {

    public List<Notification> getNotifications(){
        return new ArrayList<Notification>(){{
            add(new Notification("Lunch Time",
                    new DateTime().withHourOfDay(12).withMinuteOfHour(30).withDayOfMonth(13).withMonthOfYear(8).withYear(2014),
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum.",
                    FOOD));
            add(new Notification("Chennai Folks Started",
                    new DateTime().withHourOfDay(16).withMinuteOfHour(45).withDayOfMonth(13).withMonthOfYear(8).withYear(2014),
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum.",
                    TRAVEL));
            add(new Notification("Train Tickets Mailed",
                    new DateTime().withHourOfDay(10).withMinuteOfHour(30).withDayOfMonth(12).withMonthOfYear(8).withYear(2014),
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum.",
                    TRAVEL));
            add(new Notification("Breakout Sessions Starting Now",
                    new DateTime().withHourOfDay(16).withMinuteOfHour(30).withDayOfMonth(27).withMonthOfYear(7).withYear(2014),
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum.",
                    SESSIONS));
            add(new Notification("Videos Section Added",
                    new DateTime().withHourOfDay(14).withMinuteOfHour(15).withDayOfMonth(15).withMonthOfYear(9).withYear(2013),
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum.",
                    APP_UPDATE));
            add(new Notification("Some Useful Information",
                    new DateTime().withHourOfDay(19).withMinuteOfHour(10).withDayOfMonth(15).withMonthOfYear(9).withYear(2013),
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum.",
                    INFO));
        }};
    }
}
