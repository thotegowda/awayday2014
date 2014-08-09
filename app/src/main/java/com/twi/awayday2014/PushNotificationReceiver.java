package com.twi.awayday2014;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.twi.awayday2014.models.ShortNotification;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class PushNotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "PushNotification";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getAction();
            String channel = intent.getExtras().getString("com.parse.Channel");
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            Log.d(TAG, "got action " + action + " on channel " + channel + " with:");

            Iterator itr = json.keys();
            String name = "";
            String message = "";
            while (itr.hasNext()) {
                String key = (String) itr.next();
                if (key.equalsIgnoreCase("name")) {
                    name = json.getString(key);
                } else if (key.equalsIgnoreCase("message")) {
                    message = json.getString(key);
                }
                Log.d(TAG, "..." + key + " => " + json.getString(key));
            }

           new ShortNotification(message, name).save();

        } catch (JSONException e) {
            Log.d(TAG, "JSONException: " + e.getMessage());
        }
    }
}
