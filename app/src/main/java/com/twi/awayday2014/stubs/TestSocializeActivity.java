package com.twi.awayday2014.stubs;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import com.twi.awayday2014.customviews.MultiSwipeRefreshLayout;
import com.twi.awayday2014.customviews.SwipeRefreshLayout;

public class TestSocializeActivity extends Activity {
    public static final String TAG = "TestActivity";
    private long iteration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final ListView listView = new ListView(this);
        final TweetsAdapterStub adapter = new TweetsAdapterStub(this);
        listView.setAdapter(adapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int first, int visible, int total) {
                //Log.d(TAG, " onScroll: first: " + first + " visible: " + visible + " total: " + total);

                if (first + visible >= (total - 5) && shouldLoadMore()) {
                    Log.d(TAG, "loading more: ");
                }

            }
        });


        final MultiSwipeRefreshLayout swipeLayout = new MultiSwipeRefreshLayout(this);
        swipeLayout.setCanChildScrollUpCallback(new MultiSwipeRefreshLayout.CanChildScrollUpCallback() {
            @Override
            public boolean canSwipeRefreshChildScrollUp() {
                //Log.d(TAG, "isFirstTweetVisible : " + isFirstTweetVisible());
                //Log.d(TAG, "isLastTweetVisible : " + isLastTweetVisible());

                if (isLastTweetVisible()) {
                    return true;
                } else {
                    return !isFirstTweetVisibleCompletely();
                }
            }

            private boolean isFirstTweetVisibleCompletely() {
                return isFirstTweet() && isFirstTweetVisible();
            }

            private boolean isFirstTweetVisible() {
                return listView.getChildAt(0) != null ? listView.getChildAt(0).getTop() >= 0 : true;
            }

            private boolean isFirstTweet() {
                return listView.getFirstVisiblePosition() == 0;
            }

            private boolean isLastTweetVisible() {

                //Log.d(TAG, "lastVisiblePosition : " + listView.getLastVisiblePosition() + " tweets count: " + adapter.getCount());
                return listView.getLastVisiblePosition() >= adapter.getCount() - 1;
            }

        });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "Refresh now");
                adapter.addMore(5);
                swipeLayout.setRefreshing(false);
            }
        });

        swipeLayout.addView(listView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        setContentView(swipeLayout);
    }

    private boolean shouldLoadMore() {
       if (iteration >= 5) {
           return false;
       } else {
           iteration++;
           return true;
       }
    }
}
