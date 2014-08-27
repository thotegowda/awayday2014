package com.twi.awayday2014.tasks;


import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.twi.awayday2014.services.twitter.TwitterService;

import twitter4j.Status;
import twitter4j.TwitterException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AsyncTweeterTasks {
    private static final String TAG = "awayDayAsyncTweeter";

    private static final List<Status> EMPTY_STATUS = new ArrayList<Status>();

    public interface TwitterCallbacks {
        public void onSearchResults(List<twitter4j.Status> tweets);

        public void onRefresh(List<Status> tweets);
    }

    private final Activity activity;
    private final TwitterService twitterService;
    private final TwitterCallbacks callback;

    private boolean searchInProgress = false;

    public AsyncTweeterTasks(Activity activity, TwitterService tweeter, TwitterCallbacks callback) {
        this.activity = activity;
        this.twitterService = tweeter;
        this.callback = callback;
    }

    public boolean isLoggedIn() {
        return twitterService.isLoggedIn();
    }

    public boolean isSearchInProgress() {
        return searchInProgress;
    }

    public void logIn() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    twitterService.login(activity);
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
                    twitterService.retrieveAccessToken(uri);
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
                    return twitterService.search();
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
        return twitterService.hasMoreResults();
    }

    public void searchNext() {
        if (!twitterService.hasMoreResults()) {
            Log.d("ScrollDebug", "no more results, returning");
            return;
        }

        new AsyncTask<Void, Void, List<Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... voids) {
                try {
                    searchInProgress = true;
                    return twitterService.searchNext();
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
                    return twitterService.getRecentTweets();
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
                    twitterService.tweet(tweetMessage);
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
                    twitterService.tweet(tweetMessage, image);
                } catch (TwitterException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Failed to tweet ex: " + e.getMessage());
                }
                return null;
            }
        }.execute();
    }

    public boolean isCallbackUrl(Uri uri) {
        return twitterService.isCallbackUrl(uri);
    }

}

