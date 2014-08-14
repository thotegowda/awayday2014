package com.twi.awayday2014.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twi.awayday2014.R;

public class ViewPagerFragment extends Fragment {

    private View rootLayout;
    private View leftIndicator;
    private View rightIndicator;
    protected ViewPager pager;

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
        leftIndicator = rootLayout.findViewById(R.id.selectionIndicatorLeft);
        rightIndicator = rootLayout.findViewById(R.id.selectionIndicatorRight);
        selectIndicator(pager.getCurrentItem());
    }

    private void selectIndicator(int pos) {
        switch (pos) {
            case 0:
                leftIndicator.setBackgroundColor(getResources().getColor(R.color.viewpager_indicator_color));
                rightIndicator.setBackgroundColor(getResources().getColor(R.color.pink));
                break;
            case 1:
                rightIndicator.setBackgroundColor(getResources().getColor(R.color.viewpager_indicator_color));
                leftIndicator.setBackgroundColor(getResources().getColor(R.color.pink));
                break;
            default:
                break;
        }
    }
}
