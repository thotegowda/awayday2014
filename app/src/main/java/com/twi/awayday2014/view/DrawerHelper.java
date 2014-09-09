package com.twi.awayday2014.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.twi.awayday2014.R;
import com.twi.awayday2014.utils.Fonts;

public class DrawerHelper {
    public static final String ADMIN_SETTINGS = "admin_settings";
    public static final String CAN_SEND_NOTIFICATION = "can_send_notification";
    private HomeActivity homeActivity;
    private DrawerLayout drawerlayout;
    private ActionBarDrawerToggle drawerToggle;
    private int clickCounter;
    private Handler handler;
    private View[] selections;

    private int[] resourceIds = {
      R.drawable.ic_agenda,
      R.drawable.ic_speakers,
      R.drawable.ic_sessions
    };
    private TextView sendNotificationView;

    public DrawerHelper(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    public void onCreate(DrawerLayout drawerLayout) {
        this.drawerlayout = drawerLayout;
        setupDrawer();
        setupAdminPage();

        onNavigationItemSelect(HomeActivity.AGENDA_FRAGMENT);
    }

    private void setupDrawer() {
        drawerlayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerToggle = new ActionBarDrawerToggle(
                homeActivity,
                drawerlayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                homeActivity.onDrawerClosed();
            }

            public void onDrawerOpened(View drawerView) {
                homeActivity.onDrawerOpen();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                homeActivity.onDrawerSlide(slideOffset);
            }
        };
        drawerlayout.setDrawerListener(drawerToggle);

        setupTextViews();

        if (isAuthorizedToSendPushNotifications(homeActivity)) {
            showSendNotificationView();
        }
    }

    private void setupAdminPage() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                clickCounter = 0;
            }
        };
        View drawerImage = drawerlayout.findViewById(R.id.drawerImage);
        drawerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCounter += 1;
                handler.removeMessages(0);
                handler.sendEmptyMessageDelayed(0, 400);
                if (clickCounter == 9) {
                    clickCounter = 0;
                    rememberAdmin(homeActivity);
                    showSendNotificationView();
                }
            }
        });
    }

    private void setupTextViews() {
        selections = new View[]{
                drawerlayout.findViewById(R.id.select_agenda),
                drawerlayout.findViewById(R.id.select_speakers),
                drawerlayout.findViewById(R.id.select_breakout)
        };

        TextView agendaText = (TextView) drawerlayout.findViewById(R.id.agendaText);
        agendaText.setTypeface(Fonts.openSansRegular(homeActivity));
        agendaText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNavigationItemSelect(HomeActivity.AGENDA_FRAGMENT);
            }
        });

        TextView speakers = (TextView) drawerlayout.findViewById(R.id.speakersText);
        speakers.setTypeface(Fonts.openSansRegular(homeActivity));
        speakers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNavigationItemSelect(HomeActivity.SPEAKERS_FRAGMENT);
            }
        });

        TextView breakoutSessions = (TextView) drawerlayout.findViewById(R.id.breakoutSessionsText);
        breakoutSessions.setTypeface(Fonts.openSansRegular(homeActivity));
        breakoutSessions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNavigationItemSelect(HomeActivity.BREAKOUT_FRAGMENT);
            }
        });

//        TextView videos = (TextView) drawerlayout.findViewById(R.id.videosText);
//        videos.setTypeface(Fonts.openSansRegular(homeActivity));
//        videos.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onNavigationItemSelect(HomeActivity.VIDEOS_FRAGMENT);
//            }
//        });
//
//        TextView tags = (TextView) drawerlayout.findViewById(R.id.tagsText);
//        tags.setTypeface(Fonts.openSansRegular(homeActivity));
//        tags.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onNavigationItemSelect(HomeActivity.TAG_FRAGMENT);
//            }
//        });

        sendNotificationView = (TextView) drawerlayout.findViewById(R.id.btn_send_notification);
        sendNotificationView.setTypeface(Fonts.openSansRegular(homeActivity));
        sendNotificationView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startPushActivity();
            }
        });

        TextView countryText = (TextView) drawerlayout.findViewById(R.id.countryYearText);
        countryText.setTypeface(Fonts.openSansRegular(homeActivity));
    }

    private void onNavigationItemSelect(int fragment) {
        homeActivity.onNavigationItemSelected(fragment, resourceIds[fragment - 1]);
        resetIndicators();
        selections[fragment - 1].setVisibility(View.VISIBLE);
    }

    private void resetIndicators() {
        for (View view : selections) {
          view.setVisibility(View.GONE);
        }
    }

    public void onPostCreate() {
        drawerToggle.syncState();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean isDrawerOpen() {
        return drawerlayout.isDrawerOpen(Gravity.LEFT);
    }

    public void closeDrawer() {
        drawerlayout.closeDrawer(Gravity.LEFT);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item);
    }

    public void rememberAdmin(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(ADMIN_SETTINGS, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(CAN_SEND_NOTIFICATION, true);
        editor.commit();
    }

    public boolean isAuthorizedToSendPushNotifications(Activity activity) {
        return activity.getSharedPreferences(ADMIN_SETTINGS, 0).getBoolean(CAN_SEND_NOTIFICATION, false);
    }

    private void startPushActivity() {
        Intent intent = new Intent(homeActivity, PushActivity.class);
        homeActivity.startActivity(intent);
    }

    private void showSendNotificationView() {
        sendNotificationView.setVisibility(View.VISIBLE);
    }
}
