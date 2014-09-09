package com.twi.awayday2014.tasks;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.twi.awayday2014.models.ImageSize;
import com.twi.awayday2014.services.twitter.TwitterService;
import com.twi.awayday2014.utils.BitmapUtils;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AsyncTweeterTasks {
    private static final String TAG = "awayDayAsyncTweeter";
    private static final int SUCCESS = 0;
    private static final int ERROR = 1;

    private static final List<Status> EMPTY_STATUS = new ArrayList<Status>();

    public interface TwitterCallbacks {
        public void onSearchResults(List<twitter4j.Status> tweets);

        public void onRefresh(List<Status> tweets);

        public void onUserLoggedIn();

        public void onUserLoggedInFail();

        public void onTweetSuccess();

        public void onTweetFailure();

        public void onGenericError();
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


    public void retrieveAndSaveAccessToken(final String oauthVerifier) {
        new AsyncTask<Void, Void, AccessToken>() {

            @Override
            protected AccessToken doInBackground(Void... voids) {
                try {
                    AccessToken accessToken = twitterService.retrieveAccessToken(oauthVerifier);
                    if(accessToken != null){
                        twitterService.saveAccessToken(accessToken);
                        return accessToken;
                    }
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(AccessToken accessToken) {
                if(accessToken != null){
                    Log.d(TAG, "User login successful");
                    callback.onUserLoggedIn();
                }else {
                    callback.onUserLoggedInFail();
                }
            }
        }.execute();
    }

    public void search() {
        new AsyncTask<Void, Void, List<Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... voids) {
                try {
                    Log.d(TAG, "Searching for tweets");
                    searchInProgress = true;
                    return twitterService.search();
                } catch (TwitterException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Failed to search tweets ex: " + e.getMessage());
                    callback.onGenericError();
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
                    Log.d(TAG, "Fetching old tweets");
                    searchInProgress = true;
                    return twitterService.searchNext();
                } catch (TwitterException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Failed to search next tweets ex: " + e.getMessage());
                    callback.onGenericError();
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
                    Log.d(TAG, "Fetching recent tweets");
                    searchInProgress = true;
                    return twitterService.getRecentTweets();
                } catch (TwitterException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Failed to load recent tweets ex: " + e.getMessage());
                    callback.onGenericError();
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
        new AsyncTask<Void, Void, Integer>() {

            @Override
            protected Integer doInBackground(Void... voids) {
                try {
                    twitterService.tweet(tweetMessage);
                    return SUCCESS;
                } catch (TwitterException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Failed to tweet ex: " + e.getMessage());
                }
                return ERROR;
            }

            @Override
            protected void onPostExecute(Integer status) {
                if(status == SUCCESS){
                    callback.onTweetSuccess();
                }else {
                    callback.onTweetFailure();
                }
            }
        }.execute();

    }

    public void tweet(final String tweetMessage, final Uri imageUri) {
        new AsyncTask<Void, Void, Integer>() {

            @Override
            protected Integer doInBackground(Void... voids) {
                try {
                    String sampledImage = BitmapUtils.sampleImage(getPath(imageUri), 4);
                    Log.d(TAG, "Uploading image with size "
                            + BitmapUtils.decodeBitmapSizeFromUristring(sampledImage).getPredictedImageSizeInMb()
                            + " mb");
                    twitterService.tweet(tweetMessage, new File(sampledImage));
                    return SUCCESS;
                } catch (TwitterException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Failed to tweet ex: " + e.getMessage());
                } catch (IOException e) {
                    Log.d(TAG, "Failed to sample image to smaller size: " + e.getMessage());
                }
                return ERROR;
            }

            public String getPath(Uri uri) {
                if (uri == null) {
                    return null;
                }

                String path = uri.getPath();
                Cursor cursor = null;
                try {
                    String[] projection = {MediaStore.Images.Media.DATA};
                    cursor = activity.getContentResolver().query(uri, projection, null, null, null);
                    if (cursor != null) {
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        path = cursor.getString(column_index);
                        cursor.close();
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                return path;
            }

            @Override
            protected void onPostExecute(Integer status) {
                if(status == SUCCESS){
                    callback.onTweetSuccess();
                }else {
                    callback.onTweetFailure();
                }

            }
        }.execute();
    }

    public boolean isCallbackUrl(Uri uri) {
        return twitterService.isCallbackUrl(uri);
    }

}

