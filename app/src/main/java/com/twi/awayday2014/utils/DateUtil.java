package com.twi.awayday2014.utils;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

import static org.joda.time.DateTime.now;

public class DateUtil {

    public static String getDisplayTimeForTwitter(LocalDateTime then) {
        LocalDateTime now = LocalDateTime.now();

        int seconds = Math.abs(Seconds.secondsBetween(now, then).getSeconds());
        if (seconds <= 10) {
            return "now";
        }
        if (seconds < 60) {
            return String.valueOf(seconds) + "s";
        }

        int minutes = Math.abs(Minutes.minutesBetween(now, then).getMinutes());
        if (minutes < 60) {
            return String.valueOf(minutes) + "m";
        }

        int hours = Math.abs(Hours.hoursBetween(now, then).getHours());
        if (hours < 24) {
            return String.valueOf(hours) + "h";
        }

        int days = Math.abs(Days.daysBetween(now, then).getDays());
        if (days < 9) {
            return String.valueOf(days) + "d";
        }

        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
        return then.toString(formatter);
    }

    public static String formatDate(Date date) {
       return getDisplayTimeForTwitter(new LocalDateTime(date));
    }

    public static String getDisplayTimeForNotifications(DateTime time) {
        if (hasHappenedWithinHalfAnHour(time)) {
            return "Now";
        } else if (hasHappenedToday(time)) {
            return time.toString("HH:mm") + " Today";
        } else if (hasHappenedYesterday(time)) {
            return time.toString("HH:mm") + " Yesterday";
        } else if (hasHappenedThisYear(time)) {
            return time.toString("HH:mm , dd MMM");
        } else {
            return time.toString("dd MMM, yyyy");
        }
    }

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

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sec40 = LocalDateTime.now().minusSeconds(40);
        int diffsec = Minutes.minutesBetween(now, sec40).getMinutes();
        System.out.println("now: " + DateUtil.getDisplayTimeForTwitter(sec40));

        LocalDateTime min2 = LocalDateTime.now().minusMinutes(2);
        int diffMin = Minutes.minutesBetween(min2, now).getMinutes();
        System.out.println("min1: " + DateUtil.getDisplayTimeForTwitter(min2));

        LocalDateTime hrs5 = LocalDateTime.now().minusHours(5);
        int diffhrs = Hours.hoursBetween(hrs5, now).getHours();
        System.out.println("hrs4: " + DateUtil.getDisplayTimeForTwitter(hrs5));

        LocalDateTime days3 = LocalDateTime.now().minusDays(3);
        int diffDays = Days.daysBetween(days3, now).getDays();
        System.out.println("days3: " + DateUtil.getDisplayTimeForTwitter(days3));

        System.out.println("");

    }
}
