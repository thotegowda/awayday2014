package com.twi.awayday2014.components.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.utils.Fonts;
import com.twi.awayday2014.R;
import com.twi.awayday2014.adapters.NotificationsAdapter;

public class NotificationsActivity extends Activity {

    private ListView notificationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        View rootLayout = findViewById(R.id.notificationsRootLayout);
        AwayDayApplication awayDayApplication = (AwayDayApplication) getApplication();
        Bitmap background = awayDayApplication.getHomeActivityScreenshot();
        if (background != null) {
            rootLayout.setBackground(new BitmapDrawable(getResources(), background));
        }

        setupListView();
        setupHeader();
    }

    private void setupHeader() {
        TextView headerText = (TextView) findViewById(R.id.notificationsHeaderText);
        headerText.setTypeface(Fonts.openSansRegular(this));
    }

    private void setupListView() {
        notificationsList = (ListView) findViewById(R.id.notificationsList);
        notificationsList.setAdapter(new NotificationsAdapter(this));
    }
}
