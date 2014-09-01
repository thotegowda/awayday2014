package com.twi.awayday2014.components.activities;

import android.app.*;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
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
import com.twi.awayday2014.components.DrawerHelper;
import com.twi.awayday2014.utils.Blur;
import com.twi.awayday2014.utils.Fonts;
import com.twi.awayday2014.R;
import com.twi.awayday2014.components.fragments.*;
import com.twi.awayday2014.customviews.KenBurnsView;
import com.twi.awayday2014.customviews.ObservableScrollView;

import java.util.ArrayList;
import java.util.List;

public class NewHomeActivity extends Activity {
    private static final String TAG = "HomeActivity";

    public static final int AGENDA_FRAGMENT = 1;
    public static final int SPEAKERS_FRAGMENT = 2;
    public static final int BREAKOUT_FRAGMENT = 3;
    public static final int MY_SCHEDULE_FRAGMENT = 4;

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
    private double mCurrentPosition;
    private Fragment currentFragment;
    private TextView selectedSectionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_home);

        drawerHelper = new DrawerHelper(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.new_drawer_layout);
        drawerHelper.onCreate(drawerLayout);
        customActionbarStateListener = new ArrayList<CustomActionbarStateListener>();

        setupActionbar();
        setupHeader();
        setupScroller();
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
                int distanceToTravel = defaultActionbarTopPos - defaultHeaderTopPos;
                int currentDistanceTravelled = getAbsTop(customActionbar) - defaultHeaderTopPos;
                float ratioTravelled = (float) currentDistanceTravelled / distanceToTravel;
                Log.e("LIST", "ratio: " + ratioTravelled);

                if (ratioTravelled <= 0.0f && currentCustomActionbarState == CustomActionbarState.FLOATING) {
                    onCustomActionBarStateChanged(CustomActionbarState.STICKY);
                    Log.d("LIST", "actionbar is now sticky");
                } else if (ratioTravelled > 0 && currentCustomActionbarState == CustomActionbarState.STICKY) {
                    onCustomActionBarStateChanged(CustomActionbarState.FLOATING);
                    Log.d("LIST", "actionbar is now floating");
                }
                customActionbarBackground.setAlpha(1 - ratioTravelled);
                appIcon.setAlpha((int) (255 * ratioTravelled));
            }
        });
    }

    private void onCustomActionBarStateChanged(CustomActionbarState state) {
        currentCustomActionbarState = state;
        notifyCustomActionbarStateChange(state);
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
        if (drawerHelper.isDrawerOpen()) {
            drawerHelper.closeDrawer();
        } else {
            super.onBackPressed();
        }
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
        actionbarDrawable.setAlpha((int) (255 * slideOffset));
    }

    private int getAbsTop(View view) {
        int[] coords = {0, 0};
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
        KenBurnsView headerPicture = (KenBurnsView) findViewById(R.id.header_picture);
        headerPicture.setResourceIds(R.drawable.picture0, R.drawable.picture1);
        setupHeaderText();
        Button notificationsButton = (Button) findViewById(R.id.notificationsButton);
        notificationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AwayDayApplication awayDayApplication = (AwayDayApplication) NewHomeActivity.this.getApplication();
                awayDayApplication.setHomeActivityScreenshot(getScreenShot());
                Intent intent = new Intent(NewHomeActivity.this, NotificationsActivity.class);
                NewHomeActivity.this.startActivity(intent);
            }
        });

        Button twitterButton = (Button) findViewById(R.id.twitterButton);
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AwayDayApplication awayDayApplication = (AwayDayApplication) NewHomeActivity.this.getApplication();
                awayDayApplication.setHomeActivityScreenshot(getScreenShot());
                Intent intent = new Intent(NewHomeActivity.this, TweetsActivity.class);
                NewHomeActivity.this.startActivity(intent);
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

        Bitmap bitmapWithoutStatusBar = Bitmap.createBitmap(b, 0, statusBarHeight, width, height - statusBarHeight);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapWithoutStatusBar,
                bitmapWithoutStatusBar.getWidth() / 2, bitmapWithoutStatusBar.getHeight() / 2, false);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            decorView.destroyDrawingCache();
            Blur blur = new Blur(this);
            return blur.blur(scaledBitmap, 10);
        }
        return scaledBitmap;
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
        actionbarDrawable = getResources().getDrawable(R.drawable.ab_solid_awayday);
        actionbarDrawable.setAlpha(0);
        actionBar.setBackgroundDrawable(actionbarDrawable);
    }

    public void onNavigationItemSelected(int position) {
        if (mCurrentPosition == position) {
            drawerHelper.closeDrawer();
            return;
        }
        mCurrentPosition = position;
        selectItem(position);

        selectedSectionText.setText(getFragmentTitle(position));
    }

    private void selectItem(int position) {
        startFragment(position);
        drawerHelper.closeDrawer();
    }

    private void startFragment(int position) {
        currentFragment = getFragment(position);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, currentFragment);

        if (fragmentManager.getBackStackEntryCount() <= 1) {
            transaction.addToBackStack(null);
        } else {
            fragmentManager.popBackStack();
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public android.app.Fragment getFragment(int position) {
        android.app.Fragment fragment = getFragmentManager().findFragmentById(position);
        if (fragment == null) {
            fragment = createFragment(position);
        }
        return fragment;
    }

    public android.app.Fragment createFragment(int position) {
        switch (position) {
            default:
            case AGENDA_FRAGMENT:
                return AgendaFragment.newInstance(position);
            case SPEAKERS_FRAGMENT:
                return SpeakersFragment.newInstance(position);
            case BREAKOUT_FRAGMENT:
                return BreakoutFragment.newInstance(position);
            case MY_SCHEDULE_FRAGMENT:
                return MyScheduleFragment.newInstance(position);

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
            case MY_SCHEDULE_FRAGMENT:
                return "My Schedule";
        }
    }

    public enum CustomActionbarState {
        STICKY,
        FLOATING;
    }

    public interface CustomActionbarStateListener {
        void onActionbarStateChange(CustomActionbarState customActionbarState);
    }
}
