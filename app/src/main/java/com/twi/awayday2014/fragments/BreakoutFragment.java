package com.twi.awayday2014.fragments;

import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import android.app.Fragment;
import com.twi.awayday2014.RandomColorSelector;
import com.twi.awayday2014.adapters.SessionsAdapter;

public class BreakoutFragment extends ListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static BreakoutFragment newInstance(int sectionNumber) {
        BreakoutFragment fragment = new BreakoutFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public BreakoutFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();

        setListAdapter(getAdapter());

        restoreListViewPosition();
    }
    @Override
    public void onPause() {
        super.onPause();

        saveListViewCurrentPosition();
    }

    private void restoreListViewPosition() {
        SharedPreferences sharedPreference = getSharedPreference();
        int index = sharedPreference.getInt("index", 0);
        int top = sharedPreference.getInt("top", 0);
        getListView().setSelectionFromTop(index, top);
    }

    private void saveListViewCurrentPosition() {
        int index = getListView().getFirstVisiblePosition();
        View topView = getListView().getChildAt(0);
        int top = topView == null ? 0 : topView.getTop();
        saveListViewCurrentPosition(index, top);
    }

    private void saveListViewCurrentPosition(int index, int top) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putInt("index", index);
        editor.putInt("top", top);
        editor.commit();
    }

    private android.content.SharedPreferences getSharedPreference() {
        return getActivity().getSharedPreferences("breakout_fragment", Context.MODE_PRIVATE);
    }

    private SessionsAdapter getAdapter() {
        return new SessionsAdapter(this.getActivity(), getApplication().getSessionOrganizer(), new RandomColorSelector());
    }

    private AwayDayApplication getApplication() {
        return (AwayDayApplication) this.getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        return rootView;
    }
}
