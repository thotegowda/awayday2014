package com.twi.awayday2014;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.twi.awayday2014.fragments.*;
import com.twi.awayday2014.models.Tweeter;

public class HomeActivity extends Activity {

    private static final int HOME_FRAGMENT = 0;
    private static final int AGENDA_FRAGMENT = 1;
    private static final int SPEAKERS_FRAGMENT = 2;
    private static final int BREAKOUT_FRAGMENT = 3;
    private static final int MY_SCHEDULE_FRAGMENT = 4;
    private static final int VIDEOS_FRAGMENT = 5;
    private static final int SOCIALIZE_FRAGMENT = 6;
    private static final int MAP_FRAGMENT = 7;

    private String mTitle;
    private String mDrawerTitle;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private static final Tweeter tweeter = new Tweeter();

    public static Tweeter getTweeter() {
        return tweeter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTitle = mDrawerTitle = (String) getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.navigation_array);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.navigation_item, mPlanetTitles));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(Tweeter.TWITTER_CALLBACK_URL)) {
            selectItem(SOCIALIZE_FRAGMENT);
        } else if (savedInstanceState == null) {
            selectItem(HOME_FRAGMENT);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        startFragment(position);

        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void startFragment(int position) {
        Fragment fragment = getFragment(position);
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

    @Override
    public void setTitle(CharSequence title) {
        mTitle = (String) title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 1) {
                fragmentManager.popBackStack();
            } else {
                fragmentManager.popBackStack();
                super.onBackPressed();
            }
        }
    }

    public Fragment getFragment(int position) {
        Fragment fragment = getFragmentManager().findFragmentById(position);
        if (fragment == null) {
            fragment = createFragment(position);
        }
        return fragment;
    }

    public Fragment createFragment(int position) {
        switch (position) {
            default:
            case HOME_FRAGMENT:
                return HomeFragment.newInstance(position);
            case AGENDA_FRAGMENT:
                return AgendaFragment.newInstance(position);
            case SPEAKERS_FRAGMENT:
                return SpeakersFragment.newInstance(position);
            case BREAKOUT_FRAGMENT:
                return BreakoutFragment.newInstance(position);
            case MY_SCHEDULE_FRAGMENT:
                return MyScheduleFragment.newInstance(position);
            case VIDEOS_FRAGMENT:
                return VideosFragment.newInstance(position);
            case SOCIALIZE_FRAGMENT:
                return SocializeFragment.newInstance(position);
            case MAP_FRAGMENT:
                return MapFragment.newInstance(position);
        }
    }
}
