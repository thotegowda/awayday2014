package com.twi.awayday2014;


import android.content.Context;
import android.content.SharedPreferences;
import twitter4j.auth.AccessToken;

public class Properties {

    private static final String SHARED_PREFERENCE = "awayday2014_preference";
    private static final String PREFERENCE_LOGGED_IN = "twitter_user_logged_in_key";
    private static final String TWITTER_ACCESS_TOKEN = "twitter_access_token_key";
    private static final String TWITTER_ACCESS_SECRET = "twitter_access_secret_key";

    private Context context;

    public Properties(Context context) {
        this.context = context;
    }

    public boolean alreadyLoggedIn() {
        return getSharedPreferences().getBoolean(PREFERENCE_LOGGED_IN, false);
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
    }

    public void saveAccessToken(AccessToken accessToken) {
        SharedPreferences.Editor edit = getSharedPreferences().edit();
        edit.putBoolean(PREFERENCE_LOGGED_IN, true);
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
}
