package com.twi.awayday2014.fragments;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.AbsListView;
import com.twi.awayday2014.adapters.TweetsAdapter;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Fragment;
import java.util.ArrayList;

public class SocializeFragment extends ListFragment {

    private static final String TAG = "AwayDay";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TWITTER_SEARCH_TERM = "ThoughtWorks";

    private Twitter twitter;
    private TweetsAdapter tweetsAdapter;
    private QueryResult lastResponse;

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

        twitter = setupTwitter();
        asyncSearch(TWITTER_SEARCH_TERM);

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
                    if (lastResponse == null) {
                        asyncSearch(TWITTER_SEARCH_TERM);
                    } else {
                        asyncSearch(lastResponse.nextQuery());
                    }
                }
            }
        });
    }

    private boolean shouldLoadMore(int firstVisible, int visibleCount, int totalCount, int padding) {
        return firstVisible + visibleCount + padding >= totalCount;
    }

    private void asyncSearch(final String term) {
       asyncSearch(new Query(term));
    }

    private void asyncSearch(final Query query) {
        new AsyncTask<Void, Void, QueryResult>() {
            @Override
            protected QueryResult doInBackground(Void... voids) {
                return searchTwitter(query);
            }

            @Override
            protected void onPostExecute(QueryResult result) {
                lastResponse = result;
                tweetsAdapter.append(result.getTweets());
            }
        }.execute();
    }

    private QueryResult searchTwitter(Query query) {
        QueryResult result = null;
        try {
            result = twitter.search(query);
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return result;
    }

    private Twitter setupTwitter() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("hJs1Fhwwmg3QDMBKX0TgaINuq")
                .setOAuthConsumerSecret("PITREV1N69WxEn7U9c685a661ffjVIdWOgdDNJCUehEw6mCfM3")
                .setOAuthAccessToken("97134656-9tMIEXQosam5TZMIrR7NA2DYIn14dsaSKvIfTlEMU")
                .setOAuthAccessTokenSecret("y1nMS5ricaXRjjNH4kUxpgl9EtppjdkUOM98eYcQ3KWnM");
        return new TwitterFactory(cb.build()).getInstance();
    }

}
