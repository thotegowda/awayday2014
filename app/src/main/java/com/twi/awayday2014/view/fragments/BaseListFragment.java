package com.twi.awayday2014.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.twi.awayday2014.R;
import com.twi.awayday2014.view.HomeActivity;
import com.twi.awayday2014.view.custom.ScrollListener;
import com.twi.awayday2014.view.custom.ScrollableView;

public abstract class BaseListFragment extends Fragment implements ScrollableView {
    private static final String TAG = "BaseFragment";

    protected ListView listView;
    protected View header;
    protected View placeHolderView;
    protected TextView placeHolderText;
    protected View rootlayout;
    protected boolean isListViewAdjustedAsPerParent;
    protected boolean isActive;
    private ScrollListener scrollListener;

    @Override
    public void onAttach(android.app.Activity activity) {
        isListViewAdjustedAsPerParent = false;
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        listView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (listView.getChildAt(1) == null) {
                    return false;
                }

                listView.getViewTreeObserver().removeOnPreDrawListener(this);
                float currentScrollPos = ((HomeActivity) getActivity()).getCurrentVisibleHeaderHeight() + getExtraScrollPos();
                isListViewAdjustedAsPerParent = true;
                listView.setSelectionFromTop(1, (int) currentScrollPos);
                Log.d(TAG, "listView is adjusted as per header height");
                return false;
            }
        });
        listView.setSelectionAfterHeaderView();
    }

    protected abstract float getExtraScrollPos();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setAdapter(getAdapter());

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isListViewAdjustedAsPerParent && scrollListener != null && isActive) {
                    scrollListener.onScroll(BaseListFragment.this, header.getY());
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onListItemClick(adapterView, view, i, l);
            }
        });
    }

    protected abstract ListAdapter getAdapter();

    protected abstract void onListItemClick(AdapterView<?> parent, View view, int position, long id);

    protected ListView getListView() {
        return listView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootlayout = getRootLayout(inflater, container);
        listView = (ListView) rootlayout.findViewById(R.id.list);
        header = getHeaderView(inflater, listView);
        listView.addHeaderView(header);
        return rootlayout;
    }

    @Override
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean getActive() {
        return isActive;
    }

    @Override
    public void scrollTo(float y) {
        listView.setSelectionFromTop(1, (int) (y + getExtraScrollPos()));
    }

    protected abstract View getHeaderView(LayoutInflater inflater, ListView listView);

    protected abstract View getRootLayout(LayoutInflater inflater, ViewGroup container);

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }
}