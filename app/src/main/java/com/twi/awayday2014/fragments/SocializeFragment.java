package com.twi.awayday2014.fragments;

import android.app.ListFragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;
import com.twi.awayday2014.HomeActivity;
import com.twi.awayday2014.R;
import com.twi.awayday2014.adapters.TweetsAdapter;
import com.twi.awayday2014.models.Tweeter;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;

public class SocializeFragment extends ListFragment {

    private static final String TAG = "AwayDay";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TWITTER_SEARCH_TERM = "ThoughtWorks";

    private Tweeter tweeter = HomeActivity.getTweeter();
    private TweetsAdapter tweetsAdapter;
    private View rootView;
    private View tweetButton;

    public static SocializeFragment newInstance(int sectionNumber) {
        SocializeFragment fragment = new SocializeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SocializeFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tweetsAdapter = new TweetsAdapter(this.getActivity(), new ArrayList<Status>());
        setListAdapter(tweetsAdapter);

        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisible, int visibleCount, int totalCount) {
                int padding = 5;
                if (totalCount > 10 && shouldLoadMore(firstVisible, visibleCount, totalCount, padding)) {
                    twitterSearchNext();
                }
            }
        });

        twitterSearch(TWITTER_SEARCH_TERM);

        Uri uri = getActivity().getIntent().getData();
        if (uri != null && uri.toString().startsWith(Tweeter.TWITTER_CALLBACK_URL)) {
            ((TextView)tweetButton).setText("Tweet");
            saveAccessToken(uri);
        } else {
            ((TextView) tweetButton).setText("LogIn");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_socialize, container, false);
        tweetButton = rootView.findViewById(R.id.signin);
        tweetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    if (tweeter.isTwitterLoggedInAlready()) {
                        tweet();
                    } else {
                        loginToTwitter();
                    }
                } catch (TwitterException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Exception : " + e.getErrorMessage());
                }
            }
        });
        return rootView;
    }

    private boolean shouldLoadMore(int firstVisible, int visibleCount, int totalCount, int padding) {
        return firstVisible + visibleCount + padding >= totalCount;
    }


    private void tweet() {
        tweeter.tweet();
    }

    private void loginToTwitter() throws TwitterException {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    tweeter.login(SocializeFragment.this.getActivity());
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

    }

    public void saveAccessToken(final Uri uri ) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    tweeter.retrieveAccessToken(uri);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

    }

    private void twitterSearch(final String searchTerm) {
        new AsyncTask<Void, Void, List<Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... voids) {
                return tweeter.search(searchTerm);
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> tweets) {
                tweetsAdapter.append(tweets);
            }
        }.execute();
    }

    private void twitterSearchNext() {
        new AsyncTask<Void, Void, List<Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... voids) {
                return tweeter.searchNext();
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> tweets) {
                tweetsAdapter.append(tweets);
            }
        }.execute();
    }
}
