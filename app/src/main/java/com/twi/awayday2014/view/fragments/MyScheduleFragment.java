package com.twi.awayday2014.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.adapters.ScheduledSessionsAdapter;
import com.twi.awayday2014.models.Session;
import com.twi.awayday2014.view.SessionDetailsActivity;

public class MyScheduleFragment extends BaseListFragment {

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

    protected ListAdapter getAdapter() {
        return new ScheduledSessionsAdapter(getActivity(), getApplication().getAgendaService());
    }

    @Override
    protected void onListItemClick(AdapterView<?> parent, View view, int position, long id) {
        Session session = (Session) getListView().getAdapter().getItem(position);
        launchSessionDetails(session.getId());
    }

    private void launchSessionDetails(Long id) {
        getActivity().startActivity(
                new Intent(getActivity(), SessionDetailsActivity.class).putExtra("session_id", String.valueOf(id)));
    }

    private AwayDayApplication getApplication() {
        return (AwayDayApplication)getActivity().getApplication();
    }


}
