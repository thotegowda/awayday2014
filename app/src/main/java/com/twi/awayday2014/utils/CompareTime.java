package com.twi.awayday2014.utils;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.Interval;

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

    public static boolean hasHappenedWithinAnHour(DateTime dateTime) {
        return (dateTime.plusMinutes(60).isAfter(now()));
    }

    public static boolean hasHappenedWithinHalfAnHour(DateTime dateTime) {
        return (dateTime.plusMinutes(30).isAfter(now()));
    }

    public static boolean hasHappenedWithinTwoMinutes(DateTime dateTime) {
        return (dateTime.plusMinutes(2).isAfter(now()));
    }

    public static boolean doesTimeOverlap(DateTime startTime1, DateTime endTime1, DateTime startTime2, DateTime endTime2) {
        Interval interval1 = new Interval(startTime1, endTime1);
        Interval interval2 = new Interval(startTime2, endTime2);
        Interval overlap = interval1.overlap(interval2);
        return overlap != null;
    }

    public static String duration(DateTime start, DateTime end){
        Interval interval = new Interval(start, end);
        int duration = (interval.toDuration().toPeriod().getHours() * 60) + (interval.toDuration().toPeriod().getMinutes());
        return duration + "";
    }
}
