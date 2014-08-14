package com.twi.awayday2014.view;

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
import com.twi.awayday2014.utils.Fonts;
import com.twi.awayday2014.view.fragments.TwitterLoginFragment;

public class TweetsActivity extends FragmentActivity{

    private ListView tweetsList;
    private TwitterLoginFragment twitterLoginFragment;

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
        setupTwitterButton();
        setupFragments();
    }

    private void setupTwitterButton() {
        Button twitterButton = (Button) findViewById(R.id.twitterButton);
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterLoginFragment.slideIn();
            }
        });
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
