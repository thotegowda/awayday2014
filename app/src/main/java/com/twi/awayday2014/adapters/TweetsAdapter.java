package com.twi.awayday2014.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Tweet;
import com.twi.awayday2014.services.TweeterService;
import com.twi.awayday2014.utils.Fonts;

import java.util.List;

public class TweetsAdapter extends BaseAdapter {


    private final LayoutInflater inflater;
    private List<Tweet> tweets;
    private Context context;

    public TweetsAdapter(Context context) {
        this.context = context;
        TweeterService tweeterService = AwayDayApplication.tweeterService();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tweets = tweeterService.getTweets();
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
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewSource viewSource = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_tweets_listitem, parent, false);
            viewSource = new ViewSource();
            viewSource.tweetText = (TextView) convertView.findViewById(R.id.tweet_text);
            viewSource.tweetText.setTypeface(Fonts.openSansRegular(context));
            viewSource.detailsText = (TextView) convertView.findViewById(R.id.detailsText);
            viewSource.detailsText.setTypeface(Fonts.openSansItalic(context));
            convertView.setTag(viewSource);
        }

        viewSource = (ViewSource) convertView.getTag();
        Tweet tweet = tweets.get(position);
        viewSource.tweetText.setText(tweet.getTweet());
        viewSource.detailsText.setText(tweet.getDisplayDetails());
        return convertView;
    }

    private class ViewSource {
        TextView tweetText;
        TextView detailsText;
    }
}
