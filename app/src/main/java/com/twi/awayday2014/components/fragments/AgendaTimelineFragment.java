package com.twi.awayday2014.components.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ScrollView;
import com.twi.awayday2014.R;
import com.twi.awayday2014.adapters.AgendaAdapter;
import com.twi.awayday2014.components.activities.NewHomeActivity;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import static com.twi.awayday2014.components.activities.NewHomeActivity.CustomActionbarState;


public class AgendaTimelineFragment extends BaseListFragment {
    private static final String TAG = "AgendaTimelineFragment";
    private static final String DAY = "day";
    private static final String POSITION = "position";

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
        DateTime date = ISODateTimeFormat.dateTime().parseDateTime(getArguments().getString(DAY));
        return new AgendaAdapter(getActivity(), date);
    }
}
