package com.twi.awayday2014;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateUtil {

    public static String formatDate(LocalDateTime then) {
        LocalDateTime now = LocalDateTime.now();

        int minutes = Math.abs(Minutes.minutesBetween(now, then).getMinutes());
        if (minutes <= 1) {
            return "now";
        }

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
       return formatDate(new LocalDateTime(date));
    }

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sec40 = LocalDateTime.now().minusSeconds(40);
        int diffsec = Minutes.minutesBetween(now, sec40).getMinutes();
        System.out.println("now: " + DateUtil.formatDate(sec40));

        LocalDateTime min2 = LocalDateTime.now().minusMinutes(2);
        int diffMin = Minutes.minutesBetween(min2, now).getMinutes();
        System.out.println("min1: " + DateUtil.formatDate(min2));

        LocalDateTime hrs5 = LocalDateTime.now().minusHours(5);
        int diffhrs = Hours.hoursBetween(hrs5, now).getHours();
        System.out.println("hrs4: " + DateUtil.formatDate(hrs5));

        LocalDateTime days3 = LocalDateTime.now().minusDays(3);
        int diffDays = Days.daysBetween(days3, now).getDays();
        System.out.println("days3: " + DateUtil.formatDate(days3));

        System.out.println("");

    }
}
