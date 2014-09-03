package com.twi.awayday2014.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.adapters.AgendaAdapter;
import com.twi.awayday2014.models.Session;
import com.twi.awayday2014.services.AgendaService;
import com.twi.awayday2014.view.SessionDetailsActivity;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;


public class AgendaTimeLineFragment extends BaseListFragment {
    private static final String TAG = "AgendaTimelineFragment";
    private static final String DAY = "day";
    private static final String POSITION = "position";

    public static AgendaTimeLineFragment newInstance(DateTime day, int position) {
        AgendaTimeLineFragment fragment = new AgendaTimeLineFragment();
        Bundle args = new Bundle();
        args.putString(DAY, ISODateTimeFormat.dateTime().print(day));
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected AgendaAdapter getAdapter() {
        DateTime date = ISODateTimeFormat.dateTime().parseDateTime(getArguments().getString(DAY));
        return new AgendaAdapter(getActivity(), getAgendaService(), date);
    }

    private AgendaService getAgendaService() {
        return ((AwayDayApplication)getActivity().getApplication()).getAgendaService();
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
}
