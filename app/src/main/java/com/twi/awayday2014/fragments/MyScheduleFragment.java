package com.twi.awayday2014.fragments;

import android.app.ListFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.RandomColorSelector;
import com.twi.awayday2014.adapters.ScheduledSessionsAdapter;
import com.twi.awayday2014.adapters.TweetsAdapter;
import android.app.Fragment;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class MyScheduleFragment extends ListFragment {

    private static final String TAG = "AwayDay";
    private static final String ARG_SECTION_NUMBER = "section_number";


    public static MyScheduleFragment newInstance(int sectionNumber) {
        MyScheduleFragment fragment = new MyScheduleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MyScheduleFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        setListAdapter(getAdapter());
    }

    private ScheduledSessionsAdapter getAdapter() {
        return new ScheduledSessionsAdapter(getActivity(), getApplication().getSessionOrganizer(), new RandomColorSelector());
    }

    private AwayDayApplication getApplication() {
        return (AwayDayApplication)getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        return rootView;
    }

}
