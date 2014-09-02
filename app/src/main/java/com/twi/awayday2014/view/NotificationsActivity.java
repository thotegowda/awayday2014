package com.twi.awayday2014.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.twi.awayday2014.PushNotificationReceiver;
import com.twi.awayday2014.R;
import com.twi.awayday2014.adapters.NotificationsAdapter;
import com.twi.awayday2014.models.AwayDayNotification;
import com.twi.awayday2014.utils.Fonts;

import java.util.Collections;
import java.util.List;

import static com.twi.awayday2014.PushNotificationReceiver.NOTIFICATION_RECEIVED;
import static com.twi.awayday2014.utils.Constants.Notifications.PREVIEW_MODE;
import static com.twi.awayday2014.utils.Constants.Notifications.PREVIEW_NOTIFICATION;
import static java.util.Arrays.asList;

public class NotificationsActivity extends Activity{
    private static final String TAG = "NotificationsActivity";
    private ListView notificationsList;
    private TextView noNotificationsText;
    private NotificationsAdapter notificationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        setupListView();
        setupHeader();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(NOTIFICATION_RECEIVED));
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<AwayDayNotification> awayDayNotifications = AwayDayNotification.listAll(AwayDayNotification.class);
            if(awayDayNotifications.size() > 0 && notificationsAdapter != null){
                notificationsList.setVisibility(View.VISIBLE);
                noNotificationsText.setVisibility(View.GONE);
                Collections.sort(awayDayNotifications, Collections.reverseOrder(new AwayDayNotification.NotificatonsComparator()));
                notificationsAdapter.dataSetChanged(awayDayNotifications);
            }else{
                notificationsList.setVisibility(View.GONE);
                noNotificationsText.setVisibility(View.VISIBLE);
            }
        }
    };


    private void setupHeader() {
        TextView headerText = (TextView) findViewById(R.id.notificationsHeaderText);
        headerText.setTypeface(Fonts.openSansRegular(this));
    }

    private void setupListView() {
        notificationsList = (ListView) findViewById(R.id.notificationsList);
        noNotificationsText = (TextView) findViewById(R.id.noNotificationsText);
        noNotificationsText.setTypeface(Fonts.openSansLight(this));
        Intent intent = getIntent();
        boolean previewMode = intent.getBooleanExtra(PREVIEW_MODE, false);
        List<AwayDayNotification> awayDayNotifications = AwayDayNotification.listAll(AwayDayNotification.class);
        Collections.sort(awayDayNotifications, Collections.reverseOrder(new AwayDayNotification.NotificatonsComparator()));
        if(previewMode){
            List<AwayDayNotification> previewNotification = asList((AwayDayNotification)intent.getExtras().getParcelable(PREVIEW_NOTIFICATION));
            notificationsList.setAdapter(new NotificationsAdapter(this, previewNotification));
        }else{
            notificationsAdapter = new NotificationsAdapter(this, awayDayNotifications);
            notificationsList.setAdapter(notificationsAdapter);
            if(awayDayNotifications.size() == 0) {
                notificationsList.setVisibility(View.GONE);
                noNotificationsText.setVisibility(View.VISIBLE);
            }
        }
    }
}
