package com.twi.awayday2014.adapters;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import com.twi.awayday2014.components.fragments.AgendaTimelineFragment;
import org.joda.time.DateTime;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int i) {
        DateTime day1 = new DateTime().withDayOfMonth(27).withMonthOfYear(9);
        DateTime day2 = new DateTime().withDayOfMonth(28).withMonthOfYear(9);
        if (i == 0) {
            return AgendaTimelineFragment.newInstance(day1, i);
        } else if (i == 1) {
            return AgendaTimelineFragment.newInstance(day2, i);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
