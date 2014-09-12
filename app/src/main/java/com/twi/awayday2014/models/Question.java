package com.twi.awayday2014.models;


import com.parse.ParseObject;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

import static com.twi.awayday2014.utils.CompareTime.duration;
import static com.twi.awayday2014.utils.CompareTime.hasHappenedThisYear;
import static com.twi.awayday2014.utils.CompareTime.hasHappenedToday;
import static com.twi.awayday2014.utils.CompareTime.hasHappenedWithinAnHour;
import static com.twi.awayday2014.utils.CompareTime.hasHappenedWithinHalfAnHour;
import static com.twi.awayday2014.utils.CompareTime.hasHappenedWithinTwoMinutes;
import static com.twi.awayday2014.utils.CompareTime.hasHappenedYesterday;
import static com.twi.awayday2014.utils.Constants.Parse.QUESTIONER_NAME;
import static com.twi.awayday2014.utils.Constants.Parse.QUESTION_TEXT;
import static com.twi.awayday2014.utils.Constants.Parse.SESSION_ID;
import static com.twi.awayday2014.utils.Constants.Parse.SESSION_TITLE;
import static org.joda.time.DateTime.now;

public class Question {

   private String sessionId;
   private String sessionTitle;
   private String Name;
   private String question;
   private Date createdAt;

    public Question() {

    }

    public Question(String sessionId, String sessionTitle, String name, String question) {
        this.sessionId = sessionId;
        this.sessionTitle = sessionTitle;
        Name = name;
        this.question = question;
    }

    public Question(String sessionId, String sessionTitle, String name, String question, Date createdAt) {
        this.sessionId = sessionId;
        this.sessionTitle = sessionTitle;
        Name = name;
        this.question = question;
        this.createdAt = createdAt;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getSessionTitle() {
        return sessionTitle;
    }

    public String getName() {
        return Name;
    }

    public String getQuestion() {
        return question;
    }

    public Date getCreatedDate() {
        return createdAt;
    }

    public static Question newQuestion(ParseObject parseObject) {
        return new Question(
                (String) parseObject.get(SESSION_ID),
                (String) parseObject.get(SESSION_TITLE),
                (String) parseObject.get(QUESTIONER_NAME),
                (String) parseObject.get(QUESTION_TEXT),
                parseObject.getCreatedAt());

    }

    public String getDisplayTime() {
        DateTime t = new DateTime(createdAt);
        if (hasHappenedWithinTwoMinutes(t)) {
            return "Now";
        } else if (hasHappenedWithinAnHour(t)) {
            return duration(t, now());
        } else {
            return t.toString("HH:mm , dd MMM");
        }
    }
}
