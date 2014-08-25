package com.twi.awayday2014.services;

import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.models.Session;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class AgendaService {

    public List<Session> getAgendaFor(DateTime day) {
        if (day.getDayOfMonth() == 27) {
            return sessionsFor27();
        } else {
            return sessionsFor28();
        }
    }

    private List<Session> sessionsFor27() {
        return new ArrayList<Session>(){{
            add(new Session(null, "Lunch & Settling In", "27 Sep", "12:00", null));
            add(new Session(new ArrayList<Presenter>(){{
                new Presenter("Deepa", R.drawable.placeholder, 0);
            }}, "Away Day Kick-off", "27 Sep", "15:00 - 15:30", null));
            add(new Session(null, "One year gone by, one year to come", "27 Sep", "15:30", "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum." +
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum."));
            add(new Session(null, "The other side of development in India", "27 Sep", "16:00", "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum." +
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum."));
            add(new Session(null, "Coffee Break", "27 Sep", "15:00", null));
            add(new Session(null, "Technology in the service of society", "27 Sep", "17:30", "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum." +
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum."));
            add(new Session(null, "Improving healthcare for India's underprevileged", "27 Sep", "18:00", "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum." +
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum."));
            add(new Session(null, "Music for change", "27 Sep", "19:15", "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum." +
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum."));
            add(new Session(null, "Dinner", "27 Sep", "20:15", null));
            add(new Session(null, "Fluid Time: Culturals and Networking", "27 Sep", "20:30", null));
        }};
    }

    private List<Session> sessionsFor28() {
        return new ArrayList<Session>(){{
            add(new Session(null, "TW Chairman", "28 Sep", "09:00", "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum." +
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum."));
            add(new Session(null, "Fighting the communialisation of India", "28 Sep", "10:00", "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum." +
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum."));
            add(new Session(null, "The other side of development in India", "28 Sep", "10:30", "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum." +
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum."));
            add(new Session(null, "Coffee Break", "28 Sep", "11:00", null));
            add(new Session(null, "Frugal Innovation: Building an OSS business in the Global South", "28 Sep", "11:30", "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum." +
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum."));
            add(new Session(null, "Beryllium", "28 Sep", "12:00", "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum." +
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum."));
            add(new Session(null, "Something really ThoughtWorks - Wait for it!", "28 Sep", "12:30", "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum." +
                    "Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum. Lorel ipsum contro borati cocum."));
            add(new Session(null, "Lunch", "28 Sep", "13:00", null));
            add(new Session(null, "Breakout Sessions", "28 Sep", "14:30", null));
            add(new Session(null, "Team Games", "28 Sep", "17:00", null));
            add(new Session(null, "Thought Bands", "28 Sep", "19:30", null));
            add(new Session(null, "Dinner", "28 Sep", "19:30", null));
            add(new Session(null, "DJ", "28 Sep", "22:00", null));
        }};
    }
}
