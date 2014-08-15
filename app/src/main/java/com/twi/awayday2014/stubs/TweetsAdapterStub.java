package com.twi.awayday2014.stubs;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.twi.awayday2014.DateUtil;
import com.twi.awayday2014.R;
import com.twi.awayday2014.adapters.TweetsAdapter;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TweetsAdapterStub  extends TweetsAdapter {


    private Context context;
    int i = 10;

    public TweetsAdapterStub(Context context) {
        super(context, new ArrayList<Status>(), new OnTweetClickListener() {
            @Override
            public void onPhotoClicked(View photoView, String url) {

            }
        });

        this.context = context;
    }

    @Override
    public int getCount() {
      return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.tweet_item, viewGroup, false);
        bindView(view, i);
        return view;
    }

    private void bindView(View tweetView, int i) {
        TextView userView = (TextView) tweetView.findViewById(R.id.tweeted_by);
        TextView textView = (TextView) tweetView.findViewById(R.id.tweet_text);
        TextView dateView = (TextView) tweetView.findViewById(R.id.tweet_time);
        ImageView profileView = (ImageView) tweetView.findViewById(R.id.profile_thumbnail);
        ImageView photoView = (ImageView) tweetView.findViewById(R.id.tweet_image);

        userView.setText("User "  + i);
        textView.setText("Tweet " + i );
        dateView.setText(i + "m");

    }

    public void addMore(int number) {
        i += number;
        notifyDataSetChanged();
    }
}
