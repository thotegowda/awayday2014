package com.twi.awayday2014.view.fragments;

import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twi.awayday2014.R;
import com.twi.awayday2014.adapters.BreakoutPagerAdapter;

public class BreakoutFragment extends ViewPagerFragment{
    private static final String TAG = "AgendaFragment";

    public static BreakoutFragment newInstance(int sectionNumber) {
        return new BreakoutFragment();
    }

    @Override
    protected View getRootLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_breakout_viewpager, container, false);
    }

    @Override
    protected FragmentPagerAdapter getAdapter() {
        return new BreakoutPagerAdapter(getChildFragmentManager());
    }
}
