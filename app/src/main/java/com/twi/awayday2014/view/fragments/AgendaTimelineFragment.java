package com.twi.awayday2014.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.adapters.AgendaAdapter;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.models.Session;
import com.twi.awayday2014.services.parse.AgendaParseDataFetcher;
import com.twi.awayday2014.services.parse.ParseDataListener;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.view.View.VISIBLE;

public class AgendaTimelineFragment extends BaseTimelineFragment {
    private static final String TAG = "AgendaTimelineFragment";
    private static final String DAY = "day";
    private static final String POSITION = "position";
    private AgendaAdapter agendaAdapter;
    private AgendaDataListener agendaDataListener;
    private static float viewpagerIndicatorheight;

    public static AgendaTimelineFragment newInstance(DateTime day, int position) {
        AgendaTimelineFragment fragment = new AgendaTimelineFragment();
        Bundle args = new Bundle();
        args.putString(DAY, ISODateTimeFormat.dateTime().print(day));
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected AgendaAdapter getAdapter() {
        if (agendaAdapter == null) {
            agendaAdapter = new AgendaAdapter(getActivity(), new ArrayList<Session>());
        }
        return agendaAdapter;
    }

    @Override
    public void onStart() {
        super.onStart();
        AwayDayApplication application = (AwayDayApplication) getActivity().getApplication();
        AgendaParseDataFetcher agendaParseDataFetcher = application.getAgendaParseDataFetcher();
        agendaDataListener = new AgendaDataListener();
        agendaParseDataFetcher.addListener(agendaDataListener);
        if(agendaParseDataFetcher.isDataFetched()){
            List<Session> fetchedData = agendaParseDataFetcher.getFetchedData();
            agendaDataListener.onDataFetched(fetchedData, false);
        }else {
            agendaParseDataFetcher.fetchData();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        AwayDayApplication application = (AwayDayApplication) getActivity().getApplication();
        AgendaParseDataFetcher agendaParseDataFetcher = application.getAgendaParseDataFetcher();
        agendaParseDataFetcher.removeListener(agendaDataListener);
    }

    @Override
    protected void onPresentersFetched(List<Presenter> presenters) {
        agendaAdapter.presentersInfoFetched(presenters);
    }

    private List<Session> getSortedSessionsForDay(List<Session> sessions, String day) {
        List<Session> result = new ArrayList<Session>();
        DateTimeFormatter dateTimeParser = ISODateTimeFormat.dateTimeParser();
        DateTime agendaDay = dateTimeParser.parseDateTime(day);
        for (Session session : sessions) {
            DateTime dateTime = dateTimeParser.parseDateTime(session.getDate());
            if (dateTime.dayOfMonth().equals(agendaDay.dayOfMonth())) {
                result.add(session);
            }
        }
        Collections.sort(result, new Session.SessionsComparator());
        return result;
    }

    private class AgendaDataListener implements ParseDataListener<Session> {

        @Override
        public void onDataValidation(boolean status) {

        }

        @Override
        public void onDataFetched(List<Session> data, boolean actuallyFetched) {
            placeHolderView.setVisibility(View.INVISIBLE);
            listView.setVisibility(VISIBLE);
            agendaAdapter.onDataChange(getSortedSessionsForDay(data, getArguments().getString(DAY)));
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

}
