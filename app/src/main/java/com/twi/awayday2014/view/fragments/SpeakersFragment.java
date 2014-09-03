package com.twi.awayday2014.view.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.adapters.SpeakersAdapter;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.services.AgendaService;
import com.twi.awayday2014.view.SpeakerDetailsActivity;

import java.util.Arrays;

public class SpeakersFragment extends BaseListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

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
    protected ListAdapter getAdapter() {
        return new SpeakersAdapter(this.getActivity(), getAgendaService().getSpeakers());
    }

    private AgendaService getAgendaService() {
        return ((AwayDayApplication)getActivity().getApplication()).getAgendaService();
    }

    @Override
    protected void onListItemClick(AdapterView<?> parent, View view, int position, long id) {
        Presenter presenter = (Presenter) getListView().getAdapter().getItem(position);
        startActivity(new Intent(getActivity(), SpeakerDetailsActivity.class).putExtra("presenter_id", String.valueOf(presenter.getId())));
    }
}
