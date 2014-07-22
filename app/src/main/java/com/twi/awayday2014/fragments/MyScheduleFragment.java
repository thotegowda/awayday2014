package com.twi.awayday2014.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Tweeter;
import android.app.Fragment;
import java.util.List;

public class MyScheduleFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static MyScheduleFragment newInstance(int sectionNumber) {
        MyScheduleFragment fragment = new MyScheduleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MyScheduleFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                Tweeter tweeter = new Tweeter();
                tweeter.authenticate();

                if (tweeter.isAuthenticated()) {
                    tweeter.search("thote", new Tweeter.SearchResultCallback() {
                        @Override
                        public void onSearchComplete(List<twitter4j.Status> result) {
                            System.out.println(result.size());
                        }
                    });
                }
                return null;
            }
        }.execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_other, container, false);
        return rootView;
    }

}
