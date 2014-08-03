package com.twi.awayday2014.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.twi.awayday2014.R;
import com.twi.awayday2014.ui.SwipeDismissListViewTouchListener;
import com.twi.awayday2014.adapters.NotificationsAdapter;
import com.twi.awayday2014.models.ShortNotification;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import android.app.Fragment;

import static com.twi.awayday2014.ui.SwipeDismissListViewTouchListener.DismissCallbacks;

public class HomeFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static String AWAY_DAY_DATE = "19/09/2014 08:00:00";
    private ListView listView;
    private SwipeDismissListViewTouchListener swipeListener;
    private NotificationsAdapter adapter;

    public static HomeFragment newInstance(int sectionNumber) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        setupTime((TextView) rootView.findViewById(R.id.count_down_timer));
        listView = (ListView) rootView.findViewById(R.id.notification_layout);
        setupNotifications();
        return rootView;
    }

    private void setupNotifications() {
        adapter = new NotificationsAdapter(getActivity(), ShortNotification.listAll(ShortNotification.class));
        listView.setAdapter(adapter);
        swipeListener = new SwipeDismissListViewTouchListener(listView, new DismissCallbacks() {

            @Override
            public boolean canDismiss(int position) {
                return true;
            }

            @Override
            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    adapter.remove(position);
                }
                adapter.notifyDataSetChanged();
            }
        });

        listView.setOnTouchListener(swipeListener);
        listView.setOnScrollListener(swipeListener.makeScrollListener());
    }

    private void setupTime(TextView countDownTimer) {
        countDownTimer.setText(String.valueOf(getNumberOfDaysRemaining().getDays()));
    }

    private Days getNumberOfDaysRemaining() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        LocalDate then = formatter.parseLocalDate(AWAY_DAY_DATE);
        LocalDate now = LocalDate.now();
        return Days.daysBetween(now, then);
    }
}
