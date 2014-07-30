package com.twi.awayday2014.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.twi.awayday2014.R;
import android.app.Fragment;

public class BreakoutFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static BreakoutFragment newInstance(int sectionNumber) {
        BreakoutFragment fragment = new BreakoutFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public BreakoutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_other, container, false);
        return rootView;
    }
}
