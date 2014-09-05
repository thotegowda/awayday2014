package com.twi.awayday2014.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.adapters.ViewPagerAdapter;
import com.twi.awayday2014.services.parse.AgendaParseDataFetcher;

public class AgendaFragment extends ViewPagerFragment{
    private static final String TAG = "AgendaFragment";

    public static AgendaFragment newInstance(int sectionNumber) {
        return new AgendaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootLayout = super.onCreateView(inflater, container, savedInstanceState);
        pager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        return rootLayout;
    }
}
