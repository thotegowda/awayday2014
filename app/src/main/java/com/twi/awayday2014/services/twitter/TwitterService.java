package com.twi.awayday2014.services.twitter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.twi.awayday2014.utils.DeveloperKeys;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TwitterService {
    private static final String TAG = "AwayDayTwitter";

    private static final List<Status> EMPTY_STATUS = new ArrayList<Status>();

    private Twitter searchTwitter;
    private TwitterFactory twitterFactory;
    private Twitter twitter;

    private boolean isLoggedIn = false;

    private QueryResult lastQueryResult;
    private RequestToken requestToken;
    private TwitterPreference preference;
    private QueryResult recentQueryResult;

    public TwitterService(TwitterPreference preference) {
        this.preference = preference;

        setup();
    }

    private void setup() {
        if (preference.alreadyLoggedIn()) {
            isLoggedIn = true;

            twitter = new TwitterFactory().getInstance();
            twitter.setOAuthConsumer(DeveloperKeys.TWITTER_CONSUMER_KEY, DeveloperKeys.TWITTER_CONSUMER_SECRET);
            twitter.setOAuthAccessToken(preference.loadAccessToken());
            searchTwitter = twitter;
        } else {
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setOAuthConsumerKey(DeveloperKeys.TWITTER_CONSUMER_KEY);
            cb.setOAuthConsumerSecret(DeveloperKeys.TWITTER_CONSUMER_SECRET);
            Configuration configuration = cb.build();
            twitterFactory = new TwitterFactory(configuration);
            twitter = twitterFactory.getInstance();

            // TODO: This is to show search results even when user is not logged in
            cb = new ConfigurationBuilder();
            cb.setOAuthConsumerKey(DeveloperKeys.TWITTER_CONSUMER_KEY);
            cb.setOAuthConsumerSecret(DeveloperKeys.TWITTER_CONSUMER_SECRET);
            cb.setOAuthAccessToken(DeveloperKeys.TWITTER_ACCESS_KEY);
            cb.setOAuthAccessTokenSecret(DeveloperKeys.TWITTER_ACCESS_SECRET);
            searchTwitter = new TwitterFactory(cb.build()).getInstance();
        }
    }

    public void login(final Context context) throws TwitterException {
        if (!isLoggedIn()) {
            requestToken = getRequestToken();
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
        }
    }

    private RequestToken getRequestToken() throws TwitterException {
        return twitter.getOAuthRequestToken(preference.getCallbackUrl());
    }

    public void retrieveAccessToken(Uri uri) throws TwitterException {
        if (uri != null && uri.toString().startsWith(preference.getCallbackUrl())) {
            AccessToken accessToken = twitter.getOAuthAccessToken(
                    requestToken, uri.getQueryParameter(preference.getAuthVerifier()));
            isLoggedIn = true;
            saveAccessToken(accessToken);
        }
    }

    private void saveAccessToken(AccessToken accessToken) {
        preference.saveAccessToken(accessToken);
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public boolean hasMoreResults() {
        return lastQueryResult != null && lastQueryResult.hasNext();
    }

    public List<Status> search() throws TwitterException {
        recentQueryResult = lastQueryResult = searchTwitter(new Query(preference.getSearchKeyword()));
        return lastQueryResult.getTweets();
    }

    private QueryResult searchTwitter(Query query) throws TwitterException {
       if (preference.shouldUseLocation()) {
           query.setGeoCode(preference.getGeoLocation(), preference.getRadius(), preference.getRadiusUnit());
       }
        return searchTwitter.search(query);
    }

    public List<Status> searchNext() throws TwitterException {
        List<Status> tweets = EMPTY_STATUS;
        if (lastQueryResult != null && lastQueryResult.hasNext()) {
            Query query = lastQueryResult.nextQuery();
            if (query != null) {
                lastQueryResult = searchTwitter(query);
                tweets = lastQueryResult.getTweets();
            }
        }
        return tweets;
    }

    public void tweet(String tweetText) throws TwitterException {
        tweet(new StatusUpdate(appendHashTags(tweetText)));
    }

    public void tweet(String tweetText, File image) throws TwitterException {
        StatusUpdate status = new StatusUpdate(appendHashTags(tweetText));
        status.setMedia(image);
        tweet(status);
    }

    private String appendHashTags(String tweetText) {
        return tweetText + " " + preference.getHashTags();
    }

    public void tweet(StatusUpdate status) throws TwitterException {
        twitter.updateStatus(status);
    }

    public List<Status> getRecentTweets() throws TwitterException {
        Query query = new Query(preference.getSearchKeyword());
        if (recentQueryResult != null) {
            query.setSinceId(recentQueryResult.getMaxId());
        }

        recentQueryResult = lastQueryResult = searchTwitter(query);
        List<Status> tweets = lastQueryResult.getTweets();
        return tweets;
    }

    public boolean isCallbackUrl(Uri uri) {
        return uri.toString().startsWith(preference.getCallbackUrl());
    }

    public TwitterPreference getPreference() {
        return preference;
    }
}
