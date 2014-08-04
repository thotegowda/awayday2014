package com.twi.awayday2014.fragments;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import android.app.Fragment;
import com.twi.awayday2014.RandomColorSelector;
import com.twi.awayday2014.adapters.SessionsAdapter;
import com.twi.awayday2014.models.Presentation;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.ui.SessionDetailsActivity;

import java.util.Arrays;
import java.util.List;

public class AgendaFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static AgendaFragment newInstance(int sectionNumber) {
        AgendaFragment fragment = new AgendaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public AgendaFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_other, container, false);
        return rootView;
    }

}
