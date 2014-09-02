package com.twi.awayday2014.view;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
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
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Theme;
import com.twi.awayday2014.services.ParseDataService;
import com.twi.awayday2014.utils.Blur;
import com.twi.awayday2014.utils.Fonts;
import com.twi.awayday2014.view.custom.KenBurnsView;
import com.twi.awayday2014.view.custom.ObservableScrollView;
import com.twi.awayday2014.view.fragments.AgendaFragment;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.twi.awayday2014.utils.Constants.DrawerConstants.AGENDA;

public class HomeActivity extends FragmentActivity{
    private static final String TAG = "HomeActivity";
    private DrawerHelper drawerHelper;
    private Drawable actionbarDrawable;
    private DrawerLayout drawerLayout;
    private View customActionbar;
    private int defaultHeaderTopPos;
    private int defaultActionbarTopPos;
    private CustomActionbarState currentCustomActionbarState = CustomActionbarState.FLOATING;
    private List<CustomActionbarStateListener> customActionbarStateListener;
    private View customActionbarBackground;
    private Drawable appIcon;
    private KenBurnsView headerPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerHelper = new DrawerHelper(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerHelper.onCreate(drawerLayout);
        customActionbarStateListener = new ArrayList<CustomActionbarStateListener>();

        setupActionbar();
        setupHeader();
        setupScroller();
        fetchData();
    }

    private void fetchData() {
        AwayDayApplication application = (AwayDayApplication) getApplication();
        ParseDataService parseDataService = application.getParseDataService();
        parseDataService.fetchThemeInBackground();
    }

    private void setupScroller() {
        final ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scrollView);
        customActionbar = findViewById(R.id.customActionbar);
        final View header = findViewById(R.id.header);
        customActionbarBackground = findViewById(R.id.customActionbarBackgroundView);

        final FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        drawerLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                drawerLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int height = drawerLayout.getHeight();
                contentFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        height - getActionBar().getHeight()));
                defaultHeaderTopPos = getAbsTop(header);
                defaultActionbarTopPos = getAbsTop(customActionbar);
            }
        });

        scrollView.setCallbacks(new ObservableScrollView.ScrollCallbacks() {
            @Override
            public void onScrollChanged() {
                int distanceToTravel  = defaultActionbarTopPos - defaultHeaderTopPos;
                int currentDistanceTravelled = getAbsTop(customActionbar) - defaultHeaderTopPos;
                float ratioTravelled = (float)currentDistanceTravelled/distanceToTravel;
                Log.e(TAG, "ratio: " + ratioTravelled);
                if (ratioTravelled <= 0 && currentCustomActionbarState == CustomActionbarState.FLOATING) {
                    currentCustomActionbarState = CustomActionbarState.STICKY;
                    Log.d(TAG, "actionbar is now sticky");
                    notifyCustomActionbarStateChange(CustomActionbarState.STICKY);
                }else if(ratioTravelled > 0 && currentCustomActionbarState == CustomActionbarState.STICKY){
                    currentCustomActionbarState = CustomActionbarState.FLOATING;
                    Log.d(TAG, "actionbar is now floating");
                    notifyCustomActionbarStateChange(CustomActionbarState.FLOATING);
                }
                customActionbarBackground.setAlpha(1 - ratioTravelled);
                appIcon.setAlpha((int) (255 * ratioTravelled));
            }
        });
    }

    private void notifyCustomActionbarStateChange(CustomActionbarState state) {
        for (CustomActionbarStateListener actionbarStateListener : customActionbarStateListener) {
            actionbarStateListener.onActionbarStateChange(state);
        }
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
        // Handle your other action bar items...

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


    public void onDrawerItemClick(int itemId) {
        Fragment fragment = getFragment(itemId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment, "" + itemId);
        fragmentTransaction.commit();
    }

    public void onDrawerSlide(float slideOffset) {
        actionbarDrawable.setAlpha((int) (255 * slideOffset));
    }

    private int getAbsTop(View view){
        int[] coords = {0,0};
        view.getLocationOnScreen(coords);
        return coords[1];
    }

    public void addCustomActionbarStateListener(CustomActionbarStateListener listener) {
        customActionbarStateListener.add(listener);
    }

    public void removeCustomActionbarStateListener(CustomActionbarStateListener listener) {
        customActionbarStateListener.remove(listener);
    }

    private void setupHeader() {
        headerPicture = (KenBurnsView) findViewById(R.id.header_picture);
        Bitmap[] bitmaps = new Bitmap[4];
        bitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.picture0);
        bitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.picture1);
        bitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.notifications_image_travel);
        bitmaps[3] = BitmapFactory.decodeResource(getResources(), R.drawable.notifications_image_sessions);
