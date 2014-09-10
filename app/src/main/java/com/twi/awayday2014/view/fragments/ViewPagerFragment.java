package com.twi.awayday2014.view.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twi.awayday2014.R;
import com.twi.awayday2014.utils.Fonts;
import com.twi.awayday2014.view.HomeActivity;
import com.twi.awayday2014.view.custom.PagerSlidingTabStrip;
import com.twi.awayday2014.view.custom.ScrollListener;
import com.twi.awayday2014.view.custom.ScrollableView;

public abstract class ViewPagerFragment extends Fragment implements ScrollListener {
    private View rootLayout;
    protected ViewPager pager;
    private int scrollablePagerIndicatorDistance;
    private PagerSlidingTabStrip mPageSlidingTabStrip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootLayout = getRootLayout(inflater, container);
        rootLayout.findViewById(R.id.pager);
        pager = (ViewPager) rootLayout.findViewById(R.id.pager);
        pager.setAdapter(getAdapter());
        setupPagerSlider();
        return rootLayout;
    }

    private void setupPagerSlider() {
        mPageSlidingTabStrip = (PagerSlidingTabStrip) rootLayout.findViewById(R.id.pager_indicator);
        mPageSlidingTabStrip.setTextSize((int) getActivity().getResources().getDimension(R.dimen.viewpager_indicator_textsize));
        mPageSlidingTabStrip.setTextColor(Color.WHITE);
        mPageSlidingTabStrip.setSelectedTextColor(Color.WHITE);
        mPageSlidingTabStrip.setTypeface(Fonts.openSansRegular(getActivity()), -1);
        mPageSlidingTabStrip.setViewPager(pager);
        mPageSlidingTabStrip.setOnPageChangeListener(new Listener());
        mPageSlidingTabStrip.makeCurrentSelected(0);
    }

    protected abstract View getRootLayout(LayoutInflater inflater, ViewGroup container);
    protected abstract FragmentPagerAdapter getAdapter();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        scrollablePagerIndicatorDistance = (int) getResources().getDimension(R.dimen.home_activity_header_height_without_countdown)
                - (int) getResources().getDimension(R.dimen.home_activity_header_bar_height);

        // This is a dirty workaround but fragment references in viewpager become invalid on config change (eg device rotation), so they have to directly
        // register themselves to activity
        ((HomeActivity) getActivity()).setDelegateListener(this);
    }

    @Override
    public void onScroll(ScrollableView scrollableView, float y) {
        if (-y <= scrollablePagerIndicatorDistance) {
            mPageSlidingTabStrip.setTranslationY(y);
        } else {
            mPageSlidingTabStrip.setTranslationY(-scrollablePagerIndicatorDistance);
        }
    }

    @Override
    public void addParallelScrollableChild(ScrollableView scrollableView, int position) {

    }

    @Override
    public void removeParallelScrollableChild(ScrollableView scrollableView) {

    }

    private class Listener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int i) {
            ((HomeActivity) getActivity()).setCurrentParallelScrollableChild(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }
}
