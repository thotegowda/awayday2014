package com.twi.awayday2014.components.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ScrollView;
import com.twi.awayday2014.R;
import com.twi.awayday2014.adapters.AgendaAdapter;
import com.twi.awayday2014.components.activities.NewHomeActivity;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import static com.twi.awayday2014.components.activities.NewHomeActivity.CustomActionbarState;


public class AgendaTimelineFragment extends Fragment implements NewHomeActivity.CustomActionbarStateListener {
    private static final String TAG = "AgendaTimelineFragment";
    private static final String DAY = "day";
    private static final String POSITION = "position";
    private ListView listView;
    private View header;
    private boolean interceptingTouchEvents;

    public static AgendaTimelineFragment newInstance(DateTime day, int position) {
        AgendaTimelineFragment fragment = new AgendaTimelineFragment();
        Bundle args = new Bundle();
        args.putString(DAY, ISODateTimeFormat.dateTime().print(day));
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);

        int position = getArguments().getInt(POSITION);
        Log.d(TAG + " " + position, "onAttach()");
        NewHomeActivity newHomeNewHomeActivity = (NewHomeActivity) getActivity();
        newHomeNewHomeActivity.addCustomActionbarStateListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        int position = getArguments().getInt(POSITION);
        Log.d(TAG + " " + position, "onDetach()");
        NewHomeActivity newHomeActivity = (NewHomeActivity) getActivity();
        newHomeActivity.removeCustomActionbarStateListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DateTime date = ISODateTimeFormat.dateTime().parseDateTime(getArguments().getString(DAY));
        listView.setAdapter(new AgendaAdapter(getActivity(), date));
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(listIsAtTop()){
                    interceptingTouchEvents = false;
                }
            }
        });

        listView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!interceptingTouchEvents) {
                    Log.e(TAG, "Not intercepting");
                    return false;
                } else {
                    Log.e(TAG, "intercepting");
                    int action = event.getAction();
                    ViewParent scrollviewParent = findScrollviewParent(v);
                    if(scrollviewParent == null){
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
                while(v != null){
                    result = v.getParent();
                    if(result instanceof ScrollView){
                        return result;
                    }else{
                        v = (View) result;
                    }
                }
                return null;
            }
        });
    }

    private boolean listIsAtTop()   {
        if(listView.getChildCount() == 0) return true;
        return listView.getChildAt(0).getTop() == 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listView = (ListView) inflater.inflate(R.layout.fragment_timeline, container, false);
        header = inflater.inflate(R.layout.view_fake_header, listView, false);
        return listView;
    }

    @Override
    public void onActionbarStateChange(CustomActionbarState customActionbarState) {
        if (customActionbarState == CustomActionbarState.STICKY) {
            interceptingTouchEvents = true;
        } else {
            interceptingTouchEvents = false;
        }
    }
}
