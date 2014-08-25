package com.twi.awayday2014.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.adapters.TweetsAdapter;
import com.twi.awayday2014.animations.SmoothInterpolator;
import com.twi.awayday2014.utils.Fonts;
import com.twi.awayday2014.view.fragments.TwitterLoginFragment;

import static com.twi.awayday2014.view.fragments.TwitterLoginFragment.CLOSE_ANIMATION_DURATION;
import static com.twi.awayday2014.view.fragments.TwitterLoginFragment.OPEN_ANIMATION_DURATION;

public class TweetsActivity extends FragmentActivity{

    private ListView tweetsList;
    private TwitterLoginFragment twitterLoginFragment;
    private Button twitterButton;
    private Button cancelButton;
    private AnimatorSet slideDownAnimatorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);
        View rootLayout = findViewById(R.id.tweetsRootLayout);
        AwayDayApplication awayDayApplication = (AwayDayApplication)getApplication();
        Bitmap background = awayDayApplication.getHomeActivityScreenshot();
        if(background != null){
           rootLayout.setBackground(new BitmapDrawable(getResources(), background));
        }

        setupListView();
        setupHeader();
        setupButtons();
        setupFragments();
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
    }

    private void setupListView() {
        tweetsList = (ListView) findViewById(R.id.tweetsList);
        tweetsList.setAdapter(new TweetsAdapter(this));
    }
}
