package com.twi.awayday2014.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.models.Session;
import com.twi.awayday2014.services.parse.AgendaParseDataFetcher;
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


public class AgendaTimelineFragment extends BaseListFragment {
    private static final String TAG = "AgendaTimelineFragment";
    private static final String DAY = "day";
    private static final String POSITION = "position";
    private AgendaAdapter agendaAdapter;
    private AgendaDataListener agendaDataListener;
    private SpeakersDataListener speakersDataListener;
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeActivity)getActivity()).addParallelScrollableChild(this, getArguments().getInt(POSITION));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((HomeActivity)getActivity()).removeParallelScrollableChild(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected float getExtraScrollPos() {
        return viewpagerIndicatorheight;
    }

    @Override
    protected AgendaAdapter getAdapter() {
        if (agendaAdapter == null) {
            agendaAdapter = new AgendaAdapter(getActivity(), new ArrayList<Session>());
        }
        return agendaAdapter;
    }

    @Override
    protected void onListItemClick(AdapterView<?> parent, View view, int position, long id) {
        Session session = (Session) getListView().getAdapter().getItem(position);
        if(session == null){
            return;
        }
        if (session.getDescription() == null) {
            Toast.makeText(getActivity(), "No detailed description is available for this section", Toast.LENGTH_SHORT).show();
        } else {
            launchSessionDetails(session.getId());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        placeHolderView = rootlayout.findViewById(R.id.placeholderView);
        placeHolderText = (TextView) rootlayout.findViewById(R.id.placeholderText);
        placeHolderText.setTypeface(Fonts.openSansLight(getActivity()));
        viewpagerIndicatorheight = getResources().getDimension(R.dimen.viewpager_indicator_height);
        return rootlayout;
    }

    @Override
    protected View getHeaderView(LayoutInflater inflater, ListView listView) {
        return inflater.inflate(R.layout.view_fake_header_for_viewpager_child, listView, false);
    }

    @Override
    protected View getRootLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        AwayDayApplication application = (AwayDayApplication) getActivity().getApplication();
        AgendaParseDataFetcher agendaParseDataFetcher = application.getAgendaParseDataFetcher();
        agendaDataListener = new AgendaDataListener();
        agendaParseDataFetcher.addListener(agendaDataListener);
        agendaParseDataFetcher.fetchData();
        PresenterParseDataFetcher presenterParseDataFetcher = application.getPresenterParseDataFetcher();
        speakersDataListener = new SpeakersDataListener();
        presenterParseDataFetcher.addListener(speakersDataListener);
        presenterParseDataFetcher.fetchData();
    }

    @Override
    public void onStop() {
        super.onStop();
        AwayDayApplication application = (AwayDayApplication) getActivity().getApplication();
        AgendaParseDataFetcher agendaParseDataFetcher = application.getAgendaParseDataFetcher();
        agendaParseDataFetcher.removeListener(agendaDataListener);
    }

    private void launchSessionDetails(String id) {
        getActivity().startActivity(
                new Intent(getActivity(), SessionDetailsActivity.class).putExtra("session_id", id));
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
        public void onDataFetched(List<Session> data) {
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

    private class SpeakersDataListener implements ParseDataListener<Presenter> {

        @Override
        public void onDataValidation(boolean status) {

        }

        @Override
        public void onDataFetched(List<Presenter> presenters) {
            agendaAdapter.presentersInfoFetched(presenters);
        }

        @Override
        public void onDataFetchError(int errorStatus) {

        }

        @Override
        public void fetchingFromNetwork() {
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
