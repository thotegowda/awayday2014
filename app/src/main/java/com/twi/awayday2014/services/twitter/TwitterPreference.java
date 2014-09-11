package com.twi.awayday2014.services.twitter;


import android.content.Context;
import android.content.SharedPreferences;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.auth.AccessToken;

public class TwitterPreference {

    private static final String SHARED_PREFERENCE = "awayday2014_preference_twitter";
    private static final String PREFERENCE_IS_TWITTER_LOGGED_IN = "twitter_user_logged_in_key";

    private static final String TWITTER_ACCESS_TOKEN = "twitter_access_token_key";
    private static final String TWITTER_ACCESS_SECRET = "twitter_access_secret_key";

    public static final String TWITTER_CALLBACK_URL = "oauth://thoughtworks.Twitter_oAuth";
    public static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";

    public static final String HASH_TAG_TEST = "awaydayTest";
    public static final String HASH_TAG = "#indiaawayday";

    private static final GeoLocation MARRIOTT_HOTEL = new GeoLocation(17.4243185, 78.4870042);

    private static final int RADIUS_AROUND_DISTANCE = 50;

    private Context context;

    public TwitterPreference(Context context) {
        this.context = context;
    }

    public boolean alreadyLoggedIn() {
        return getSharedPreferences().getBoolean(PREFERENCE_IS_TWITTER_LOGGED_IN, false);
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
    }

    public void saveAccessToken(AccessToken accessToken) {
        SharedPreferences.Editor edit = getSharedPreferences().edit();
        edit.putBoolean(PREFERENCE_IS_TWITTER_LOGGED_IN, true);
        edit.putString(TWITTER_ACCESS_TOKEN, accessToken.getToken());
        edit.putString(TWITTER_ACCESS_SECRET, accessToken.getTokenSecret());
        edit.commit();
    }

    public AccessToken loadAccessToken() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        String token = sharedPreferences.getString(TWITTER_ACCESS_TOKEN, "");
        String secret = sharedPreferences.getString(TWITTER_ACCESS_SECRET, "");
        if (token.length() > 0 && secret.length() > 0) {
            return new AccessToken(token, secret);
        }
        return null;
    }

    public boolean shouldUseLocation() {
        return true;
    }

    public GeoLocation getGeoLocation() {
       return MARRIOTT_HOTEL;
    }

    public int getRadius() {
        return RADIUS_AROUND_DISTANCE;
    }

    public Query.Unit getRadiusUnit() {
        return Query.Unit.km;
    }

    public String getCallbackUrl() {
        return TWITTER_CALLBACK_URL;
    }

    public String getAuthVerifier() {
        return URL_TWITTER_OAUTH_VERIFIER;
    }

    public String getSearchKeyword() {
        return HASH_TAG;
    }

    public String getHashTags() {
        return HASH_TAG;
    }

    private String flatten(String[] values) {
        String stringValues = "";
        for (String value : values) {
            stringValues += " " + value;
        }
        return stringValues;
    }
}
