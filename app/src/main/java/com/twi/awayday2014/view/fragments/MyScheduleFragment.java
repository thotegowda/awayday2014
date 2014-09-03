package com.twi.awayday2014.view.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.adapters.ScheduledSessionsAdapter;

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

    }

    private AwayDayApplication getApplication() {
        return (AwayDayApplication)getActivity().getApplication();
    }


}
