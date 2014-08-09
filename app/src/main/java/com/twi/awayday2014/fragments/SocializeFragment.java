package com.twi.awayday2014.fragments;

import android.app.ListFragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.EditText;
import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.adapters.TweetsAdapter;
import com.twi.awayday2014.Tweeter;
import com.twi.awayday2014.ui.MultiSwipeRefreshLayout;
import com.twi.awayday2014.ui.SwipeRefreshLayout;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;

public class SocializeFragment extends ListFragment implements MultiSwipeRefreshLayout.CanChildScrollUpCallback {

    private static final String TAG = "AwayDay";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TWITTER_SEARCH_TERM = "bangalore";
    private static final String AWAYDAY_TWITTER_TAG = "#awayday2014";

    private Tweeter tweeter;
    private TweetsAdapter tweetsAdapter;

    private View rootView;
    private View signInOrTweetButton;
    private View refreshButton;
    private View tweetMessageLayout;
    private View tweetButton;
    private EditText tweetMessageView;
    private View cancelButton;
    private MultiSwipeRefreshLayout swipeRefreshLayout;

    private Animation pushInAnimation;
    private Animation pushOutAnimation;

    private boolean isTweetWindowVisible;
    private boolean isRefreshInProgress;

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
    }

    @Override
    public void onResume() {
        super.onResume();

        tweeter = getApplication().getTwitterService();

        setupAdapter();

        handleTwitterCallback();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_socialize, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindViews();
    }

    private void bindViews() {
        pushInAnimation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.push_up_in);
        pushOutAnimation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.push_up_out);

        trySetupSwipeRefresh();

        signInOrTweetButton = rootView.findViewById(R.id.signInOrTweet);
        signInOrTweetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (tweeter.isTwitterLoggedInAlready()) {
                    showTweetWindow();
                } else {
                    loginToTwitter();
                }
            }
        });

        tweetMessageLayout = rootView.findViewById(R.id.tweet_message_layout);
        tweetMessageView = (EditText) rootView.findViewById(R.id.tweet_message);
        tweetButton = rootView.findViewById(R.id.tweet);
        tweetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                hideTweetWindow();
                tweet();
            }
        });

        cancelButton = rootView.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                hideTweetWindow();
            }
        });
    }

    private void handleTwitterCallback() {
        if (!tweeter.isTwitterLoggedInAlready()) {
            if (isLaunchedFromTwitterCallbackUrl(getActivity().getIntent().getData())) {
                retrieveAccessToken(getActivity().getIntent().getData());
                showTweetWindow();
            }
        }
    }

    private void tweet() {
        String text = tweetMessageView.getText().toString();
        tweetMessageView.setText("");
        if (text.length() > 0) {
            tweet(AddTagsIfNeeded(text));
            tweetMessageLayout.setVisibility(View.GONE);
        }
    }

    private void trySetupSwipeRefresh() {
        swipeRefreshLayout = (MultiSwipeRefreshLayout) rootView;
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setColorScheme(
                    R.color.refresh_progress_1,
                    R.color.refresh_progress_2,
                    R.color.refresh_progress_3,
                    R.color.refresh_progress_4);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refresh();
                }
            });

            swipeRefreshLayout.setCanChildScrollUpCallback(this);
        }
    }

    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        return isTweetWindowVisible || getListView().getFirstVisiblePosition() != 0;
    }

    private void refresh() {
        if (!isRefreshInProgress) {
            isRefreshInProgress = true;
            loadRecentTweets();
        }
    }

    private void onRefreshFinished(List<Status> tweets) {
        tweetsAdapter.insertAtTheTop(tweets);
        swipeRefreshLayout.setRefreshing(false);
        isRefreshInProgress = false;
    }

    private boolean isLaunchedFromTwitterCallbackUrl(Uri uri) {
        return uri != null && uri.toString().startsWith(Tweeter.TWITTER_CALLBACK_URL);
    }

    private void setupAdapter() {
        tweetsAdapter = new TweetsAdapter(this.getActivity(), new ArrayList<Status>());
        setListAdapter(tweetsAdapter);

        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisible, int visibleCount, int totalCount) {
                int padding = 5;
                if (totalCount > 10
                        && shouldLoadMore(firstVisible, visibleCount, totalCount, padding)) {
                    twitterSearchNext();
                }
            }
        });

        twitterSearch(TWITTER_SEARCH_TERM);

    }

    private AwayDayApplication getApplication() {
        return (AwayDayApplication) getActivity().getApplication();
    }

    private String AddTagsIfNeeded(String text) {
        if (!text.contains(AWAYDAY_TWITTER_TAG)) {
            text += " " + AWAYDAY_TWITTER_TAG;
        }
        return text;
    }

    private void showTweetWindow() {
        isTweetWindowVisible = true;
        tweetMessageLayout.setVisibility(View.VISIBLE);
        pushInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tweetMessageLayout.requestFocus();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        tweetMessageLayout.startAnimation(pushInAnimation);
    }

    private void hideTweetWindow() {
        isTweetWindowVisible = false;
        pushOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tweetMessageLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        tweetMessageLayout.startAnimation(pushOutAnimation);
    }

    private boolean shouldLoadMore(int firstVisible, int visibleCount, int totalCount, int padding) {
        return firstVisible + visibleCount + padding >= totalCount;
    }

    private void loginToTwitter() {
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

    public void retrieveAccessToken(final Uri uri) {
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
        if (!tweeter.hasMoreResults()) {
            return;
        }

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

    private void loadRecentTweets() {
        new AsyncTask<Void, Void, List<Status>>() {

            @Override
            protected List<twitter4j.Status> doInBackground(Void... voids) {
                return tweeter.getRecentTweets(TWITTER_SEARCH_TERM);
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> tweets) {
                onRefreshFinished(tweets);
            }
        }.execute();
    }

    private void tweet(final String tweetMessage) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                tweeter.tweet(tweetMessage);
                return null;
            }
        }.execute();

    }
}
