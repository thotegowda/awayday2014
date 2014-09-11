package com.twi.awayday2014.models;

import com.orm.SugarRecord;

public class SavedSession extends SugarRecord<SavedSession> {
    public String sessionId;

    public SavedSession() {
    }

    public SavedSession(String id) {
        this.sessionId = id;
    }
}
