package com.twi.awayday2014.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twi.awayday2014.R;
import com.twi.awayday2014.view.HomeActivity;
import com.twi.awayday2014.view.custom.ScrollListener;
import com.twi.awayday2014.view.custom.ScrollableView;

public class ViewPagerFragment extends Fragment implements ScrollListener {

    private View rootLayout;
    private View leftIndicator;
    private View rightIndicator;
    private View pagerIndicator;
    protected ViewPager pager;
    private int scrollablePagerIndicatorDistance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootLayout = inflater.inflate(R.layout.fragment_viewpager, container, false);
        rootLayout.findViewById(R.id.pager);
        pager = (ViewPager) rootLayout.findViewById(R.id.pager);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                selectIndicator(position);
                ((HomeActivity)getActivity()).setCurrentParallelScrollableChild(position);
            }
        });
        setupIndicators();
        setupIndiacatorButtons();
        return rootLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // This is a dirty workaround but fragment references in viewpager become invalid on config change (eg device rotation), so they have to directly
        // register themselves to activity
        scrollablePagerIndicatorDistance = (int) getResources().getDimension(R.dimen.home_activity_header_height_without_countdown)
                        - (int) getResources().getDimension(R.dimen.home_activity_header_bar_height);
        ((HomeActivity)getActivity()).setDelegateListener(this);
    }

    private void setupIndiacatorButtons() {
        View leftIndicatorButton = rootLayout.findViewById(R.id.leftIndicatorButton);
        View rightIndicatorButton = rootLayout.findViewById(R.id.rightIndicatorButton);
        leftIndicatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(0, true);
            }
        });
        rightIndicatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1, true);
            }
        });
    }

    private void setupIndicators() {
        pagerIndicator = rootLayout.findViewById(R.id.viewpagerIndicator);
        leftIndicator = rootLayout.findViewById(R.id.selectionIndicatorLeft);
        rightIndicator = rootLayout.findViewById(R.id.selectionIndicatorRight);
        selectIndicator(pager.getCurrentItem());
    }

    private void selectIndicator(int pos) {
        switch (pos) {
            case 0:
                leftIndicator.setBackgroundColor(getResources().getColor(R.color.viewpager_indicator_color));
                rightIndicator.setBackgroundColor(getResources().getColor(R.color.theme_color));
                break;
            case 1:
                rightIndicator.setBackgroundColor(getResources().getColor(R.color.viewpager_indicator_color));
                leftIndicator.setBackgroundColor(getResources().getColor(R.color.theme_color));
                break;
            default:
                break;
        }
    }

    @Override
    public void onScroll(ScrollableView scrollableView, float y) {
        if(-y <= scrollablePagerIndicatorDistance){
            pagerIndicator.setTranslationY(y);
        }else {
            pagerIndicator.setTranslationY(-scrollablePagerIndicatorDistance);
        }
    }

    @Override
    public void addParallelScrollableChild(ScrollableView scrollableView, int position) {

    }

    @Override
    public void removeParallelScrollableChild(ScrollableView scrollableView) {

    }
}
