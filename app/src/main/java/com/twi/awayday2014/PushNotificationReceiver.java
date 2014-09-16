package com.twi.awayday2014;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.twi.awayday2014.models.AwayDayNotification;
import com.twi.awayday2014.models.NotificationType;
import com.twi.awayday2014.view.NotificationsActivity;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PushNotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "PushNotification";
    public static final String NOTIFICATION_RECEIVED = "notificationReceived";

    @Override
    public void onReceive(final Context context, Intent intent) {
        try {
            String action = intent.getAction();
            String channel = intent.getExtras().getString("com.parse.Channel");
            JSONObject json = null;
            json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            Log.d(TAG, "got action " + action + " on channel " + channel + " with:");

            Iterator itr = json.keys();
            String title = "";
            String message = "";
            NotificationType type = null;
            while (itr.hasNext()) {
                String key = (String) itr.next();
                if (key.equalsIgnoreCase("heading")) {
                    title = json.getString(key);
                } else if (key.equalsIgnoreCase("message")) {
                    message = json.getString(key);
                } else if (key.equalsIgnoreCase("type")) {
                    type = NotificationType.fromDisplayText(json.getString(key));
                }
                Log.d(TAG, "..." + key + " => " + json.getString(key));
            }

            final AwayDayNotification awayDayNotification = new AwayDayNotification(title, DateTime.now(), message, type);

            saveInBackground(context, awayDayNotification);
            showNotification(awayDayNotification, context);

        } catch (JSONException e) {
            Log.d(TAG, "JSONException: " + e.getMessage());
        }
    }

    private void saveInBackground(final Context context, final AwayDayNotification awayDayNotification) {
        new AsyncTask<Void, Void, AwayDayNotification>(){
            @Override
            protected AwayDayNotification doInBackground(Void... params) {
                List<AwayDayNotification> awayDayNotifications = AwayDayNotification.listAll(AwayDayNotification.class);
                Collections.sort(awayDayNotifications, Collections.reverseOrder(new AwayDayNotification.NotificatonsComparator()));

                //don't save notification if you have already received it more then once
                if(awayDayNotifications.size() > 0 && awayDayNotifications.get(0).getDescription().equals(awayDayNotification.getDescription())){
                    Log.e(TAG, "Notification received multiple times, not saving it");
                    return null;
                }

                awayDayNotification.save();
                return awayDayNotification;
            }

            @Override
            protected void onPostExecute(AwayDayNotification notification) {
                if(notification != null){
                    notifyActivity(context);
                }
            }
        }.execute();
    }

    private void notifyActivity(Context context) {
        Intent intent = new Intent(NOTIFICATION_RECEIVED);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void showNotification(AwayDayNotification awayDayNotification, Context context) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(awayDayNotification.getTitle())
                        .setContentText(awayDayNotification.getDescription())
                        .setAutoCancel(true);
        Intent resultIntent = new Intent(context, NotificationsActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(NotificationsActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
