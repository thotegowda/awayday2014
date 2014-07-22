package com.twi.awayday2014.models;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.BasicAuthorization;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


import java.util.List;

public class Tweeter {

    private static final String TAG = "AwayDay";
    Twitter twitter;
    private boolean isAuthenticated = false;

    public void authenticate() {
        try {
            //authenticate("awaydayapp2014@gmail.com", "!abcd1234");
            auth("awaydayapp2014", "!abcd1234");
        } catch (TwitterException e) {
            e.printStackTrace();
            Log.e(TAG, "failed with an exception : " + e.getMessage());
            isAuthenticated = false;
        }
    }

    public void authenticate(String userName, String password) throws TwitterException {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("hJs1Fhwwmg3QDMBKX0TgaINuq")
                .setOAuthConsumerSecret("PITREV1N69WxEn7U9c685a661ffjVIdWOgdDNJCUehEw6mCfM3")
                .setOAuthAccessToken("97134656-9tMIEXQosam5TZMIrR7NA2DYIn14dsaSKvIfTlEMU")
                .setOAuthAccessTokenSecret("y1nMS5ricaXRjjNH4kUxpgl9EtppjdkUOM98eYcQ3KWnM");
        twitter = new TwitterFactory(cb.build()).getInstance();
        AccessToken accessToken = twitter.getOAuthAccessToken(userName, password);
        if (null == accessToken) {
            Log.d(TAG, "Failed to log in");
            isAuthenticated = false;

        }
        storeAccessToken((int) twitter.verifyCredentials().getId(), accessToken);
        isAuthenticated = true;
    }

    public void auth(String userName, String pwd) throws TwitterException {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();

        configurationBuilder.setOAuthConsumerKey("hJs1Fhwwmg3QDMBKX0TgaINuq");
        configurationBuilder.setOAuthConsumerSecret("PITREV1N69WxEn7U9c685a661ffjVIdWOgdDNJCUehEw6mCfM3");
        Configuration configuration = configurationBuilder.build();

        Twitter twitter = new TwitterFactory(configuration).getInstance(new BasicAuthorization(userName, pwd));


        AccessToken token = twitter.getOAuthAccessToken();
        System.out.println("Access Token " +token );

        String name = token.getScreenName();
        System.out.println("Screen Name" +name);
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    private static void storeAccessToken(int userId, AccessToken accessToken) {
        Log.d(TAG, "saving access token userId" + userId);
        //store accessToken.getToken()
        //store accessToken.getTokenSecret()
    }

    public void search(String text, SearchResultCallback callback) {
        asyncSearch(text, callback);
    }

    private void asyncSearch(final String term, SearchResultCallback callback) {
        asyncSearch(new Query(term), callback);
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private void asyncSearch(final Query query, final SearchResultCallback callback) {
        new AsyncTask<Void, Void, QueryResult>() {
            @Override
            protected QueryResult doInBackground(Void... voids) {
                return searchTwitter(query);
            }

            @Override
            protected void onPostExecute(QueryResult result) {
                callback.onSearchComplete(result.getTweets());
            }
        }.execute();
    }
    private QueryResult searchTwitter(Query query) {
        QueryResult result = null;
        try {
            result = twitter.search(query);
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return result;
    }

    public interface SearchResultCallback {
        public void onSearchComplete(List<Status> result);
    }
}
