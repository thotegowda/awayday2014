package com.twi.awayday2014.services.parse;

import android.content.Context;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.twi.awayday2014.models.Session;

import com.twi.awayday2014.utils.Constants;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.twi.awayday2014.utils.Constants.Parse.*;

public class AgendaParseDataFetcher extends BaseParseDataFetcher<Session> {
    private static final String TAG = "AgendaParseDataFetcher";
    private List<Session> sessions = new ArrayList<Session>();
    private boolean isFetching;
    private boolean isFetched;

    public AgendaParseDataFetcher(Context context) {
        super(context);
    }

    @Override
    protected String getTable() {
        return TABLE_AGENDA;
    }

    @Override
    public void fetchData() {
        if (isFetching) {
            Log.d(TAG, "Already fetching data");
            return;
        }
        ParseQuery<ParseObject> query = getQuery();
        Log.d(TAG, "Fetching agendas.");

        notifyFetchSource(query);

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
                        String title = d.getString(COL_SESSION_ITEM);
                        String startTime = d.getString(COL_START);
                        String endTime = d.getString(COL_END);
                        String date = d.getString(COL_DATE);
                        String location = d.getString(COL_LOCATION);
                        String description = d.getString(COL_DESCRIPTION);
                        String image = d.getString(COL_IMAGE);
                        JSONArray speakers = d.getJSONArray(COL_SPEAKERS);
                        Boolean canAskQuestions = d.getBoolean(COL_CAN_ASK_QUESTIONS);
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
                                image, canAskQuestions));
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

            private void notifyDataFetch(List<Session> sessions) {
                isFetching = false;
                isFetched = true;
                AgendaParseDataFetcher.this.sessions = sessions;
                for (ParseDataListener dataListener : listeners) {
                    dataListener.onDataFetched(sessions, true);
                }
            }
        });
    }

    @Override
    protected ParseQuery<ParseObject> getQuery() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_AGENDA);
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        return query;
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

}
