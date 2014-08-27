package com.twi.awayday2014.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import com.twi.awayday2014.view.fragments.TwitterLoginFragment;

import java.util.List;

import twitter4j.Status;

import static android.widget.AbsListView.OnScrollListener;
import static com.twi.awayday2014.tasks.AsyncTweeterTasks.TwitterCallbacks;
import static com.twi.awayday2014.view.fragments.TwitterLoginFragment.CLOSE_ANIMATION_DURATION;
import static com.twi.awayday2014.view.fragments.TwitterLoginFragment.OPEN_ANIMATION_DURATION;

public class TweetsActivity extends FragmentActivity implements TwitterCallbacks{

    private ListView tweetsList;
    private TwitterLoginFragment twitterLoginFragment;
    private Button twitterButton;
    private Button cancelButton;
    private AnimatorSet slideDownAnimatorSet;
    private AsyncTweeterTasks asyncTweeterTasks;
    private TweetsAdapter tweetsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);
        setupTweeterTask();
        setupListView();
        setupHeader();
        setupButtons();
        setupFragments();
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
        tweetsAdapter.append(tweets);
    }

    @Override
    public void onRefresh(List<Status> tweets) {

    }
}
