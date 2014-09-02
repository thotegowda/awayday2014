package com.twi.awayday2014.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Comparator;

import static com.twi.awayday2014.utils.CompareTime.hasHappenedThisYear;
import static com.twi.awayday2014.utils.CompareTime.hasHappenedToday;
import static com.twi.awayday2014.utils.CompareTime.hasHappenedWithinHalfAnHour;
import static com.twi.awayday2014.utils.CompareTime.hasHappenedYesterday;

public class AwayDayNotification extends SugarRecord<AwayDayNotification> implements Parcelable{
    private String title;
    private String time;
    private String description;
    private NotificationType type;

    //used by sugar orm
    public AwayDayNotification() {
    }

    public AwayDayNotification(String title, DateTime time, String description, NotificationType type) {
        this.title = title;
        this.time = time.toString("dd/MM/yyyy HH:mm:ss");
        this.description = description;
        this.type = type;
    }

    public AwayDayNotification(Parcel in) {
        title = in.readString();
        time = in.readString();
        description = in.readString();
        type = NotificationType.fromDisplayText(in.readString());
    }


    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public NotificationType getType() {
        return type;
    }

    public String getDisplayTime() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        DateTime t = formatter.parseDateTime(this.time);
        if (hasHappenedWithinHalfAnHour(t)) {
            return "Now";
        } else if (hasHappenedToday(t)) {
            return t.toString("HH:mm") + " Today";
        } else if (hasHappenedYesterday(t)) {
            return t.toString("HH:mm") + " Yesterday";
        } else if (hasHappenedThisYear(t)) {
            return t.toString("HH:mm , dd MMM");
        } else {
            return t.toString("dd MMM, yyyy");
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(time);
        dest.writeString(description);
        dest.writeString(type.getDisplayText());
    }

    public static final Parcelable.Creator<AwayDayNotification> CREATOR
            = new Parcelable.Creator<AwayDayNotification>() {
        public AwayDayNotification createFromParcel(Parcel in) {
            return new AwayDayNotification(in);
        }

        public AwayDayNotification[] newArray(int size) {
            return new AwayDayNotification[size];
        }
    };

}
