package com.twi.awayday2014.services;

import android.database.sqlite.SQLiteException;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.models.Session;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class AgendaService {

    public AgendaService() {
        setupSessions();
    }

    public List<Session> getAgendaFor(DateTime day) {
        if (day.getDayOfMonth() == 27) {
            return Session.listAll(Session.class);
        } else {
            return Session.listAll(Session.class);
        }
    }

    public List<Session> getScheduledSessions() {
        return Session.listAll(Session.class);
    }

    private void setupSessions() {
        //deleteSessions();
        if (hasPresentationRecords()) {
            return;
        }

        for (int i = 0; i < 10; i++) {
            save("Martin Fowler",
                    "This will contain presenter headline",
                    "this is presenter description",
                    "One year gone by, one year to come",
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum.Lorel ipsum contro borati cocum. " +
                            "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum.",
                    "29/09/14 10.00 - 11.00");
        }
    }

    private void save(String presenterName, String presenterTitle, String presenterDescription,
                      String sessionTitle, String sessionDescription, String date) {
        Presenter presenter = new Presenter(presenterName, presenterTitle, presenterDescription);
        presenter.save();

        new Session(presenter, sessionTitle, date, "12.00", sessionDescription, "Convention Hall").save();
    }

    private boolean hasPresentationRecords() {
        try {
            return Session.findAll(Session.class).hasNext();
        } catch (SQLiteException e) {
            return false;
        }
    }

    private List<Session> sessionsFor27() {
        return new ArrayList<Session>() {{
            add(new Session(null, "Lunch & Settling In", "27 Sep", "12:00", null, "Convention Hall"));
            add(new Session(null, "Away Day Kick-off", "27 Sep", "15:00 - 15:30", null, "Convention Hall"));
            add(new Session(null, "One year gone by, one year to come", "27 Sep", "15:30", "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum." +
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum.", "Convention Hall"));
            add(new Session(null, "The other side of development in India", "27 Sep", "16:00", "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum." +
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum.", "Convention Hall"));
            add(new Session(null, "Coffee Break", "27 Sep", "15:00", null, "Convention Hall"));
            add(new Session(null, "Technology in the service of society", "27 Sep", "17:30", "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum." +
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum.", "Convention Hall"));
            add(new Session(null, "Improving healthcare for India's underprevileged", "27 Sep", "18:00", "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum." +
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum.", "Convention Hall"));
            add(new Session(null, "Music for change", "27 Sep", "19:15", "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum." +
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum.", "Convention Hall"));
            add(new Session(null, "Dinner", "27 Sep", "20:15", null, "Convention Hall"));
            add(new Session(null, "Fluid Time: Culturals and Networking", "27 Sep", "20:30", null, "Convention Hall"));
        }};
    }

    private List<Session> sessionsFor28() {
        return new ArrayList<Session>() {{
            add(new Session(null, "TW Chairman", "28 Sep", "09:00", "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum." +
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum.", "Convention Hall"));
            add(new Session(null, "Fighting the communialisation of India", "28 Sep", "10:00", "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum." +
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum.", "Convention Hall"));
            add(new Session(null, "The other side of development in India", "28 Sep", "10:30", "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum." +
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum.", "Convention Hall"));
            add(new Session(null, "Coffee Break", "28 Sep", "11:00", null, "Convention Hall"));
            add(new Session(null, "Frugal Innovation: Building an OSS business in the Global South", "28 Sep", "11:30", "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum." +
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum.", "Convention Hall"));
            add(new Session(null, "Beryllium", "28 Sep", "12:00", "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum." +
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum.", "Convention Hall"));
            add(new Session(null, "Something really ThoughtWorks - Wait for it!", "28 Sep", "12:30", "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum." +
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum.", "Convention Hall"));
            add(new Session(null, "Lunch", "28 Sep", "13:00", null, "Convention Hall"));
            add(new Session(null, "Breakout Sessions", "28 Sep", "14:30", null, "Convention Hall"));
            add(new Session(null, "Team Games", "28 Sep", "17:00", null, "Convention Hall"));
            add(new Session(null, "Thought Bands", "28 Sep", "19:30", null, "Convention Hall"));
            add(new Session(null, "Dinner", "28 Sep", "19:30", null, "Convention Hall"));
            add(new Session(null, "DJ", "28 Sep", "22:00", null, "Convention Hall"));
        }};
    }


}
