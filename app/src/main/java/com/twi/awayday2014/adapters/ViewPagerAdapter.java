package com.twi.awayday2014.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.twi.awayday2014.view.fragments.AgendaTimeLineFragment;

import org.joda.time.DateTime;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int i) {
        DateTime day1 = new DateTime().withDayOfMonth(19).withMonthOfYear(9);
        DateTime day2 = new DateTime().withDayOfMonth(20).withMonthOfYear(9);
        if (i == 0) {
            return AgendaTimeLineFragment.newInstance(day1, i);
        } else if (i == 1) {
            return AgendaTimeLineFragment.newInstance(day2, i);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
