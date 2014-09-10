package com.twi.awayday2014.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.twi.awayday2014.view.fragments.AgendaTimelineFragment;
import com.twi.awayday2014.view.fragments.BreakoutTimelineFragment;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

public class BreakoutPagerAdapter extends FragmentPagerAdapter {

    private Map<Integer, String> streamMap;

    public BreakoutPagerAdapter(FragmentManager fm) {
        super(fm);
        streamMap = new HashMap<Integer, String>();
        streamMap.put(0, "P2 Track");
        streamMap.put(1, "Personal Journeys");
        streamMap.put(2, "Open Track");
    }


    @Override
    public Fragment getItem(int i) {
        return BreakoutTimelineFragment.newInstance(streamMap.get(i), i);
    }

    @Override
    public long getItemId(int position) {
        return streamMap.get(position).hashCode();
    }

    @Override
    public int getCount() {
        return streamMap.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return streamMap.get(position);
    }
}
