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
import com.twi.awayday2014.R;
import com.twi.awayday2014.utils.Fonts;
import com.twi.awayday2014.view.custom.CircularImageView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import twitter4j.MediaEntity;
import twitter4j.Status;

public class TweetsAdapter extends BaseAdapter {
    private static final String TAG = "TweetsAdapter";
    private final LayoutInflater inflater;
    private List<Status> tweets;
    private Context context;

    public TweetsAdapter(Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tweets = new ArrayList<Status>();
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
            viewSource.timeText = (TextView) convertView.findViewById(R.id.timeText);
            viewSource.timeText.setTypeface(Fonts.openSansItalic(context));
            viewSource.imageView = (ImageView) convertView.findViewById(R.id.image);
            viewSource.userImage = (CircularImageView) convertView.findViewById(R.id.userImage1);
            convertView.setTag(viewSource);
        }

        viewSource = (ViewSource) convertView.getTag();
        Status status = tweets.get(position);
        viewSource.tweetText.setText(status.getText());
        viewSource.detailsText.setText(status.getUser().getName() +
            " (@" + status.getUser().getScreenName() + ")");
        DateTime dateTime = new DateTime(status.getCreatedAt());
        viewSource.timeText.setText(dateTime.toString("hh:mm, dd MMM yyyy"));

        viewSource.userImage.setImageDrawable(context.getResources().getDrawable(R.drawable.placeholder));
        Picasso.with(context)
                .load(status.getUser().getBiggerProfileImageURL())
                .placeholder(R.drawable.placeholder)
                .into(viewSource.userImage);
        viewSource.userImage.setTag(status.getUser().getBiggerProfileImageURL());

        MediaEntity[] mediaEntities = status.getMediaEntities();
        String mediaURL = null;
        for (MediaEntity mediaEntity : mediaEntities) {
            mediaURL = mediaEntity.getMediaURL();
        }
        if (mediaURL != null) {
            viewSource.imageView.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(mediaURL)
                    .placeholder(R.drawable.placeholder_twitter)
                    .into(viewSource.imageView);

            viewSource.imageView.setTag(mediaURL);
        } else {
            viewSource.imageView.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void append(List<Status> moreTweets){
        if(moreTweets.size() == 0){
            return;
        }

        tweets.addAll(moreTweets);
        notifyDataSetChanged();
    }

    public void insertAtTheTop(List<Status> moreTweets) {
        if (moreTweets.size() > 0) {
            Log.d(TAG, "new tweets added count : " + moreTweets.size());

            List<Status> newTweets = new ArrayList<Status>(moreTweets);
            newTweets.addAll(tweets);
            tweets = newTweets;

            notifyDataSetChanged();
        } else {
            Log.d(TAG, "no new tweets available ");
        }
    }


    private class ViewSource {
        TextView tweetText;
        TextView detailsText;
        TextView timeText;
        ImageView imageView;
        CircularImageView userImage;
    }
}
