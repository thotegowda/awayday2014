package com.twi.awayday2014.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twi.awayday2014.DateUtil;
import com.twi.awayday2014.R;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

public class TweetsAdapter extends BaseAdapter {

    private static final String TAG = "Twitter";
    private Context context;
    private List<Status> tweets;
    private LayoutInflater inflater;

    public TweetsAdapter(Context context, List<Status> tweets) {
        this.context = context;
        this.tweets = tweets;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void append(List<Status> moreTweets) {
        tweets.addAll(moreTweets);
        notifyDataSetChanged();
    }

    public void insertAtTheTop(List<Status> moreTweets) {
        if (moreTweets.size() > 1) {
            Log.d(TAG, "new tweets added count : " + moreTweets.size());

            List<Status> newTweets = new ArrayList<Status>(moreTweets);
            newTweets.addAll(tweets);
            tweets = newTweets;

            notifyDataSetChanged();
        } else {
            Log.d(TAG, "no new tweets available ");
        }
    }

    @Override
    public int getCount() {
        return tweets.size();
    }

    @Override
    public Object getItem(int i) {
        return tweets.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       if (view == null) {
           view = inflater.inflate(R.layout.tweet_item, viewGroup, false);
       }

        Status tweet = tweets.get(i);
        ((TextView)view.findViewById(R.id.tweeted_by)).setText(tweet.getUser().getScreenName());
        ((TextView)view.findViewById(R.id.tweet_text)).setText(tweet.getText());
        String date = DateUtil.formatDate(tweet.getCreatedAt());
        ((TextView)view.findViewById(R.id.tweet_time)).setText(date);
        view.setTag(tweet);

        Picasso.with(context)
                .load(tweet.getUser().getProfileImageURL())
                .placeholder(R.drawable.tw_profile_placeholder)
                .into((ImageView) view.findViewById(R.id.profile_thumbnail));

        return view;
    }
}
