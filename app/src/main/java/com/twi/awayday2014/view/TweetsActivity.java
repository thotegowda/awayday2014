package com.twi.awayday2014.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.adapters.TweetsAdapter;
import com.twi.awayday2014.animations.SmoothInterpolator;
import com.twi.awayday2014.services.twitter.TwitterService;
import com.twi.awayday2014.tasks.AsyncTweeterTasks;
import com.twi.awayday2014.utils.Fonts;
import com.twi.awayday2014.utils.OsUtils;
import com.twi.awayday2014.view.custom.MultiSwipeRefreshLayout;
import com.twi.awayday2014.view.fragments.SlidingFragment;
import com.twi.awayday2014.view.fragments.TwitterLoginFragment;
import com.twi.awayday2014.view.fragments.TwitterTweetFragment;

import java.util.List;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;

import static android.widget.AbsListView.OnScrollListener;
import static com.twi.awayday2014.tasks.AsyncTweeterTasks.TwitterCallbacks;
import static com.twi.awayday2014.view.fragments.TwitterLoginFragment.CLOSE_ANIMATION_DURATION;
import static com.twi.awayday2014.view.fragments.TwitterLoginFragment.OPEN_ANIMATION_DURATION;

public class TweetsActivity extends FragmentActivity implements TwitterCallbacks, MultiSwipeRefreshLayout.CanChildScrollUpCallback {
    private static final String TAG = "TweetsActivity";
    private static final int INTENT_TWITTER_AUTH = 0;

