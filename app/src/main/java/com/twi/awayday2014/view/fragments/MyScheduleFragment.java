package com.twi.awayday2014.view.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.twi.awayday2014.R;
import com.twi.awayday2014.adapters.TweetsAdapter;
import android.app.Fragment;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class MyScheduleFragment extends Fragment {

    private static final String TAG = "AwayDay";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TWITTER_SEARCH_TERM = "ThoughtWorks";

    static final String TWITTER_CALLBACK_URL = "oauth://thoughtworks.Twitter_oAuth";
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

    private Twitter twitter;
    private TweetsAdapter tweetsAdapter;
    private QueryResult lastResponse;
    private RequestToken requestToken;
    private View rootView;
    private TwitterFactory twitterFactory;


    public static MyScheduleFragment newInstance(int sectionNumber) {
        MyScheduleFragment fragment = new MyScheduleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MyScheduleFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();

        new AsyncTask<Void,Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    retrieveAccessToken();
                } catch (TwitterException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Exception : " + e.getErrorMessage());
                }
                return null;
            }
        }.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_socialize, container, false);
        rootView.findViewById(R.id.signin).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    loginToTwitter();
                } catch (TwitterException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Exception : " + e.getErrorMessage());
                }
            }
        });
        return rootView;
    }


    private void loginToTwitter() throws TwitterException {
        if (!isTwitterLoggedInAlready()) {
            ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
            configurationBuilder.setOAuthConsumerKey("CyqRuU77Lbyv7i9EOkDMGinSo");
            configurationBuilder.setOAuthConsumerSecret("h2ElL2l13ANJG5qYCUqGacL1uGNnPKkA6mfJmWwnofOI8w6bUb");
            Configuration configuration = configurationBuilder.build();
            twitterFactory = new TwitterFactory(configuration);
            twitter = twitterFactory.getInstance();

            new AsyncTask<Void, Void, Uri>() {

                @Override
                protected Uri doInBackground(Void... voids) {
                    try {
                        requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "Got request token.");
                    Log.d(TAG, "Request token: " + requestToken.getToken());
                    Log.d(TAG, "Request token secret: " + requestToken.getTokenSecret());
                    Log.d(TAG, "Open the following URL and grant access to your account:");
                    Log.d(TAG, requestToken.getAuthorizationURL());
                    return Uri.parse(requestToken.getAuthenticationURL());
                }

                @Override
                protected void onPostExecute(Uri uri) {
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }
            }.execute();
        } else {
            Toast.makeText(getActivity(), "Already Logged into twitter", Toast.LENGTH_LONG).show();
        }
    }

    public void retrieveAccessToken() throws TwitterException {
        Uri uri = getActivity().getIntent().getData();
        if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
            String verifier = uri.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
            AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

            Log.d(TAG, "Got access token.");
            Log.d(TAG, "Access token: " + accessToken.getToken());
            Log.d(TAG, "Access token secret: " + accessToken.getTokenSecret());
        }
    }

    private boolean isTwitterLoggedInAlready() {
        return false;
    }


}
