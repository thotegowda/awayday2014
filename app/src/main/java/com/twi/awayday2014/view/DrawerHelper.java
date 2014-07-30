package com.twi.awayday2014.view;

import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.twi.awayday2014.R;
import com.twi.awayday2014.utils.Fonts;

import static com.twi.awayday2014.utils.Constants.DrawerConstants.AGENDA;

public class DrawerHelper {
    private HomeActivity homeActivity;
    private DrawerLayout drawerlayout;
    private ActionBarDrawerToggle drawerToggle;

    public DrawerHelper(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    public void onCreate(DrawerLayout drawerLayout) {
        this.drawerlayout = drawerLayout;
        setupDrawer();
        homeActivity.onDrawerItemClick(AGENDA);
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
        };
        drawerlayout.setDrawerListener(drawerToggle);

        TextView agendaText = (TextView) drawerlayout.findViewById(R.id.agenda);
        agendaText.setTypeface(Fonts.openSansRegular(homeActivity));
        TextView myScheduleText = (TextView) drawerlayout.findViewById(R.id.mySchedule);
        myScheduleText.setTypeface(Fonts.openSansRegular(homeActivity));
        TextView speakers = (TextView) drawerlayout.findViewById(R.id.speakers);
        speakers.setTypeface(Fonts.openSansRegular(homeActivity));
        TextView breakoutSessions = (TextView) drawerlayout.findViewById(R.id.breakoutSessions);
        breakoutSessions.setTypeface(Fonts.openSansRegular(homeActivity));
        TextView videos = (TextView) drawerlayout.findViewById(R.id.videos);
        videos.setTypeface(Fonts.openSansRegular(homeActivity));
        TextView tags = (TextView) drawerlayout.findViewById(R.id.tags);
        tags.setTypeface(Fonts.openSansRegular(homeActivity));
        TextView countryText = (TextView) drawerlayout.findViewById(R.id.countryYearText);
        countryText.setTypeface(Fonts.openSansLight(homeActivity));
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
}
