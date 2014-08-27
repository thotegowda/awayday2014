package com.twi.awayday2014.ui;

import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.twi.awayday2014.Fonts;
import com.twi.awayday2014.R;


public class DrawerHelper {
    private NewHomeActivity newHomeActivity;
    private DrawerLayout drawerlayout;
    private ActionBarDrawerToggle drawerToggle;

    public DrawerHelper(NewHomeActivity newHomeActivity) {
        this.newHomeActivity = newHomeActivity;
    }

    public void onCreate(DrawerLayout drawerLayout) {
        this.drawerlayout = drawerLayout;
        setupDrawer();
        newHomeActivity.onDrawerItemClick(NewHomeActivity.AGENDA_FRAGMENT);
    }

    private void setupDrawer() {
        drawerlayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerToggle = new ActionBarDrawerToggle(
                newHomeActivity,
                drawerlayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                newHomeActivity.onDrawerClosed();
            }

            public void onDrawerOpened(View drawerView) {
                newHomeActivity.onDrawerOpen();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                newHomeActivity.onDrawerSlide(slideOffset);
            }
        };
        drawerlayout.setDrawerListener(drawerToggle);

        setupTextViews();
    }

    private void setupTextViews() {
        TextView agendaText = (TextView) drawerlayout.findViewById(R.id.agenda);
        agendaText.setTypeface(Fonts.openSansRegular(newHomeActivity));

        TextView myScheduleText = (TextView) drawerlayout.findViewById(R.id.mySchedule);
        myScheduleText.setTypeface(Fonts.openSansRegular(newHomeActivity));
        myScheduleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newHomeActivity.onNavigationItemSelected(NewHomeActivity.MY_SCHEDULE_FRAGMENT);
            }
        });

        TextView speakers = (TextView) drawerlayout.findViewById(R.id.speakers);
        speakers.setTypeface(Fonts.openSansRegular(newHomeActivity));
        speakers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newHomeActivity.onNavigationItemSelected(NewHomeActivity.SPEAKERS_FRAGMENT);
            }
        });

        TextView breakoutSessions = (TextView) drawerlayout.findViewById(R.id.breakoutSessions);
        breakoutSessions.setTypeface(Fonts.openSansRegular(newHomeActivity));
        breakoutSessions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newHomeActivity.onNavigationItemSelected(NewHomeActivity.BREAKOUT_FRAGMENT);
            }
        });

        TextView videos = (TextView) drawerlayout.findViewById(R.id.videos);
        videos.setTypeface(Fonts.openSansRegular(newHomeActivity));
        videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newHomeActivity.onNavigationItemSelected(NewHomeActivity.VIDEOS_FRAGMENT);
            }
        });

        TextView tags = (TextView) drawerlayout.findViewById(R.id.tags);
        tags.setTypeface(Fonts.openSansRegular(newHomeActivity));
        tags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        TextView countryText = (TextView) drawerlayout.findViewById(R.id.countryYearText);
        countryText.setTypeface(Fonts.openSansLight(newHomeActivity));
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
}
