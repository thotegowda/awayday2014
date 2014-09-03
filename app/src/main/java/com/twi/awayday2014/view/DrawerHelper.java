package com.twi.awayday2014.view;

import android.content.Intent;
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

import static com.twi.awayday2014.utils.Constants.DrawerConstants.AGENDA;

public class DrawerHelper {
    private HomeActivity homeActivity;
    private DrawerLayout drawerlayout;
    private ActionBarDrawerToggle drawerToggle;
    private int clickCounter;
    private Handler handler;

    public DrawerHelper(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    public void onCreate(DrawerLayout drawerLayout) {
        this.drawerlayout = drawerLayout;
        setupDrawer();
        setupAdminPage();
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

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                homeActivity.onDrawerSlide(slideOffset);
            }
        };
        drawerlayout.setDrawerListener(drawerToggle);

        setupTextviews();
    }

    private void setupAdminPage() {
        handler = new Handler(){
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
                if(clickCounter == 9){
                    clickCounter = 0;
                    Intent intent = new Intent(homeActivity, PushActivity.class);
                    homeActivity.startActivity(intent);
                }
            }
        });
    }

    private void setupTextviews() {
        TextView agendaText = (TextView) drawerlayout.findViewById(R.id.agendaText);
        agendaText.setTypeface(Fonts.openSansRegular(homeActivity));
        TextView speakers = (TextView) drawerlayout.findViewById(R.id.speakersText);
        speakers.setTypeface(Fonts.openSansRegular(homeActivity));
        TextView breakoutSessions = (TextView) drawerlayout.findViewById(R.id.breakoutSessionsText);
        breakoutSessions.setTypeface(Fonts.openSansRegular(homeActivity));
        TextView videos = (TextView) drawerlayout.findViewById(R.id.videosText);
        videos.setTypeface(Fonts.openSansRegular(homeActivity));
        TextView tags = (TextView) drawerlayout.findViewById(R.id.tagsText);
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

    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item);
    }
}
