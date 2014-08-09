package com.twi.awayday2014.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twi.awayday2014.DateUtil;
import com.twi.awayday2014.R;
import com.twi.awayday2014.Utils;
import twitter4j.MediaEntity;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

public class TweetsAdapter extends BaseAdapter implements View.OnClickListener {

    private static final String TAG = "AwayDayTwitter";
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
        TweetViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.tweet_item, viewGroup, false);
            holder = new TweetViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (TweetViewHolder) view.getTag();
        }

        bindTweet(i, view, holder);

        return view;
    }

    private void bindTweet(int i, View view, TweetViewHolder holder) {
        Status tweet = tweets.get(i);
        holder.userView.setText(tweet.getUser().getScreenName());
        holder.textView.setText(tweet.getText());
        String date = DateUtil.formatDate(tweet.getCreatedAt());
        holder.dateView.setText(date);

        String mediaURL = null;
        MediaEntity[] mediaEntities = tweet.getMediaEntities();
        for (MediaEntity mediaEntity : mediaEntities) {
             mediaURL = mediaEntity.getMediaURL();
        }

        Picasso.with(context)
                .load(tweet.getUser().getProfileImageURL())
                .placeholder(R.drawable.tw_profile_placeholder)
                .into(holder.profileView);

        if (mediaURL != null) {
            holder.photoView.setVisibility(View.VISIBLE);
            int height = (int) context.getResources().getDimension(R.dimen.tweet_list_item_height_large);
            view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height));
            Log.d(TAG, "loading the url : " + mediaURL);
            Picasso.with(context)
                    .load(mediaURL)
                    .into(holder.photoView);
            holder.photoView.setTag(mediaURL);
            holder.photoView.setOnClickListener(this);
        } else {
            holder.photoView.setVisibility(View.GONE);
            int height = (int) context.getResources().getDimension(R.dimen.tweet_list_item_height_normal);
            view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height));
        }
    }

    @Override
    public void onClick(View view) {
        Utils.launchPhotoViewer(context, (String) view.getTag());
    }

    private class TweetViewHolder {
        public TextView userView;
        public TextView textView;
        public TextView dateView;
        public ImageView profileView;
        public ImageView photoView;

        public TweetViewHolder(View tweetView) {
            userView = (TextView) tweetView.findViewById(R.id.tweeted_by);
            textView = (TextView) tweetView.findViewById(R.id.tweet_text);
            dateView = (TextView) tweetView.findViewById(R.id.tweet_time);
            profileView = (ImageView) tweetView.findViewById(R.id.profile_thumbnail);
            photoView = (ImageView) tweetView.findViewById(R.id.tweet_image);
        }
    }
}
