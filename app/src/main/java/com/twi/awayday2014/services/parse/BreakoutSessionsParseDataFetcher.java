package com.twi.awayday2014.services.parse;

import android.content.Context;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.twi.awayday2014.models.BreakoutSession;
import com.twi.awayday2014.models.Session;
import com.twi.awayday2014.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.twi.awayday2014.utils.Constants.Parse.COL_DATE;
import static com.twi.awayday2014.utils.Constants.Parse.COL_DESCRIPTION;
import static com.twi.awayday2014.utils.Constants.Parse.COL_DESCRIPTION2;
import static com.twi.awayday2014.utils.Constants.Parse.COL_END;
import static com.twi.awayday2014.utils.Constants.Parse.COL_IMAGE;
import static com.twi.awayday2014.utils.Constants.Parse.COL_LOCATION;
import static com.twi.awayday2014.utils.Constants.Parse.COL_SESSION_ITEM;
import static com.twi.awayday2014.utils.Constants.Parse.COL_SESSION_TITLE;
import static com.twi.awayday2014.utils.Constants.Parse.COL_SPEAKERS;
import static com.twi.awayday2014.utils.Constants.Parse.COL_START;
import static com.twi.awayday2014.utils.Constants.Parse.COL_STREAM;
import static com.twi.awayday2014.utils.Constants.Parse.ERROR_EXCEPTION_THROWN;
import static com.twi.awayday2014.utils.Constants.Parse.ERROR_NO_DATA_FOUND;
import static com.twi.awayday2014.utils.Constants.Parse.TABLE_AGENDA;
import static com.twi.awayday2014.utils.Constants.Parse.TABLE_BREAKOUTS;

public class BreakoutSessionsParseDataFetcher extends BaseParseDataFetcher<BreakoutSession> {
    private static final String TAG = "BreakoutSessionsParseDataFetcher";
    private List<BreakoutSession> sessions = new ArrayList<BreakoutSession>();
    private boolean isFetching;
    private boolean isFetched;

    public BreakoutSessionsParseDataFetcher(Context context) {
        super(context);
    }

    @Override
    protected String getTable() {
        return TABLE_BREAKOUTS;
    }

    @Override
    public void fetchData() {
        if (isFetching) {
            Log.d(TAG, "Already fetching data");
            return;
        }
        ParseQuery<ParseObject> query = getQuery();
        Log.d(TAG, "Fetching breakouts.");

        notifyFetchSource(query);

        isFetching = true;
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> data, ParseException e) {
                if (e == null) {
                    List<BreakoutSession> sessions = new ArrayList<BreakoutSession>();
                    List<String> imageObjectIds = new ArrayList<String>();
                    Log.d(TAG, "Number of breakout sessions: " + data.size());
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
                        String description = d.getString(COL_DESCRIPTION2);
                        String image = d.getString(COL_IMAGE);
                        String stream = d.getString(COL_STREAM);
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
                        sessions.add(new BreakoutSession(id, presenters, title, startTime,
                                endTime, date, description, location,
                                image,stream));
                        if (image != null) {
                            imageObjectIds.add(image);
                        }
                    }
                    Log.d(TAG, imageObjectIds.size() + " breakout sessions have image");
                    fetchImageUrlAndNotify(sessions, imageObjectIds);
                } else {
                    e.printStackTrace();
                    isFetching = false;
                    for (ParseDataListener listener : listeners) {
                        listener.onDataFetchError(ERROR_EXCEPTION_THROWN);
                    }
                }
            }

            private void fetchImageUrlAndNotify(final List<BreakoutSession> sessions, final List<String> imageObjectIds) {
                if(imageObjectIds.size() == 0){
                    notifyDataFetch(sessions);
                    return;
                }

                fetchImages(imageObjectIds, new ImageFetchListener() {
                    @Override
                    public void onImageFetch(Map<String, String> images, ParseException e) {
                        if(e == null){
                            for (Session session : sessions) {
                                if (images.containsKey(session.getImageId())) {
                                    session.setImageUrl(images.get(session.getImageId()));
                                }
                            }

                            notifyDataFetch(sessions);
                        }else{
                            Log.e(TAG, "Error: " + e.getMessage());
                            isFetching = false;
                            for (ParseDataListener dataListener : listeners) {
                                dataListener.onDataFetchError(ERROR_EXCEPTION_THROWN);
                            }
                        }
                    }
                });
            }

            private void notifyDataFetch(List<BreakoutSession> sessions) {
                isFetching = false;
                isFetched = true;
                BreakoutSessionsParseDataFetcher.this.sessions = sessions;
                for (ParseDataListener dataListener : listeners) {
                    dataListener.onDataFetched(sessions);
                }
            }
        });
    }

    @Override
    protected ParseQuery<ParseObject> getQuery() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_BREAKOUTS);
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        return query;
    }

    @Override
    public boolean isDataFetched() {
        return isFetched;
    }

    @Override
    public List<BreakoutSession> getFetchedData() {
        return sessions;
    }

    @Override
    public BreakoutSession getDataById(String id) {
        for (BreakoutSession session : sessions) {
            if (session.getId().equals(id)) {
                return session;
            }
        }
        return null;
    }

}
