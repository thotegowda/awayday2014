package com.twi.awayday2014.services.parse;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.twi.awayday2014.utils.Constants.Parse.COL_CHANGE_COUNTER;
import static com.twi.awayday2014.utils.Constants.Parse.COL_IMAGE;
import static com.twi.awayday2014.utils.Constants.Parse.COL_OBJECT_ID;
import static com.twi.awayday2014.utils.Constants.Parse.COL_TABLE;
import static com.twi.awayday2014.utils.Constants.Parse.ERROR_EXCEPTION_THROWN;
import static com.twi.awayday2014.utils.Constants.Parse.TABLE_DATA_TIMESTAMPS;
import static com.twi.awayday2014.utils.Constants.Parse.TABLE_IMAGES;

public abstract class BaseParseDataFetcher<T> implements ParseDataFetcher<T>{
    private static final String TAG = "BaseParseDataFetcher";
    private static final String PARSED_SHARED_PREF = "ParseSharedPref";

    protected List<ParseDataListener> listeners = new ArrayList<ParseDataListener>();
    private Context context;

    public BaseParseDataFetcher(Context context) {
        this.context = context;
    }

    @Override
    public void invalidateAndFetchFreshData() {
        ParseQuery<ParseObject> query = getQuery();
        query.clearCachedResult();
        fetchData();
    }

    @Override
    public void checkDataOutdated() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_DATA_TIMESTAMPS);
        query.whereEqualTo(COL_TABLE, getTable());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject data, ParseException e) {
                if (e == null) {
                    Number changeCounter = data.getNumber(COL_CHANGE_COUNTER);
                    SharedPreferences sharedPreferences = context.getSharedPreferences(PARSED_SHARED_PREF, Context.MODE_PRIVATE);
                    String key = getTable() + "Counter";
                    int currentCounter = sharedPreferences.getInt(key, 1);
                    if(currentCounter != changeCounter.intValue()){
                        Log.d(TAG, getTable() + " data is outdated, refetching it");
                        sharedPreferences.edit().putInt(key, changeCounter.intValue()).commit();
                        for (ParseDataListener listener : listeners) {
                            listener.dataIsOutdated();
                        }
                        invalidateAndFetchFreshData();
                    }else{
                        Log.d(TAG, getTable() + " data is up to date");
                    }
                } else {
                    e.printStackTrace();
                    for (ParseDataListener listener : listeners) {
                        listener.onDataValidationError(ERROR_EXCEPTION_THROWN);
                    }
                }
            }
        });
    }

    protected abstract String getTable();

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

    protected void notifyFetchSource(ParseQuery<ParseObject> query) {
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
    }

    protected void fetchImages(List<String> imageObjectIds, final ImageFetchListener imageFetchListener) {
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

                    imageFetchListener.onImageFetch(images, null);
                }else{
                    imageFetchListener.onImageFetch(null, e);
                }
            }
        });
    }

    protected abstract ParseQuery<ParseObject> getQuery();

    public interface ImageFetchListener{
        void onImageFetch(Map<String, String> images, ParseException e);
    }
}
