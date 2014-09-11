package com.twi.awayday2014.services;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.twi.awayday2014.adapters.BreakoutAdapter;
import com.twi.awayday2014.models.BreakoutSession;
import com.twi.awayday2014.models.SavedSession;
import com.twi.awayday2014.models.Session;
import com.twi.awayday2014.services.parse.BreakoutSessionsParseDataFetcher;
import com.twi.awayday2014.utils.CompareTime;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAgendaService {
    private static final String TAG = "MyAgendaService";
    private final DateTimeFormatter dateTimeParser;

    private List<MyAgendaServiceListener> listeners = new ArrayList<MyAgendaServiceListener>();
    private BreakoutSessionsParseDataFetcher breakoutSessionsParseDataFetcher;


    public MyAgendaService(BreakoutSessionsParseDataFetcher breakoutSessionsParseDataFetcher) {
        this.breakoutSessionsParseDataFetcher = breakoutSessionsParseDataFetcher;
        dateTimeParser = ISODateTimeFormat.dateTimeParser();
    }

    public void fetchSavedSessions(final MyAgendaServiceListener caller) {
        new AsyncTask<Void, Void, List<SavedSession>>() {
            @Override
            protected List<SavedSession> doInBackground(Void... params) {
                return SavedSession.listAll(SavedSession.class);
            }

            @Override
            protected void onPostExecute(List<SavedSession> savedSessionList) {
                for (MyAgendaServiceListener listener : listeners) {
                    listener.onSavedSessionsFetched(savedSessionList, caller);
                }
            }
        }.execute();
    }

    public void addSessionToAgenda(final Session session, final MyAgendaServiceListener caller) {
        new AsyncTask<Void, Void, AddSessionResultSet>() {
            @Override
            protected AddSessionResultSet doInBackground(Void... params) {
                AddSessionResultSet resultSet = new AddSessionResultSet();
                Session collidingSession = findSavedCollidingSession(session);
                if (collidingSession != null) {
                    resultSet.collidingSession = collidingSession;
                    resultSet.sessionToBeSaved = session;
                } else {
                    final SavedSession newSavedSession = new SavedSession(session.getId());
                    newSavedSession.save();
                    resultSet.savedSession = newSavedSession;
                }

                return resultSet;
            }

            @Override
            protected void onPostExecute(AddSessionResultSet resultSet) {
                if (resultSet.savedSession != null) {
                    Log.d(TAG, "Session \"" + session.getTitle() + "\" saved");
                    for (MyAgendaServiceListener listener : listeners) {
                        listener.onSessionAddedToAgenda(resultSet.savedSession, session, caller);
                    }
                } else {
                    Log.d(TAG, "Session \"" + resultSet.sessionToBeSaved.getTitle() + "\" colliding with session \""
                            + resultSet.collidingSession.getTitle() + "\'");
                    for (MyAgendaServiceListener listener : listeners) {
                        listener.onSessionCollide(resultSet.sessionToBeSaved, resultSet.collidingSession, caller);
                    }
                }
            }
        }.execute();
    }

    public void removeSessionFromAgenda(final Session session, final MyAgendaServiceListener caller) {
        new AsyncTask<Void, Void, SavedSession>() {
            @Override
            protected SavedSession doInBackground(Void... params) {
                List<SavedSession> savedSessionList = SavedSession.listAll(SavedSession.class);
                SavedSession sessionToBeRemoved = null;
                for (SavedSession savedSession : savedSessionList) {
                    if (savedSession.sessionId.equals(session.getId())) {
                        sessionToBeRemoved = savedSession;
                        break;
                    }
                }
                if (sessionToBeRemoved == null) {
                    Log.e(TAG, "Saved session is not found. This should never happen as it was found some time back");
                    return null;
                } else {
                    sessionToBeRemoved.delete();
                    Log.d(TAG, "Session with id " + sessionToBeRemoved.sessionId + " removed");
                    return sessionToBeRemoved;
                }
            }

            @Override
            protected void onPostExecute(SavedSession removedSession) {
                if (removedSession == null) {
                    for (MyAgendaServiceListener listener : listeners) {
                        listener.onSessionRemovedError(caller);
                        return;
                    }
                }

                Log.d(TAG, "Session \"" + removedSession.sessionId + "\" removed");
                for (MyAgendaServiceListener listener : listeners) {
                    listener.onSessionRemoved(removedSession, session, caller);
                }
            }
        }.execute();
    }

    private Session findSavedCollidingSession(Session session) {
        Map<String, BreakoutSession> allBreakoutSessionsMap = getAllBreakoutSessionsMap();
        List<SavedSession> allSavedSessions = SavedSession.listAll(SavedSession.class);
        for (SavedSession savedSession : allSavedSessions) {
            BreakoutSession breakoutSession = allBreakoutSessionsMap.get(savedSession.sessionId);
            DateTime startTime1 = dateTimeParser.parseDateTime(breakoutSession.getStartTime());
            DateTime endTime1 = dateTimeParser.parseDateTime(breakoutSession.getEndTime());
            DateTime startTime2 = dateTimeParser.parseDateTime(session.getStartTime());
            DateTime endTime2 = dateTimeParser.parseDateTime(session.getEndTime());
            if (CompareTime.doesTimeOverlap(startTime1, endTime1, startTime2, endTime2)) {
                return breakoutSession;
            }
        }
        return null;
    }

    public void replaceSession(final Session newSession, final Session oldSession, MyAgendaServiceListener caller) {
        new AsyncTask<Void, Void, ReplaceSessionResultSet>() {
            @Override
            protected ReplaceSessionResultSet doInBackground(Void... params) {
                ReplaceSessionResultSet resultSet = new ReplaceSessionResultSet();
                List<SavedSession> savedSessionList = SavedSession.listAll(SavedSession.class);
                SavedSession sessionToBeRemoved = null;
                for (SavedSession savedSession : savedSessionList) {
                    if (savedSession.sessionId.equals(oldSession.getId())) {
                        sessionToBeRemoved = savedSession;
                        break;
                    }
                }

                resultSet.oldSession = sessionToBeRemoved;
                if (sessionToBeRemoved == null) {
                    Log.e(TAG, "Saved session is not found. This should never happen as it was found some time back");
                } else {
                    sessionToBeRemoved.delete();
                    Log.d(TAG, "Session with id " + sessionToBeRemoved.sessionId + " removed");
                }

                SavedSession newSavedSession = new SavedSession(newSession.getId());
                newSavedSession.save();
                resultSet.newSession = newSavedSession;
                return resultSet;
            }

            @Override
            protected void onPostExecute(ReplaceSessionResultSet data) {
                for (MyAgendaServiceListener listener : listeners) {
                    listener.onSessionReplaced(data.newSession, data.oldSession);
                }
            }
        }.execute();

    }

    public void addListener(MyAgendaServiceListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(MyAgendaServiceListener listener) {
        listeners.remove(listener);
    }

    private Map<String, BreakoutSession> getAllBreakoutSessionsMap() {
        List<BreakoutSession> allBreakoutSessions = breakoutSessionsParseDataFetcher.getFetchedData();
        Map<String, BreakoutSession> result = new HashMap<String, BreakoutSession>();
        for (BreakoutSession breakoutSession : allBreakoutSessions) {
            result.put(breakoutSession.getId(), breakoutSession);
        }
        return result;
    }

    public interface MyAgendaServiceListener {
        void onSavedSessionsFetched(List<SavedSession> savedSessions, MyAgendaServiceListener caller);

        void onSessionAddedToAgenda(SavedSession savedSession, Session breakoutSession, MyAgendaServiceListener caller);

        void onSessionRemoved(SavedSession session, Session breakoutSession, MyAgendaServiceListener caller);

        void onSessionRemovedError(MyAgendaServiceListener caller);

        void onSessionCollide(Session currentSession, Session savedSession, MyAgendaServiceListener caller);

        void onSessionReplaced(SavedSession newSession, SavedSession oldSession);
    }

    private class AddSessionResultSet {
        SavedSession savedSession;
        Session collidingSession;
        Session sessionToBeSaved;
    }

    private class ReplaceSessionResultSet {
        SavedSession newSession;
        SavedSession oldSession;
    }
}
