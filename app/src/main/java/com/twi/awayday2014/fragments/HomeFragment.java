package com.twi.awayday2014.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.twi.awayday2014.R;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import android.app.Fragment;

public class HomeFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static String AWAY_DAY_DATE = "19/09/2014 08:00:00";

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        setupTime((TextView) rootView.findViewById(R.id.count_down_timer));
        return rootView;
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
