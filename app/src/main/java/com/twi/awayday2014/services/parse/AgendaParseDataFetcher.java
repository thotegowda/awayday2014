package com.twi.awayday2014.services.parse;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.models.Session;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.twi.awayday2014.utils.Constants.Parse.COL_DATE;
import static com.twi.awayday2014.utils.Constants.Parse.COL_DESCRIPTION;
import static com.twi.awayday2014.utils.Constants.Parse.COL_END;
import static com.twi.awayday2014.utils.Constants.Parse.COL_IMAGE;
import static com.twi.awayday2014.utils.Constants.Parse.COL_LOCATION;
import static com.twi.awayday2014.utils.Constants.Parse.COL_OBJECT_ID;
import static com.twi.awayday2014.utils.Constants.Parse.COL_SESSION_TITLE;
import static com.twi.awayday2014.utils.Constants.Parse.COL_SPEAKERS;
import static com.twi.awayday2014.utils.Constants.Parse.COL_START;
import static com.twi.awayday2014.utils.Constants.Parse.ERROR_EXCEPTION_THROWN;
import static com.twi.awayday2014.utils.Constants.Parse.ERROR_NO_DATA_FOUND;
import static com.twi.awayday2014.utils.Constants.Parse.TABLE_AGENDA;
import static com.twi.awayday2014.utils.Constants.Parse.TABLE_IMAGES;

public class AgendaParseDataFetcher implements ParseDataFetcher<Session> {
    private static final String TAG = "AgendaParseDataFetcher";
    private List<ParseDataListener> listeners = new ArrayList<ParseDataListener>();
    private List<Session> sessions = new ArrayList<Session>();
    private boolean isFetching;
    private boolean isFetched;

    @Override
    public boolean isDataOutdated() {
        return false;
    }

    public AgendaParseDataFetcher() {
        super();
    }

    @Override
    public void fetchData() {
        if (isFetching) {
            Log.d(TAG, "Already fetching data");
            return;
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_AGENDA);
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        Log.d(TAG, "Fetching agendas.");
        if (query.hasCachedResult()) {
            Log.d(TAG, "Cache Hit : true");
            for (ParseDataListener listener : listeners) {
                listener.fetchingFromCache();
            }
        } else {
            Log.d(TAG, "Cache Hit : false");
            for (ParseDataListener listener : listeners) {
                listener.fetchingFromNetwork();
            }
        }

        isFetching = true;
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> data, ParseException e) {
                if (e == null) {
                    List<Session> sessions = new ArrayList<Session>();
                    List<String> imageObjectIds = new ArrayList<String>();
                    Log.d(TAG, "Number of sessions: " + data.size());
                    if (data.size() == 0) {
                        for (ParseDataListener listener : listeners) {
                            listener.onDataFetchError(ERROR_NO_DATA_FOUND);
                        }
                    }
                    for (ParseObject d : data) {
                        String id = d.getObjectId();
                        String title = d.getString(COL_SESSION_TITLE);
                        String startTime = d.getString(COL_START);
                        String endTime = d.getString(COL_END);
                        String date = d.getString(COL_DATE);
                        String location = d.getString(COL_LOCATION);
                        String description = d.getString(COL_DESCRIPTION);
                        String image = d.getString(COL_IMAGE);
                        JSONArray speakers = d.getJSONArray(COL_SPEAKERS);
                        List<String> presenters = new ArrayList<String>();
                        if (speakers != null) {
                            try {
                                for (int i = 0; i < speakers.length(); i++) {
                                    presenters.add(speakers.getString(i));
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                        sessions.add(new Session(id, presenters, title, startTime,
                                endTime, date, description, location,
                                image));
                        if (image != null) {
                            imageObjectIds.add(image);
                        }
                    }
                    Log.d(TAG, imageObjectIds.size() + " sessions have image");
                    fetchImageUrlAndNotify(sessions, imageObjectIds);
                } else {
                    e.printStackTrace();
                    isFetching = false;
                    for (ParseDataListener listener : listeners) {
                        listener.onDataFetchError(ERROR_EXCEPTION_THROWN);
                    }
                }
            }

            private void fetchImageUrlAndNotify(final List<Session> sessions, final List<String> imageObjectIds) {
                if (imageObjectIds.size() == 0) {
                    notifyDataFetch(sessions);
                    return;
                }

                List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
                for (String imageObject : imageObjectIds) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_IMAGES);
                    query.whereEqualTo(COL_OBJECT_ID, imageObject);
                    queries.add(query);
                }
                ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
                mainQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                Log.d(TAG, "Cache Hit for images query: " + mainQuery.hasCachedResult());
                mainQuery.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> results, ParseException e) {
                        if (e == null) {
                            Map<String, String> images = new HashMap<String, String>();
                            for (ParseObject result : results) {
                                String id = result.getObjectId();
                                ParseFile parseFile = result.getParseFile(COL_IMAGE);
                                images.put(id, parseFile.getUrl());
                            }

                            for (Session session : sessions) {
                                if (images.containsKey(session.getImageId())) {
                                    session.setImageUrl(images.get(session.getImageId()));
                                }
                            }

                            notifyDataFetch(sessions);
                        } else {
                            Log.e(TAG, "Error: " + e.getMessage());
                            isFetching = false;
                            for (ParseDataListener dataListener : listeners) {
                                dataListener.onDataFetchError(ERROR_EXCEPTION_THROWN);
                            }
                        }
                    }
                });
                notifyDataFetch(sessions);
            }

            private void notifyDataFetch(List<Session> sessions) {
                isFetching = false;
                isFetched = true;
                AgendaParseDataFetcher.this.sessions = sessions;
                for (ParseDataListener dataListener : listeners) {
                    dataListener.onDataFetched(sessions);
                }
            }
        });
    }

    @Override
    public void invalidateAndFetchFreshData() {

    }

    @Override
    public boolean isDataFetched() {
        return isFetched;
    }

    @Override
    public List<Session> getFetchedData() {
        return sessions;
    }

    @Override
    public Session getDataById(String id) {
        for (Session session : sessions) {
            if (session.getId().equals(id)) {
                return session;
            }
        }
        return null;
    }


    @Override
    public void addListener(ParseDataListener parseDataListener) {
        if (parseDataListener != null) {
            listeners.add(parseDataListener);
        }
    }

    @Override
    public void removeListener(ParseDataListener parseDataListener) {
        listeners.remove(parseDataListener);
    }
}
