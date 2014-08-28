package com.twi.awayday2014.components.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.twi.awayday2014.R;
import com.twi.awayday2014.adapters.SpeakersAdapter;
import com.twi.awayday2014.models.Speaker;

import java.util.Arrays;

public class SpeakersFragment extends ListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static ListFragment newInstance(int sectionNumber) {
        SpeakersFragment fragment = new SpeakersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SpeakersFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        setListAdapter(new SpeakersAdapter(this.getActivity(), Arrays.asList(
                new Speaker("Sanjeev Veerawarana", "Building a successfull open source business", "detailed Message", R.drawable.speaker_00),
                new Speaker("Kiran Chandra", "Using open source software for society's benefit", "detailed Message", R.drawable.speaker_01),
                new Speaker("Teesta Setalvad", "Fighting communal forces", "detailed Message", R.drawable.speaker_02),
                new Speaker("Smita Gupta", "Economic development predictor", "detailed Message", R.drawable.speaker_03),
                new Speaker("Dr Yogesh Jain", "Health care for underprivileged", "detailed Message", R.drawable.speaker_04),
                new Speaker("Sumangala Damodaran", "Music for change", "detailed Message", R.drawable.speaker_05)
        )));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //startActivity(new Intent(getActivity(), SessionDetailsActivity.class));
    }
}
