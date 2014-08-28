package com.twi.awayday2014.service.twitter;


import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AsyncTweeter {
    private static final String TAG = "awayDayAsyncTweeter";

    private static final List<Status> EMPTY_STATUS = new ArrayList<Status>();

    public interface TwitterCallbacks {
        public void onSearchResults(List<twitter4j.Status> tweets);
        public void onRefresh(List<Status> tweets);
    }

    private final Activity activity;
    private final Tweeter tweeter;
    private final TwitterCallbacks callback;

    private boolean searchInProgress = false;

    public AsyncTweeter(Activity activity, Tweeter tweeter, TwitterCallbacks callback) {
        this.activity = activity;
        this.tweeter = tweeter;
        this.callback = callback;
    }

    public boolean isLoggedIn() {
        return tweeter.isLoggedIn();
    }

    public boolean isSearchInProgress() {
        return searchInProgress;
    }

    public void logIn() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    tweeter.login(activity);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

    }

    public void onCallbackUrlInvoked(final Uri uri) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    tweeter.retrieveAccessToken(uri);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

    }

    public void search() {
        new AsyncTask<Void, Void, List<Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... voids) {
                try {
                    searchInProgress = true;
                    return tweeter.search();
                } catch (TwitterException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Failed to search tweets ex: " + e.getMessage());
                }
                return EMPTY_STATUS;
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> tweets) {
                callback.onSearchResults(tweets);
                searchInProgress = false;
            }
        }.execute();
    }

    public boolean hasMoreTweets() {
        return tweeter.hasMoreResults();
    }

    public void searchNext() {
        if (!tweeter.hasMoreResults()) {
            Log.d("ScrollDebug", "no more results, returning");
            return;
        }

        new AsyncTask<Void, Void, List<Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... voids) {
                try {
                    searchInProgress = true;
                    return tweeter.searchNext();
                } catch (TwitterException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Failed to search next tweets ex: " + e.getMessage());
                    Log.d("ScrollDebug", "Failed to search next tweets ex: " + e.getMessage());
                }
                return EMPTY_STATUS;
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> tweets) {
                callback.onSearchResults(tweets);
                searchInProgress = false;
            }
        }.execute();
    }

    public void refresh() {
        new AsyncTask<Void, Void, List<Status>>() {

            @Override
            protected List<twitter4j.Status> doInBackground(Void... voids) {
                try {
                    searchInProgress = true;
                    return tweeter.getRecentTweets();
                } catch (TwitterException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Failed to load recent tweets ex: " + e.getMessage());
                }
                return EMPTY_STATUS;
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> tweets) {
                callback.onRefresh(tweets);
                searchInProgress = false;
            }
        }.execute();
    }

    public void tweet(final String tweetMessage) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    tweeter.tweet(tweetMessage);
                } catch (TwitterException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Failed to tweet ex: " + e.getMessage());
                }
                return null;
            }
        }.execute();

    }

    public void tweet(final String tweetMessage, final File image) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    tweeter.tweet(tweetMessage, image);
                } catch (TwitterException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Failed to tweet ex: " + e.getMessage());
                }
                return null;
            }
        }.execute();
    }

    public boolean isCallbackUrl(Uri uri) {
        return tweeter.isCallbackUrl(uri);
    }

}
