package com.twi.awayday2014.service;

import com.orm.StringUtil;
import com.twi.awayday2014.models.Presentation;
import com.twi.awayday2014.models.Presenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SessionsOrganizer {

    private final List<Presentation> presentations;
    private final List<Presentation> keynotes;

    public SessionsOrganizer() {
        keynotes = Presentation.find(Presentation.class, StringUtil.toSQLName("isKeynoteSession") + "=" + String.valueOf(1));
        presentations = Presentation.find(Presentation.class, StringUtil.toSQLName("isKeynoteSession") + "=" + String.valueOf(0));
    }

    public List<Presentation> keynotes() {
        return keynotes;
    }

    public List<Presentation> sessions() {
        return presentations;
    }

    public List<Presentation> getScheduledSessions() {
        return Presentation.find(Presentation.class, StringUtil.toSQLName("isScheduled") + "=" + String.valueOf(1));
    }
}
