package com.twi.awayday2014.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.adapters.AgendaAdapter;
import com.twi.awayday2014.adapters.BreakoutAdapter;
import com.twi.awayday2014.models.BreakoutSession;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.models.Session;
import com.twi.awayday2014.services.parse.AgendaParseDataFetcher;
import com.twi.awayday2014.services.parse.BreakoutSessionsParseDataFetcher;
import com.twi.awayday2014.services.parse.ParseDataListener;
import com.twi.awayday2014.services.parse.PresenterParseDataFetcher;
import com.twi.awayday2014.utils.Fonts;
import com.twi.awayday2014.view.HomeActivity;
import com.twi.awayday2014.view.SessionDetailsActivity;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.view.View.VISIBLE;


public class BreakoutTimelineFragment extends BaseTimelineFragment {
    private static final String TAG = "BreakoutTimelineFragment";
    private static final String STREAM = "stream";
    private BreakoutAdapter breakoutAdapter;
    private BreakoutDataListener breakoutDataListener;

    public BreakoutTimelineFragment() {
    }

    public static BreakoutTimelineFragment newInstance(String stream, int position) {
        BreakoutTimelineFragment fragment = new BreakoutTimelineFragment();
        Bundle args = new Bundle();
        args.putString(STREAM, stream);
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected BreakoutAdapter getAdapter() {
        if (breakoutAdapter == null) {
            breakoutAdapter = new BreakoutAdapter(getActivity(), new ArrayList<BreakoutSession>());
        }
        return breakoutAdapter;
    }


    @Override
    public void onStart() {
        super.onStart();
        AwayDayApplication application = (AwayDayApplication) getActivity().getApplication();
        BreakoutSessionsParseDataFetcher breakoutSessionsParseDataFetcher = application.getBreakoutSessionsParseDataFetcher();
        breakoutDataListener = new BreakoutDataListener();
        breakoutSessionsParseDataFetcher.addListener(breakoutDataListener);
        breakoutSessionsParseDataFetcher.fetchData();
    }

    @Override
    public void onStop() {
        super.onStop();
        AwayDayApplication application = (AwayDayApplication) getActivity().getApplication();
        BreakoutSessionsParseDataFetcher breakoutSessionsParseDataFetcher = application.getBreakoutSessionsParseDataFetcher();
        breakoutSessionsParseDataFetcher.removeListener(breakoutDataListener);
    }

    @Override
    protected void onPresentersFetched(List<Presenter> presenters) {
        breakoutAdapter.presentersInfoFetched(presenters);
    }

    private class BreakoutDataListener implements ParseDataListener<BreakoutSession> {

        @Override
        public void onDataValidation(boolean status) {

        }

        @Override
        public void onDataFetched(List<BreakoutSession> data) {
            placeHolderView.setVisibility(View.INVISIBLE);
            listView.setVisibility(VISIBLE);
            breakoutAdapter.onDataChange(getSortedSessionsForStream(data, getArguments().getString(STREAM)));
        }

        @Override
        public void onDataFetchError(int errorStatus) {

        }

        @Override
        public void fetchingFromNetwork() {
            listView.setVisibility(View.INVISIBLE);
            placeHolderView.setVisibility(VISIBLE);
            placeHolderText.setText("Fetching Agenda");
        }

        @Override
        public void fetchingFromCache() {

        }

        @Override
        public void onDataValidationError(int errorStatus) {

        }

        @Override
        public void dataIsOutdated() {

        }
    }

    private List<BreakoutSession> getSortedSessionsForStream(List<BreakoutSession> sessions, String stream) {
        List<BreakoutSession> result = new ArrayList<BreakoutSession>();
        for (BreakoutSession session : sessions) {
            if (session.getStream().equals(stream)) {
                result.add(session);
            }
        }
        Collections.sort(result, new Session.SessionsComparator());
        return result;
    }

}
