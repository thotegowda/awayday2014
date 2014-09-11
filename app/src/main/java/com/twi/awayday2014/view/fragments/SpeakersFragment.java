package com.twi.awayday2014.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.adapters.SpeakersAdapter;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.services.parse.ParseDataListener;
import com.twi.awayday2014.services.parse.PresenterParseDataFetcher;
import com.twi.awayday2014.view.HomeActivity;
import com.twi.awayday2014.view.SpeakerDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class SpeakersFragment extends BaseListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String PRESENTER_ID = "presenter_id";
    private SpeakersAdapter speakersAdapter;
    private SpeakersDataListener parseDataListener;
    private PresenterParseDataFetcher presenterParseDataFetcher;
    private StickyListHeadersListView stickListView;

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
        rootlayout = getRootLayout(inflater, container);
        stickListView = (StickyListHeadersListView) rootlayout.findViewById(R.id.list);
        listView = stickListView.getWrappedList();
        header = getHeaderView(inflater, listView);
        listView.addHeaderView(header);
        return rootlayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        stickListView.setAdapter((se.emilsjolander.stickylistheaders.StickyListHeadersAdapter) getAdapter());

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isListViewAdjustedAsPerParent && scrollListener != null && isActive) {
                    scrollListener.onScroll(SpeakersFragment.this, header.getY());
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onListItemClick(adapterView, view, i, l);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeActivity)getActivity()).addParallelScrollableChild(this, 0);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((HomeActivity)getActivity()).removeParallelScrollableChild(this);
    }

    @Override
    protected View getRootLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_speakers, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        AwayDayApplication application = (AwayDayApplication) getActivity().getApplication();
        presenterParseDataFetcher = application.getPresenterParseDataFetcher();
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
    protected float getExtraScrollPos() {
        return 0;
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
        if(presenter.getWriteUp() == null || presenter.getWriteUp().isEmpty()){
            Toast.makeText(getActivity(), "No details are available for the speaker", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(getActivity(), SpeakerDetailsActivity.class);
        intent.putExtra(PRESENTER_ID, presenter.getId());
        startActivity(intent);
    }

    @Override
    protected View getHeaderView(LayoutInflater inflater, ListView listView) {
        return inflater.inflate(R.layout.view_fake_header_standard_child, listView, false);
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

        @Override
        public void onDataValidationError(int errorStatus) {

        }

        @Override
        public void dataIsOutdated() {
        }
    }
}
