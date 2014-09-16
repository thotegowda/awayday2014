package com.twi.awayday2014.services.parse;

import android.content.Context;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.twi.awayday2014.utils.Constants.Parse.*;

public class PresenterParseDataFetcher extends BaseParseDataFetcher<Presenter> {
    private static final String TAG = "PresenterParseDataFetcher";
    private List<Presenter> presenters = new ArrayList<Presenter>();
    private boolean isFetching;
    private boolean isDataFetched;

    public PresenterParseDataFetcher(Context context) {
        super(context);
    }

    @Override
    protected String getTable() {
        return TABLE_SPEAKERS;
    }

    @Override
    public void fetchData() {
        if(isFetching){
            Log.d(TAG, "Already fetching data");
            return;
        }
        ParseQuery<ParseObject> query = getQuery();
        Log.d(TAG, "Fetching speakers.");


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
                    List<Presenter> presenters = new ArrayList<Presenter>();
                    List<String> imageObjectIds = new ArrayList<String>();
                    Log.d(TAG, "Number of presenters: " + data.size());
                    if (data.size() == 0) {
                        for (ParseDataListener listener : listeners) {
                            listener.onDataFetchError(ERROR_NO_DATA_FOUND);
                        }
                    }
                    for (ParseObject d : data) {
                        String id = d.getObjectId();
                        String name = d.getString(COL_NAME);
                        String writeUp = d.getString(COL_DESCRIPTION2);
                        String awayDayWriteup = d.getString(COL_AWAYDAY_WRITEUP);
                        String image = d.getString(COL_IMAGE);
                        String link = d.getString(COL_LINKS);
                        boolean isGuest = d.getBoolean(Constants.Parse.COL_IS_GUEST);
                        boolean isListable = d.getBoolean(COL_IS_LISTABLE);
                        int sortOrder = d.getInt(COL_SORT_ORDER);
                        Presenter presenter = new Presenter(id, name, awayDayWriteup, link, writeUp, isGuest, isListable, sortOrder);

                        if(image != null){
                            imageObjectIds.add(image);
                            presenter.setImageId(image);
                        }
                        presenters.add(presenter);
                    }
                    Log.d(TAG, imageObjectIds.size() + " presenters have image");
                    fetchImageUrlAndNotify(presenters, imageObjectIds);
                } else {
                    e.printStackTrace();
                    isFetching = false;
                    for (ParseDataListener listener : listeners) {
                        listener.onDataFetchError(ERROR_EXCEPTION_THROWN);
                    }
                }
            }

            private void fetchImageUrlAndNotify(final List<Presenter> presenters, final List<String> imageObjectIds) {
                if(imageObjectIds.size() == 0){
                    notifyDataFetch(presenters);
                    return;
                }

                fetchImages(imageObjectIds, new ImageFetchListener() {
                    @Override
                    public void onImageFetch(Map<String, String> images, ParseException e) {
                        if(e == null){
                            for (Presenter presenter : presenters) {
                                if(images.containsKey(presenter.getImageId())){
                                    presenter.setImageUrl(images.get(presenter.getImageId()));
                                }
                            }

                            notifyDataFetch(presenters);
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

            private void notifyDataFetch(List<Presenter> presenters) {
                isFetching = false;
                isDataFetched = true;
                PresenterParseDataFetcher.this.presenters = presenters;
                for (ParseDataListener dataListener : listeners) {
                    dataListener.onDataFetched(presenters, true);
                }
            }
        });
    }

    @Override
    protected ParseQuery<ParseObject> getQuery() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_SPEAKERS);
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        return query;
    }

    @Override
    public boolean isDataFetched() {
        return isDataFetched;
    }

    @Override
    public List<Presenter> getFetchedData() {
        return presenters;
    }

    @Override
    public Presenter getDataById(String id) {
        for (Presenter presenter : presenters) {
            if(presenter.getId().equals(id)){
                return presenter;
            }
        }
        return null;
    }

}
