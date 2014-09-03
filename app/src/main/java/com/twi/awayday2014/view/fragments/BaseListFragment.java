package com.twi.awayday2014.view.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.twi.awayday2014.R;
import com.twi.awayday2014.view.HomeActivity;

public abstract class BaseListFragment extends Fragment implements HomeActivity.CustomActionbarStateListener {
    private static final String TAG = "BaseFragment";

    private ListView listView;
    private View header;
    private boolean interceptingTouchEvents;

    @Override
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);

        HomeActivity newHomeNewHomeActivity = (HomeActivity) getActivity();
        newHomeNewHomeActivity.addCustomActionbarStateListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        HomeActivity newHomeActivity = (HomeActivity) getActivity();
        newHomeActivity.removeCustomActionbarStateListener(this);
    }

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
                if (listIsAtTop()) {
                    Log.d("List", "list is at top ");
                    interceptingTouchEvents = false;
                }
            }
        });

        listView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!interceptingTouchEvents) {
                    Log.d("List", "list is at top, so not intercepting ");
                    Log.e(TAG, "Not intercepting");
                    return false;
                } else {
                    Log.e(TAG, "intercepting");
                    int action = event.getAction();
                    ViewParent scrollviewParent = findScrollviewParent(v);
                    if (scrollviewParent == null) {
                        throw new IllegalStateException("A scrollview parent is exptected in the view hierarchy");
                    }
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow ScrollView to intercept touch events.
                            scrollviewParent.requestDisallowInterceptTouchEvent(true);
                            break;

                        case MotionEvent.ACTION_UP:
                            // Allow ScrollView to intercept touch events.
                            scrollviewParent.requestDisallowInterceptTouchEvent(false);
                            break;
                    }

                    // Handle ListView touch events.
                    v.onTouchEvent(event);
                    return true;
                }
            }

            private ViewParent findScrollviewParent(View v) {
                ViewParent result;
                while (v != null) {
                    result = v.getParent();
                    if (result instanceof ScrollView) {
                        return result;
                    } else {
                        v = (View) result;
                    }
                }
                return null;
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

    private boolean listIsAtTop() {
        if (listView.getChildCount() == 0) {
            return true;
        }

        return listView.getChildAt(0).getTop() == 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listView = (ListView) inflater.inflate(R.layout.fragment_timeline, container, false);
        header = inflater.inflate(R.layout.view_fake_header, listView, false);
        return listView;
    }

    @Override
    public void onActionbarStateChange(HomeActivity.CustomActionbarState customActionbarState) {
        if (customActionbarState == HomeActivity.CustomActionbarState.STICKY) {
            interceptingTouchEvents = true;
        } else {
            interceptingTouchEvents = false;
        }
    }
}