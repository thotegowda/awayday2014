package com.twi.awayday2014.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.twi.awayday2014.R;
import twitter4j.Status;

import java.util.List;

public class TweetsAdapter extends BaseAdapter {

    private List<Status> tweets;
    private LayoutInflater inflater;

    public TweetsAdapter(Context context, List<Status> tweets) {
        this.tweets = tweets;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void append(List<Status> moreTweets) {
        tweets.addAll(moreTweets);
        notifyDataSetChanged();
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

        //((ImageView)view.findViewById(R.id.video_thumbnail)).setImageResource(tweets.get(i).thumbnail());
        ((TextView)view.findViewById(R.id.tweeted_by)).setText(tweets.get(i).getUser().getScreenName());
        ((TextView)view.findViewById(R.id.tweet_text)).setText(tweets.get(i).getText());
        view.setTag(tweets.get(i));
        return view;
    }
}
