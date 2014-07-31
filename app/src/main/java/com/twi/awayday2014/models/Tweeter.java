package com.twi.awayday2014.models;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import com.twi.awayday2014.DeveloperKeys;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


import java.util.ArrayList;
import java.util.List;

public class Tweeter {
    private static final String TAG = "AwayDay";

    public static final String TWITTER_CALLBACK_URL = "oauth://thoughtworks.Twitter_oAuth";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";

    private final Twitter searchTwitter;

    public static final List<Status> EMPTY_STATUS = new ArrayList<Status>();
    private Twitter twitter;
    private RequestToken requestToken;
    private TwitterFactory twitterFactory;
    private boolean isLoggedIn = false;

    private QueryResult lastQueryResult;

    public Tweeter() {
        // TODO : remove one twitter
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(DeveloperKeys.TWITTER_CONSUMER_KEY);
        configurationBuilder.setOAuthConsumerSecret(DeveloperKeys.TWITTER_CONSUMER_SECRET);
        Configuration configuration = configurationBuilder.build();
        twitterFactory = new TwitterFactory(configuration);
        twitter = twitterFactory.getInstance();

        configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(DeveloperKeys.TWITTER_CONSUMER_KEY);
        configurationBuilder.setOAuthConsumerSecret(DeveloperKeys.TWITTER_CONSUMER_SECRET);
        configurationBuilder.setOAuthAccessToken(DeveloperKeys.TWITTER_ACCESS_KEY);
        configurationBuilder.setOAuthAccessTokenSecret(DeveloperKeys.TWITTER_ACCESS_SECRET);
        configuration = configurationBuilder.build();
        searchTwitter = new TwitterFactory(configuration).getInstance();

    }

    public void login(final Context context) throws TwitterException {
        if (!isTwitterLoggedInAlready()) {
            getAccessToken();
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
        } else {
            Toast.makeText(context, "Already Logged into twitter", Toast.LENGTH_LONG).show();
        }
    }

    private void getAccessToken() throws TwitterException {
        requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
        Log.d(TAG, "Request token: " + requestToken.getToken());
        Log.d(TAG, "Request token secret: " + requestToken.getTokenSecret());
    }

    public void retrieveAccessToken(Uri uri) throws TwitterException {
        if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
            AccessToken accessToken = twitter.getOAuthAccessToken(requestToken,
                    uri.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER));

            isLoggedIn = true;
            saveAccessToken(accessToken);
        }
    }

    private void saveAccessToken(AccessToken accessToken) {
        Log.d(TAG, "Access token: " + accessToken.getToken());
        Log.d(TAG, "Access token secret: " + accessToken.getTokenSecret());
    }

    public boolean isTwitterLoggedInAlready() {
        return isLoggedIn;
    }


    public List<Status> search(String searchTerm) {
        try {
            lastQueryResult = searchTwitter(new Query(searchTerm));
            return lastQueryResult.getTweets();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return EMPTY_STATUS;
    }

    public boolean hasMoreResults() {
        return lastQueryResult != null;
    }

    public List<Status> searchNext() {
        List<Status> tweets =  EMPTY_STATUS;
        try {

            if (lastQueryResult != null && lastQueryResult.hasNext()) {
                Query query = lastQueryResult.nextQuery();
                if (query != null) {

                    lastQueryResult = searchTwitter(query);
                    tweets = lastQueryResult.getTweets();

                }
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return tweets;
    }

    private QueryResult searchTwitter(Query query) throws TwitterException {
        return searchTwitter.search(query);
    }

    public void tweet(String tweetText) {
        try {
            twitter.updateStatus(tweetText);
        } catch (TwitterException e) {
            e.printStackTrace();
        }

    }

    public List<Status> getRecentTweets(String searchTerm) {
        try {
            Query query = new Query(searchTerm);
            query.setSinceId(lastQueryResult.getSinceId());
            lastQueryResult = searchTwitter(query);
            return lastQueryResult.getTweets();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return EMPTY_STATUS;
    }
}
