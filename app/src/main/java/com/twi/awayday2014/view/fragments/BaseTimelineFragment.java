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
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.models.Session;
import com.twi.awayday2014.services.parse.ParseDataListener;
import com.twi.awayday2014.services.parse.PresenterParseDataFetcher;
import com.twi.awayday2014.utils.Fonts;
import com.twi.awayday2014.view.HomeActivity;
import com.twi.awayday2014.view.SessionDetailsActivity;

import java.util.List;

public abstract class BaseTimelineFragment extends BaseListFragment{
    protected static final String POSITION = "position";
    protected static float viewpagerIndicatorheight;
    private SpeakersDataListener speakersDataListener;

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
    public void onStart() {
        super.onStart();
        AwayDayApplication application = (AwayDayApplication) getActivity().getApplication();
        PresenterParseDataFetcher presenterParseDataFetcher = application.getPresenterParseDataFetcher();
        speakersDataListener = new SpeakersDataListener();
        presenterParseDataFetcher.addListener(speakersDataListener);
        presenterParseDataFetcher.fetchData();
    }

    @Override
    public void onStop() {
        super.onStop();
        AwayDayApplication application = (AwayDayApplication) getActivity().getApplication();
        PresenterParseDataFetcher presenterParseDataFetcher = application.getPresenterParseDataFetcher();
        presenterParseDataFetcher.removeListener(speakersDataListener);
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
    protected View getHeaderView(LayoutInflater inflater, ListView listView) {
        return inflater.inflate(R.layout.view_fake_header_for_viewpager_child, listView, false);
    }

    @Override
    protected View getRootLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    protected float getExtraScrollPos() {
        return BreakoutTimelineFragment.viewpagerIndicatorheight;
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

    protected abstract void onPresentersFetched(List<Presenter> presenters);

    private void launchSessionDetails(String id) {
        getActivity().startActivity(
                new Intent(getActivity(), SessionDetailsActivity.class).putExtra("session_id", id));
    }

    private class SpeakersDataListener implements ParseDataListener<Presenter> {

        @Override
        public void onDataValidation(boolean status) {

        }

        @Override
        public void onDataFetched(List<Presenter> presenters) {
            onPresentersFetched(presenters);
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