//        headerPicture.setResourceIds(R.drawable.picture0, R.drawable.picture1, R.drawable.placeholder_twitter);
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
    }

    public Bitmap getScreenShot() {
        View decorView = getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        Bitmap b = decorView.getDrawingCache();

        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = getWindowManager().getDefaultDisplay().getWidth();
        int height = getWindowManager().getDefaultDisplay().getHeight();

        Bitmap bitmapWithoutStatusBar = Bitmap.createBitmap(b, 0, statusBarHeight, width, height  - statusBarHeight);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapWithoutStatusBar,
                bitmapWithoutStatusBar.getWidth() / 2, bitmapWithoutStatusBar.getHeight() / 2, false);
        bitmapWithoutStatusBar.recycle();

        decorView.destroyDrawingCache();
        Blur blur = new Blur(this);
        return blur.blur(scaledBitmap, 10);
    }

    private void setupHeaderText() {
        TextView headerText = (TextView) findViewById(R.id.headerDetailsHeaderText);
        headerText.setTypeface(Fonts.openSansLightItalic(this));
        TextView mainText = (TextView) findViewById(R.id.headerDetailsMainText);
        mainText.setTypeface(Fonts.openSansSemiBold(this));
        TextView footerDate = (TextView) findViewById(R.id.headerDetailsFooterDate);
        footerDate.setTypeface(Fonts.openSansLightItalic(this));

//        TextView daysCount = (TextView) findViewById(R.id.daysCount);
//        daysCount.setTypeface(Fonts.openSansLight(this));
//        TextView days = (TextView) findViewById(R.id.days);
//        days.setTypeface(Fonts.openSansLightItalic(this));
//        TextView hoursCount = (TextView) findViewById(R.id.hoursCount);
//        hoursCount.setTypeface(Fonts.openSansLight(this));
//        TextView hours = (TextView) findViewById(R.id.hours);
//        hours.setTypeface(Fonts.openSansLightItalic(this));
//        TextView minutesCount = (TextView) findViewById(R.id.minutesCount);
//        minutesCount.setTypeface(Fonts.openSansLight(this));
//        TextView minutes = (TextView) findViewById(R.id.minutes);
//        minutes.setTypeface(Fonts.openSansLightItalic(this));
//        TextView seconds = (TextView) findViewById(R.id.seconds);
//        seconds.setTypeface(Fonts.openSansLightItalic(this));

        TextView selectedSectionText = (TextView) findViewById(R.id.selectedSectionText);
        selectedSectionText.setTypeface(Fonts.openSansRegular(this));
    }

    private void setupActionbar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("");
        appIcon = getResources().getDrawable(R.drawable.ic_launcher);
        actionBar.setIcon(appIcon);
        actionbarDrawable = getResources().getDrawable(R.drawable.ab_solid_awayday);
        actionbarDrawable.setAlpha(0);
        actionBar.setBackgroundDrawable(actionbarDrawable);
    }

    private Fragment getFragment(int position) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("" + position);
        if (fragment == null) {
            fragment = createFragment(position);
        }
        return fragment;
    }

    private Fragment createFragment(int position) {
        switch (position) {
            case AGENDA:
                return new AgendaFragment();
            default:
                return new AgendaFragment();
        }
    }

    public enum CustomActionbarState {
        STICKY,
        FLOATING;
    }

    public interface CustomActionbarStateListener{
        void onActionbarStateChange(CustomActionbarState customActionbarState);
    }

    private class ParseCallbackListener implements ParseDataService.ParseDataListener{

        @Override
        public void onThemeFetched(Theme theme) {

        }

        @Override
        public void onThemeFetchedError(int status) {

        }
    }
}
