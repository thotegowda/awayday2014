package com.twi.awayday2014.view.fragments;

import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twi.awayday2014.R;
import com.twi.awayday2014.adapters.AgendaPagerAdapter;

public class AgendaFragment extends ViewPagerFragment{
    private static final String TAG = "AgendaFragment";

    public static AgendaFragment newInstance(int sectionNumber) {
        return new AgendaFragment();
    }

    @Override
    protected FragmentPagerAdapter getAdapter() {
        return new AgendaPagerAdapter(getChildFragmentManager());
    }

    @Override
    protected View getRootLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_agenda_viewpager, container, false);
    }
}
