package com.twi.awayday2014;

import android.app.Application;
import com.parse.*;

public class AwayDayApplication extends Application {

    private static final String YOUR_APPLICATION_ID = "yiKETlRYko1LrZEgrilwJbp2XHmFeVSpAFkrOGGK";
    private static final String YOUR_CLIENT_KEY = "kR3esD6RWpRixzwKblqQux73nMRVXRqBkprv80rU";

    @Override
    public void onCreate() {
        super.onCreate();

        setupParse();
    }

    private void setupParse() {
        Parse.initialize(getApplicationContext(), YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        PushService.subscribe(this, "AwayDay", HomeActivity.class);

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
