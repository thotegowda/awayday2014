package com.twi.awayday2014.view;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.BuildConfig;
import com.twi.awayday2014.R;
import com.twi.awayday2014.services.ParseDataService;
import com.twi.awayday2014.utils.Fonts;
import com.twi.awayday2014.utils.OsUtils;
import com.twi.awayday2014.view.custom.KenBurnsView;
import com.twi.awayday2014.view.custom.ScrollListener;
import com.twi.awayday2014.view.custom.ScrollableView;
import com.twi.awayday2014.view.fragments.AgendaFragment;
import com.twi.awayday2014.view.fragments.BreakoutFragment;
import com.twi.awayday2014.view.fragments.SpeakersFragment;

import java.util.HashMap;
import java.util.Map;


public class HomeActivity extends FragmentActivity implements ScrollListener {
    private static final String TAG = "HomeActivity";

    private static final float ACTIONBAR_INVISIBLITY_THRESHOLD = .9f;
    public static final int AGENDA_FRAGMENT = 1;
    public static final int SPEAKERS_FRAGMENT = 2;
    public static final int BREAKOUT_FRAGMENT = 3;
    public static final int VIDEOS_FRAGMENT = 4;
    public static final int TAG_FRAGMENT = 5;

    private DrawerHelper drawerHelper;
    private Drawable actionbarDrawable;
    private DrawerLayout drawerLayout;
    private View customActionbar;
    private View customActionbarBackground;
    private View header;
    private int scrollableHeaderHeight;
    private Drawable appIcon;
    private KenBurnsView headerPicture;
    private double mCurrentPosition;
    private Fragment currentFragment;
    private TextView selectedSectionText;
    private ImageView selectedSectionIcon;
    private float ratioTravelled;
    private Map<Integer, ScrollableView> parallelScrollableChilds;
    private ScrollListener delegateListener;
    private boolean firstOnScrollCallAfterResumeRecived;

    private float currentVisibleHeaderHeight;
    private int headerActionbarHeight;
    private View actionbarRootLayout;
    private View drawerIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ratioTravelled = 1;
        drawerHelper = new DrawerHelper(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerHelper.onCreate(drawerLayout);

        parallelScrollableChilds = new HashMap<Integer, ScrollableView>();

        setupActionbar();
        setupHeader();
        setupDebugMode();
        fetchData();
    }

