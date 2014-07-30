package com.twi.awayday2014.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.*;

import com.twi.awayday2014.R;
import com.twi.awayday2014.utils.Constants;
import com.twi.awayday2014.view.custom.KenBurnsView;
import com.twi.awayday2014.view.fragments.*;
import com.twi.awayday2014.models.Tweeter;

import static com.twi.awayday2014.utils.Constants.DrawerConstants.AGENDA;

public class HomeActivity extends Activity {
    private DrawerHelper drawerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerHelper = new DrawerHelper(this);
        drawerHelper.onCreate((DrawerLayout) findViewById(R.id.drawer_layout));

        setupActionbar();
        setupHeader();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerHelper.onPostCreate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerHelper.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawerHelper.isDrawerOpen()) {
            drawerHelper.closeDrawer();
        }
    }

    public void onDrawerOpen() {
        getActionBar().setTitle("Tada");
        invalidateOptionsMenu();
    }

    public void onDrawerClosed() {
        getActionBar().setTitle("Tada");
        invalidateOptionsMenu();
    }


    public void onDrawerItemClick(int itemId) {
        Fragment fragment = getFragment(itemId);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);

        if (fragmentManager.getBackStackEntryCount() <= 1) {
            transaction.addToBackStack(null);
        } else {
            fragmentManager.popBackStack();
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    private void setupHeader() {
        KenBurnsView headerPicture = (KenBurnsView) findViewById(R.id.header_picture);
        headerPicture.setResourceIds(R.drawable.picture0, R.drawable.picture1);
        headerPicture.setAlpha(.5f);
    }

    private void setupActionbar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }


    private Fragment getFragment(int position) {
        Fragment fragment = getFragmentManager().findFragmentById(position);
        if (fragment == null) {
            fragment = createFragment(position);
        }
        return fragment;
    }

    private Fragment createFragment(int position) {
        switch (position) {
            case AGENDA:
                return AgendaFragment.newInstance(position);
            default:
                return AgendaFragment.newInstance(position);
        }
    }
}
