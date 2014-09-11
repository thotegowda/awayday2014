package com.twi.awayday2014.view.fragments;

import android.os.Bundle;
import android.view.View;

import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.adapters.BreakoutAdapter;
import com.twi.awayday2014.models.BreakoutSession;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.models.Session;
import com.twi.awayday2014.services.parse.BreakoutSessionsParseDataFetcher;
import com.twi.awayday2014.services.parse.ParseDataListener;

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
            breakoutAdapter = new BreakoutAdapter(getActivity(), new ArrayList<Session>(), listView);
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
        if(breakoutSessionsParseDataFetcher.isDataFetched()){
            List<BreakoutSession> fetchedData = breakoutSessionsParseDataFetcher.getFetchedData();
            breakoutDataListener.onDataFetched(fetchedData);
        }else {
            breakoutSessionsParseDataFetcher.fetchData();
        }

        getAdapter().onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        AwayDayApplication application = (AwayDayApplication) getActivity().getApplication();
        BreakoutSessionsParseDataFetcher breakoutSessionsParseDataFetcher = application.getBreakoutSessionsParseDataFetcher();
        breakoutSessionsParseDataFetcher.removeListener(breakoutDataListener);
        
        getAdapter().onStop();
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

    private List<Session> getSortedSessionsForStream(List<BreakoutSession> sessions, String stream) {
        List<Session> result = new ArrayList<Session>();
        for (BreakoutSession session : sessions) {
            if (session.getStream().equals(stream)) {
                result.add(session);
            }
        }
        Collections.sort(result, new Session.SessionsComparator());
        return result;
    }

}