    private void setupDebugMode() {
        if (BuildConfig.DEBUG) {
            OsUtils.enableStrictMode();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AwayDayApplication application = (AwayDayApplication) getApplication();
        application.getPresenterParseDataFetcher().checkDataOutdated();
        application.getAgendaParseDataFetcher().checkDataOutdated();
        appIcon.setAlpha((int) (255 * ratioTravelled));
        firstOnScrollCallAfterResumeRecived = false;
    }

    private void fetchData() {
        AwayDayApplication application = (AwayDayApplication) getApplication();
        ParseDataService parseDataService = application.getParseDataService();
        parseDataService.fetchThemeInBackground();
    }

    private void setupHeader() {
        drawerIcon = findViewById(R.id.drawerIcon);
        customActionbar = findViewById(R.id.customActionbar);
        customActionbarBackground = findViewById(R.id.customActionbarBackgroundView);

        header = findViewById(R.id.header);
        headerActionbarHeight = (int) getResources().getDimension(R.dimen.home_activity_header_bar_height);
        scrollableHeaderHeight = (int) getResources().getDimension(R.dimen.home_activity_header_height_without_countdown)
                - headerActionbarHeight;
        currentVisibleHeaderHeight = scrollableHeaderHeight + headerActionbarHeight;

        headerPicture = (KenBurnsView) findViewById(R.id.header_picture);
        Bitmap[] bitmaps = new Bitmap[4];
        bitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.awayday_2014_background);
        bitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.picture1);
        bitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.notifications_image_travel);
        bitmaps[3] = BitmapFactory.decodeResource(getResources(), R.drawable.notifications_image_sessions);
        headerPicture.setBitmaps(bitmaps);
        setupHeaderText();
        Button notificationsButton = (Button) findViewById(R.id.notificationsButton);
        notificationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NotificationsActivity.class);
                HomeActivity.this.startActivity(intent);
            }
        });

        Button twitterButton = (Button) findViewById(R.id.twitterButton);
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TweetsActivity.class);
                HomeActivity.this.startActivity(intent);
            }
        });

        selectedSectionIcon = (ImageView) findViewById(R.id.selectedSectionIcon);
    }

    @Override
    public void addParallelScrollableChild(ScrollableView scrollableView, int position) {
        parallelScrollableChilds.put(position, scrollableView);
        if (position == 0) {
            scrollableView.setActive(true);
        }
        scrollableView.setScrollListener(this);
    }

    @Override
    public void removeParallelScrollableChild(ScrollableView scrollableView) {
        scrollableView.setScrollListener(null);
        parallelScrollableChilds.remove(scrollableView);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerHelper.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onDrawerOpen() {
        getActionBar().setTitle("Away Day");
        invalidateOptionsMenu();
    }


    public void onDrawerClosed() {
        getActionBar().setTitle("");
        invalidateOptionsMenu();
    }

    public void onDrawerSlide(float slideOffset) {
        if(slideOffset == 0 && ratioTravelled >= ACTIONBAR_INVISIBLITY_THRESHOLD){
            actionbarRootLayout.setVisibility(View.INVISIBLE);
        }else{
            actionbarRootLayout.setVisibility(View.VISIBLE);
        }

        int currentIconAlpha = (int) (255 * (1 - ratioTravelled));
        int newIconAlpha = (int) (255 * slideOffset);
        appIcon.setAlpha(Math.max(newIconAlpha, currentIconAlpha));

        actionbarDrawable.setAlpha((int) (255 * slideOffset));
        if (slideOffset < 1) {
            getActionBar().setTitle("");
        }
    }

    private int getAbsTop(View view) {
        int[] coords = {0, 0};
        view.getLocationOnScreen(coords);
        return coords[1];
    }

    private void setupHeaderText() {
        TextView headerText = (TextView) findViewById(R.id.headerDetailsHeaderText);
        headerText.setTypeface(Fonts.openSansLightItalic(this));
        TextView mainText = (TextView) findViewById(R.id.headerDetailsMainText);
        mainText.setTypeface(Fonts.openSansSemiBold(this));
        TextView footerDate = (TextView) findViewById(R.id.headerDetailsFooterDate);
        footerDate.setTypeface(Fonts.openSansLightItalic(this));
        selectedSectionText = (TextView) findViewById(R.id.selectedSectionText);
        selectedSectionText.setTypeface(Fonts.openSansRegular(this));
    }

    private void setupActionbar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("");
        appIcon = getResources().getDrawable(R.drawable.ic_launcher);
        actionBar.setIcon(appIcon);
        actionBar.setDisplayShowTitleEnabled(true);
        actionbarDrawable = getResources().getDrawable(R.drawable.ab_solid_awayday);
        actionbarDrawable.setAlpha(0);
        actionBar.setBackgroundDrawable(actionbarDrawable);

        View decorView = getWindow().getDecorView();
        int resId;
        if (!OsUtils.hasHoneycomb()) {
            resId = getResources().getIdentifier(
                    "action_bar_container", "id", getPackageName());
        } else {
            resId = Resources.getSystem().getIdentifier(
                    "action_bar_container", "id", "android");
        }
        if (resId != 0) {
            actionbarRootLayout = decorView.findViewById(resId);
        }
    }

    public void onNavigationItemSelected(int position, int resourceId) {
        if (mCurrentPosition == position) {
            drawerHelper.closeDrawer();
            return;
        }
        mCurrentPosition = position;
        selectItem(position);

        if (selectedSectionText != null) {
            selectedSectionText.setText(getFragmentTitle(position));
            selectedSectionIcon.setImageResource(resourceId);
        }
    }

    private void selectItem(int position) {
        startFragment(position);
        drawerHelper.closeDrawer();
    }

    private void startFragment(int position) {
        currentFragment = getFragment(position);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, currentFragment);
        transaction.commit();
    }

    public Fragment getFragment(int position) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(position);
        if (fragment == null) {
            fragment = createFragment(position);
        }
        return fragment;
    }

    public Fragment createFragment(int position) {
        switch (position) {
            default:
            case AGENDA_FRAGMENT:
                return AgendaFragment.newInstance(position);
            case SPEAKERS_FRAGMENT:
                return SpeakersFragment.newInstance(position);
            case BREAKOUT_FRAGMENT:
                return BreakoutFragment.newInstance(position);
            case VIDEOS_FRAGMENT:
                return BreakoutFragment.newInstance(position);
            case TAG_FRAGMENT:
                return BreakoutFragment.newInstance(position);
        }
    }

    public String getFragmentTitle(int position) {
        switch (position) {
            default:
            case AGENDA_FRAGMENT:
                return "Agenda";
            case SPEAKERS_FRAGMENT:
                return "Speakers";
            case BREAKOUT_FRAGMENT:
                return "Breakout Sessions";
            case VIDEOS_FRAGMENT:
                return "Videos";
            case TAG_FRAGMENT:
                return "Tags";
        }
    }

    public float getCurrentVisibleHeaderHeight() {
        return currentVisibleHeaderHeight;
    }

    public void setDelegateListener(ScrollListener delegateListener) {
        this.delegateListener = delegateListener;
    }

    @Override
    public void onScroll(ScrollableView scrollableView, float y) {
        Log.e(TAG, "onScroll " + y);
        // This is a dirty workaround to ignore the redundant call
        // to onscroll with y=0 right after the activity resume
        if(!firstOnScrollCallAfterResumeRecived && y == 0){
            Log.e(TAG, "firstOnScrollCallAfterResumeRecived " + firstOnScrollCallAfterResumeRecived);
            firstOnScrollCallAfterResumeRecived = true;
            return;
        }

        adjustHeader(y);
        changeActionbarAlpha();

        if (delegateListener != null) {
            delegateListener.onScroll(scrollableView, y);
        }

        if (parallelScrollableChilds.size() > 1) {
            for (Integer integer : parallelScrollableChilds.keySet()) {
                ScrollableView scrollableChild = parallelScrollableChilds.get(integer);
                if (!scrollableChild.getActive()) {
                    scrollableChild.scrollTo(currentVisibleHeaderHeight);
                }
            }
        }
    }

    private void adjustHeader(float y) {
        if (-y <= scrollableHeaderHeight) {
            currentVisibleHeaderHeight = scrollableHeaderHeight + y + headerActionbarHeight;
            header.setTranslationY(y);
        } else {
            currentVisibleHeaderHeight = headerActionbarHeight;
            header.setTranslationY(-scrollableHeaderHeight);
        }
    }

    private void changeActionbarAlpha() {
        ratioTravelled = -header.getTranslationY() / scrollableHeaderHeight;
        customActionbarBackground.setAlpha(ratioTravelled);
        appIcon.setAlpha((int) (255 * (1 - ratioTravelled)));

        if(ratioTravelled >= ACTIONBAR_INVISIBLITY_THRESHOLD){
            drawerIcon.setVisibility(View.VISIBLE);
            actionbarRootLayout.setVisibility(View.GONE);
        }else {
            drawerIcon.setVisibility(View.INVISIBLE);
            actionbarRootLayout.setVisibility(View.VISIBLE);
        }
    }

    public void setCurrentParallelScrollableChild(int position) {
        for (Integer integer : parallelScrollableChilds.keySet()) {
            if (position == integer) {
                parallelScrollableChilds.get(integer).setActive(true);
            } else {
                parallelScrollableChilds.get(integer).setActive(false);
            }
        }
    }
}
