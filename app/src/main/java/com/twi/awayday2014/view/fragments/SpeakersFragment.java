package com.twi.awayday2014.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.adapters.SpeakersAdapter;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.services.parse.ParseDataListener;
import com.twi.awayday2014.services.parse.PresenterParseDataFetcher;
import com.twi.awayday2014.view.SpeakerDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class SpeakersFragment extends BaseListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private SpeakersAdapter speakersAdapter;
    private SpeakersDataListener parseDataListener;

    public static Fragment newInstance(int sectionNumber) {
        SpeakersFragment fragment = new SpeakersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SpeakersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listView = (ListView) inflater.inflate(R.layout.fragment_speakers, container, false);
        header = inflater.inflate(R.layout.view_fake_header, listView, false);
        return listView;
    }

    @Override
    public void onStart() {
        super.onStart();
        AwayDayApplication application = (AwayDayApplication) getActivity().getApplication();
        PresenterParseDataFetcher presenterParseDataFetcher = application.getPresenterParseDataFetcher();
        if (presenterParseDataFetcher.isDataFetched()) {
            speakersAdapter.onDataChange(presenterParseDataFetcher.getFetchedData());
        }else {
            parseDataListener = new SpeakersDataListener();
            presenterParseDataFetcher.addListener(parseDataListener);
            presenterParseDataFetcher.fetchData();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(parseDataListener != null){
            AwayDayApplication application = (AwayDayApplication) getActivity().getApplication();
            PresenterParseDataFetcher presenterParseDataFetcher = application.getPresenterParseDataFetcher();
            presenterParseDataFetcher.removeListener(parseDataListener);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    protected ListAdapter getAdapter() {
        if (speakersAdapter == null) {
            speakersAdapter = new SpeakersAdapter(this.getActivity(), new ArrayList<Presenter>());
        }
        return speakersAdapter;
    }

    @Override
    protected void onListItemClick(AdapterView<?> parent, View view, int position, long id) {
        Presenter presenter = (Presenter) getListView().getAdapter().getItem(position);
        startActivity(new Intent(getActivity(), SpeakerDetailsActivity.class).putExtra("presenter_id", presenter.getId()));
    }

    private class SpeakersDataListener implements ParseDataListener<Presenter> {

        @Override
        public void onDataValidation(boolean status) {

        }

        @Override
        public void onDataFetched(List<Presenter> presenters) {
            speakersAdapter.onDataChange(presenters);
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
    }
}
