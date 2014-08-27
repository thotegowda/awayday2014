package com.twi.awayday2014.models;

import org.joda.time.DateTime;

import java.util.Comparator;

import static com.twi.awayday2014.CompareTime.*;

public class Notification {
    private String title;
    private DateTime time;
    private String description;
    private NotificationType type;

    public Notification(String title, DateTime time, String description, NotificationType type) {
        this.title = title;
        this.time = time;
        this.description = description;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public DateTime getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public NotificationType getType() {
        return type;
    }

    public String getDisplayTime() {
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

    public static class NotificatonsComparator implements Comparator<Notification>{

        @Override
        public int compare(Notification lhs, Notification rhs) {
            if (lhs.time.isAfter(rhs.time)) {
                return 1;
            } else if (lhs.time.isBefore(rhs.time)) {
                return -1;
            }
            return 0;
        }
    }

}
