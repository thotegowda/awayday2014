package com.twi.awayday2014.models;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.twi.awayday2014.adapters.TweetsAdapter;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.BasicAuthorization;
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

    private String CONSUMER_KEY = "CyqRuU77Lbyv7i9EOkDMGinSo";
    private String CONSUMER_SECRET = "h2ElL2l13ANJG5qYCUqGacL1uGNnPKkA6mfJmWwnofOI8w6bUb";
    private String ACCESS_KEY = "97134656-DGmVyE2Npqw5AEx6tI4r8KY2pEFBcsWkN74YnkOOX";
    private String ACCESS_SECRET = "qoVv6SRRFHQF2CRZ0t66xTUCf3L78aY4402qjOlTmJpRp";

    public static final List<Status> EMPTY_STATUS = new ArrayList<Status>();
    private Twitter twitter;
    private RequestToken requestToken;
    private TwitterFactory twitterFactory;
    private boolean isLoggedIn = false;

    private QueryResult lastQueryResult;

    public Tweeter() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(CONSUMER_KEY);
        configurationBuilder.setOAuthConsumerSecret(CONSUMER_SECRET);
        Configuration configuration = configurationBuilder.build();
        twitterFactory = new TwitterFactory(configuration);
        twitter = twitterFactory.getInstance();

        configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(CONSUMER_KEY);
        configurationBuilder.setOAuthConsumerSecret(CONSUMER_SECRET);
        configurationBuilder.setOAuthAccessToken(ACCESS_KEY);
        configurationBuilder.setOAuthAccessTokenSecret(ACCESS_SECRET);
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

    public List<Status> searchNext() {
        try {
            lastQueryResult = searchTwitter(lastQueryResult.nextQuery());
            return lastQueryResult.getTweets();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return EMPTY_STATUS;
    }

    private QueryResult searchTwitter(Query query) throws TwitterException {
        return searchTwitter.search(query);
    }

    public void tweet() {

    }
}
