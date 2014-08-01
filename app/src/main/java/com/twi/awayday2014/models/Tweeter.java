package com.twi.awayday2014.models;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import com.twi.awayday2014.DeveloperKeys;
import com.twi.awayday2014.Properties;
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
    public static final List<Status> EMPTY_STATUS = new ArrayList<Status>();

    private Twitter searchTwitter;
    private Twitter twitter;
    private RequestToken requestToken;
    private TwitterFactory twitterFactory;
    private boolean isLoggedIn = false;

    private QueryResult lastQueryResult;
    private Properties properties;

    public Tweeter(Properties properties) {
        this.properties = properties;
        
        setup();
    }

    private void setup() {
        
        if (properties.alreadyLoggedIn()) {
            isLoggedIn = true;

            twitter = new TwitterFactory().getInstance();
            twitter.setOAuthConsumer(DeveloperKeys.TWITTER_CONSUMER_KEY, DeveloperKeys.TWITTER_CONSUMER_SECRET);
            twitter.setOAuthAccessToken(properties.loadAccessToken());
            searchTwitter = twitter;
        } else {
            ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
            configurationBuilder.setOAuthConsumerKey(DeveloperKeys.TWITTER_CONSUMER_KEY);
            configurationBuilder.setOAuthConsumerSecret(DeveloperKeys.TWITTER_CONSUMER_SECRET);
            Configuration configuration = configurationBuilder.build();
            twitterFactory = new TwitterFactory(configuration);
            twitter = twitterFactory.getInstance();

            // This is to show search results even when user is not logged in
            configurationBuilder = new ConfigurationBuilder();
            configurationBuilder.setOAuthConsumerKey(DeveloperKeys.TWITTER_CONSUMER_KEY);
            configurationBuilder.setOAuthConsumerSecret(DeveloperKeys.TWITTER_CONSUMER_SECRET);
            configurationBuilder.setOAuthAccessToken(DeveloperKeys.TWITTER_ACCESS_KEY);
            configurationBuilder.setOAuthAccessTokenSecret(DeveloperKeys.TWITTER_ACCESS_SECRET);
            searchTwitter = new TwitterFactory(configurationBuilder.build()).getInstance();
         }
    }

    public void login(final Context context) throws TwitterException {
        if (!isTwitterLoggedInAlready()) {
            requestToken = getRequestToken();
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
        }
    }

    private RequestToken getRequestToken() throws TwitterException {
        return twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
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
        properties.saveAccessToken(accessToken);
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
