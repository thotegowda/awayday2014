package com.twi.awayday2014.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.adapters.TweetsAdapter;
import com.twi.awayday2014.animations.SmoothInterpolator;
import com.twi.awayday2014.tasks.AsyncTweeterTasks;
import com.twi.awayday2014.utils.Fonts;
import com.twi.awayday2014.view.custom.MultiSwipeRefreshLayout;
import com.twi.awayday2014.view.fragments.TwitterLoginFragment;

import java.util.List;

import twitter4j.Status;

import static android.widget.AbsListView.OnScrollListener;
import static com.twi.awayday2014.tasks.AsyncTweeterTasks.TwitterCallbacks;
import static com.twi.awayday2014.view.fragments.TwitterLoginFragment.CLOSE_ANIMATION_DURATION;
import static com.twi.awayday2014.view.fragments.TwitterLoginFragment.OPEN_ANIMATION_DURATION;

public class TweetsActivity extends FragmentActivity implements TwitterCallbacks, MultiSwipeRefreshLayout.CanChildScrollUpCallback {
    private static final String TAG = "TweetsActivity";
    private ListView tweetsList;
    private TwitterLoginFragment twitterLoginFragment;
    private Button twitterButton;
    private Button cancelButton;
    private AsyncTweeterTasks asyncTweeterTasks;
    private TweetsAdapter tweetsAdapter;
    private MultiSwipeRefreshLayout swipeRefreshLayout;
    private boolean isRefreshInProgress;
    private TextView placeholderText;
    private View placeHolderView;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);
        setupTweeterTask();
        setupButtons();
        setupPlaceHolderViews();
        showFetchingViews();
        setupListView();
        setupHeader();
        setupFragments();
        setupSwipeRefresh();
    }

    private void setupPlaceHolderViews(){
        placeHolderView = findViewById(R.id.placeholderView);
        placeholderText = (TextView) findViewById(R.id.placeholderText);
        placeholderText.setTypeface(Fonts.openSansRegular(this));
        progressBar = findViewById(R.id.progressBar);
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

    private void showNoDataView() {
        placeHolderView.setVisibility(View.VISIBLE);
        placeholderText.setText("No tweets found!");
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
        asyncTweeterTasks.search();
    }

    private void setupButtons() {
        twitterButton = (Button) findViewById(R.id.twitterButton);
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterLoginFragment.slideIn();
                startTwitterButtonMoveUpAnimation();
            }
        });

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setClickable(false);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterLoginFragment.slideOut();
                startTwitterButtonMoveDownAnimation();
            }
        });
    }

    private void startTwitterButtonMoveUpAnimation() {
        twitterButton.animate()
                .alpha(0f)
                .rotation(360)
                .translationY(-twitterLoginFragment.getRootLayoutHeight())
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
        cancelButton.setAlpha(0);
        cancelButton.animate()
                .alpha(1f)
                .rotation(360)
                .translationY(-twitterLoginFragment.getRootLayoutHeight())
                .setDuration(OPEN_ANIMATION_DURATION)
                .setInterpolator(new SmoothInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        cancelButton.setClickable(true);
                        twitterButton.setClickable(false);
                        //just a placeholder to override the other listener
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
                        cancelButton.setClickable(false);
                        //just a placeholder to override the other listener
                    }
                })
                .setDuration(CLOSE_ANIMATION_DURATION)
                .start();

        cancelButton.animate()
                .alpha(0f)
                .rotation(-360)
                .translationY(0)
                .setDuration(CLOSE_ANIMATION_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        cancelButton.setVisibility(View.GONE);
                    }
                })
                .start();
    }

    private void setupFragments() {
        twitterLoginFragment = (TwitterLoginFragment) getSupportFragmentManager().findFragmentById(R.id.twitterLoginFragment);
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
                    asyncTweeterTasks.searchNext();
                }
            }
        });
    }

    @Override
    public void onSearchResults(List<Status> tweets) {
        if(tweets.size() == 0 && tweetsAdapter.getCount() == 0){
            showNoDataView();
        }else{
            hideFetchingViews();
            tweetsAdapter.append(tweets);
        }
    }

    @Override
    public void onRefresh(List<Status> tweets) {
        tweetsAdapter.insertAtTheTop(tweets);
        swipeRefreshLayout.setRefreshing(false);
        isRefreshInProgress = false;
    }

    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        if (isLastTweetVisible()) {
            return true;
        } else {
            return !(isFirstTweet() && isFirstTweetVisible());
        }
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
}
