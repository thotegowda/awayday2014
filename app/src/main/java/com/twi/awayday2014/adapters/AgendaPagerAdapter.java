package com.twi.awayday2014.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.twi.awayday2014.view.fragments.AgendaTimelineFragment;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

public class AgendaPagerAdapter extends FragmentPagerAdapter {

    private Map<Integer, DateTime> dayMap;

    public AgendaPagerAdapter(FragmentManager fm) {
        super(fm);
        dayMap = new HashMap<Integer, DateTime>();
        dayMap.put(0, new DateTime().withDayOfMonth(19).withMonthOfYear(9));
        dayMap.put(1, new DateTime().withDayOfMonth(20).withMonthOfYear(9));
        dayMap.put(2, new DateTime().withDayOfMonth(21).withMonthOfYear(9));
    }


    @Override
    public Fragment getItem(int i) {
        return AgendaTimelineFragment.newInstance(dayMap.get(i), i);
    }

    @Override
    public int getCount() {
        return dayMap.size();
    }

    @Override
    public long getItemId(int position) {
        return dayMap.get(position).hashCode();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return dayMap.get(position).toString("MMM dd");
    }
}
