package com.twi.awayday2014.utils;

import org.joda.time.DateTime;

import static org.joda.time.DateTime.now;

public class CompareTime {

    public static boolean hasHappenedThisYear(DateTime dateTime) {
        return now().year().equals(dateTime.year());
    }

    private static boolean hasHappendThisMonth(DateTime dateTime) {
        return hasHappenedThisYear(dateTime)
                && now().monthOfYear().equals(dateTime.monthOfYear());
    }

    public static boolean hasHappenedToday(DateTime dateTime) {
        return hasHappendThisMonth(dateTime)
                && now().dayOfMonth().equals(dateTime.dayOfMonth());
    }

    public static boolean hasHappenedYesterday(DateTime dateTime) {
        return hasHappendThisMonth(dateTime)
                && now().dayOfMonth().equals(dateTime.plusDays(1).dayOfMonth());
    }

    public static boolean hasHappenedWithinHalfAnHour(DateTime dateTime) {
        return (dateTime.plusMinutes(30).isAfter(now()));
    }
}