    private ListView tweetsList;
    private TwitterLoginFragment twitterLoginFragment;
    private TwitterTweetFragment twitterTweetFragment;
    private SlidingFragment currentActiveFragment;
    private Button twitterButton;
    private Button cancelButton;
    private AsyncTweeterTasks asyncTweeterTasks;
    private TwitterService twitterService;
    private TweetsAdapter tweetsAdapter;
    private MultiSwipeRefreshLayout swipeRefreshLayout;
    private boolean isRefreshInProgress;
    private TextView placeholderText;
    private View placeHolderView;
    private View progressBar;
    private View loginButton;
    private Handler handler;
    private boolean isCancleButtonAnimating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);
        setupButtons();
        setupTweeterTask();
        setupPlaceHolderViews();
        showFetchingViews();
        setupListView();
        setupHeader();
        setupFragments();
        setupSwipeRefresh();
        attemptFetch();

        handler = new Handler();
    }

    @Override
    public void onBackPressed() {
        if (currentActiveFragment.isOpen()) {
            slideOutCurrentActiveFragment();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case INTENT_TWITTER_AUTH:
                if (resultCode == RESULT_CANCELED) {
                    showToast("Twitter login failed. Please try again.");
                } else if (resultCode == RESULT_OK) {
                    String oauthVerifier = (String) data.getExtras().get("oauth_verifier");
                    if (oauthVerifier == null) {
                        showToast("Please grant access to the app for twitting.");
                    } else {
                        asyncTweeterTasks.retrieveAndSaveAccessToken(oauthVerifier);
                    }
                }
                break;
        }

    }

    private void setupPlaceHolderViews() {
        placeHolderView = findViewById(R.id.placeholderView);
        placeholderText = (TextView) findViewById(R.id.placeholderText);
        placeholderText.setTypeface(Fonts.openSansLight(this));
        progressBar = findViewById(R.id.progressBar);
    }


    private void attemptFetch() {
        if (OsUtils.haveNetworkConnection(this)) {
            asyncTweeterTasks.search();
        } else {
            showNoConnectionView();
        }
    }

    private void showFetchingViews() {
        placeHolderView.setVisibility(View.VISIBLE);
        placeholderText.setText("Busy fetching tweets!");
        twitterButton.setVisibility(View.INVISIBLE);
    }

    private void hideFetchingViews() {
        placeHolderView.setVisibility(View.GONE);
        twitterButton.setVisibility(View.VISIBLE);
    }

    private void showNoConnectionView() {
        placeHolderView.setVisibility(View.VISIBLE);
        placeholderText.setText("No Network Connection");
        progressBar.setVisibility(View.INVISIBLE);
        twitterButton.setVisibility(View.INVISIBLE);
    }

    private void showNoDataView(String msg) {
        placeHolderView.setVisibility(View.VISIBLE);
        placeholderText.setText(msg);
        progressBar.setVisibility(View.INVISIBLE);
        twitterButton.setVisibility(View.VISIBLE);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout = (MultiSwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setColorScheme(
                    R.color.twitterColor,
                    R.color.pink,
                    R.color.twitterColor,
                    R.color.pink);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (!isRefreshInProgress) {
                        isRefreshInProgress = true;
                        asyncTweeterTasks.refresh();
                    }
                }
            });

            swipeRefreshLayout.setCanChildScrollUpCallback(TweetsActivity.this);
        }

    }

    private void setupTweeterTask() {
        asyncTweeterTasks = new AsyncTweeterTasks(this, ((AwayDayApplication) getApplication()).getTwitterService(), this);
        twitterService = ((AwayDayApplication) getApplication()).getTwitterService();
    }

    public AsyncTweeterTasks getAsyncTweeterTasks() {
        return asyncTweeterTasks;
    }

    private void setupButtons() {
        twitterButton = (Button) findViewById(R.id.twitterButton);
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideInCurrentActiveFragment();
            }
        });

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setClickable(false);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideOutCurrentActiveFragment();
            }
        });

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    loginToTwitter();
                } catch (TwitterException e) {
                    e.printStackTrace();
                    showToast("Something went wrong while trying to login.");
                }
            }
        });
    }

    private void slideOutCurrentActiveFragment() {
        currentActiveFragment.slideOut();
        startTwitterButtonMoveDownAnimation();
    }

    private void slideInCurrentActiveFragment() {
        currentActiveFragment.slideIn();
        startTwitterButtonMoveUpAnimation();
    }

    private void showToast(String msg) {
        Toast.makeText(TweetsActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void loginToTwitter() throws TwitterException {
        if (!OsUtils.haveNetworkConnection(this)) {
            showToast("No network connectivity. Please check your connection.");
            return;
        }

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    RequestToken requestToken = twitterService.getRequestToken();
                    Intent i = new Intent(TweetsActivity.this, TwitterWebviewActivity.class);
                    i.putExtra("URL", requestToken.getAuthenticationURL());
                    startActivityForResult(i, INTENT_TWITTER_AUTH);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private void startTwitterButtonMoveUpAnimation() {
        twitterButton.setClickable(false);
        twitterButton.animate()
                .alpha(0f)
                .rotation(360)
                .translationY(-twitterLoginFragment.getRootLayoutOriginalHeight())
                .setDuration(OPEN_ANIMATION_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        twitterButton.setVisibility(View.GONE);
                    }
                })
                .setInterpolator(new SmoothInterpolator())
                .start();
        cancelButton.setVisibility(View.VISIBLE);
        cancelButton.setClickable(false);
        cancelButton.setAlpha(0);
        isCancleButtonAnimating = true;
        cancelButton.animate()
                .alpha(1f)
                .rotation(360)
                .translationY(-twitterLoginFragment.getRootLayoutOriginalHeight())
                .setDuration(OPEN_ANIMATION_DURATION)
                .setInterpolator(new SmoothInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        cancelButton.setClickable(true);
                        isCancleButtonAnimating = false;
                    }
                })
                .start();
    }

    private void startTwitterButtonMoveDownAnimation() {
        twitterButton.setVisibility(View.VISIBLE);
        twitterButton.setAlpha(0);
        twitterButton.animate()
                .alpha(1f)
                .rotation(-360)
                .translationY(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        twitterButton.setClickable(true);
                    }
                })
                .setDuration(CLOSE_ANIMATION_DURATION)
                .start();

        cancelButton.setClickable(false);
        isCancleButtonAnimating = true;
        cancelButton.animate()
                .alpha(0f)
                .rotation(-360)
                .translationY(0)
                .setDuration(CLOSE_ANIMATION_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        cancelButton.setVisibility(View.GONE);
                        isCancleButtonAnimating = false;
                    }
                })
                .start();
    }

    private void setupFragments() {
        twitterLoginFragment = (TwitterLoginFragment) getSupportFragmentManager().findFragmentById(R.id.twitterLoginFragment);
        twitterTweetFragment = (TwitterTweetFragment) getSupportFragmentManager().findFragmentById(R.id.tweetFragment);
        if (asyncTweeterTasks.isLoggedIn()) {
            currentActiveFragment = twitterTweetFragment;
        } else {
            currentActiveFragment = twitterLoginFragment;
        }
    }

    private void setupHeader() {
        TextView headerText = (TextView) findViewById(R.id.tweetsHeaderText);
        headerText.setTypeface(Fonts.openSansRegular(this));
        String hashTag = ((AwayDayApplication) getApplication()).getTwitterService().getPreference().getHashTags();
        headerText.setText("#" + hashTag);
    }

    private void setupListView() {
        tweetsList = (ListView) findViewById(R.id.tweetsList);
        tweetsAdapter = new TweetsAdapter(this);
        tweetsList.setAdapter(tweetsAdapter);
        tweetsList.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisible, int visibleCount, int totalCount) {
                if (!asyncTweeterTasks.isSearchInProgress() && firstVisible + visibleCount >= (totalCount - 5) && asyncTweeterTasks.hasMoreTweets()) {
                    if (OsUtils.haveNetworkConnection(TweetsActivity.this)) {
                        asyncTweeterTasks.searchNext();
                    }
                }
            }
        });
    }

    @Override
    public void onSearchResults(List<Status> tweets) {
        if (tweets.size() == 0 && tweetsAdapter.getCount() == 0) {
            showNoDataView("No tweets found!");
        } else {
            hideFetchingViews();
            tweetsAdapter.append(tweets);
        }
    }

    @Override
    public void onRefresh(List<Status> tweets) {
        Log.d(TAG, tweets.size() +  " new tweets found");
        if(tweets.size() > 0){
            hideFetchingViews();
        }
        tweetsAdapter.insertAtTheTop(tweets);
        swipeRefreshLayout.setRefreshing(false);
        isRefreshInProgress = false;
    }

    @Override
    public void onUserLoggedIn() {
        if (currentActiveFragment instanceof TwitterLoginFragment && currentActiveFragment.isOpen()) {
            currentActiveFragment.slideOut();
            startTwitterButtonMoveDownAnimation();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    currentActiveFragment = twitterTweetFragment;
                    slideInCurrentActiveFragment();
                }
            }, SlidingFragment.CLOSE_ANIMATION_DURATION);
        }
    }

    @Override
    public void onUserLoggedInFail() {
        showToast("Something went wrong while login. Please try again.");
    }

    @Override
    public void onTweetSuccess() {
        showToast("Tweeted successfully");
    }

    @Override
    public void onTweetFailure() {
        Toast.makeText(this,
                "Something went wrong, tweet unsuccessful. We don't have any retry mechanism for now. Please try again.",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGenericError() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                showNoDataView("Something went wrong. Please try again later.");
            }
        });
    }

    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        return !(isFirstTweet() && isFirstTweetVisible());
    }

    private boolean isFirstTweetVisible() {
        return tweetsList.getChildAt(0) != null ? tweetsList.getChildAt(0).getTop() >= 0 : true;
    }

    private boolean isFirstTweet() {
        return tweetsList.getFirstVisiblePosition() == 0;
    }

    private boolean isLastTweetVisible() {
        return tweetsList.getLastVisiblePosition() >= tweetsAdapter.getCount() - 1;
    }

    public void onTweetButtonClick() {
        showToast("Sending your tweet in the background.");
        slideOutCurrentActiveFragment();
    }

    public void onTweetFragmentHeightChange(int height) {
        if (!isCancleButtonAnimating) {
            cancelButton.setTranslationY(-height);
        }
    }


}
