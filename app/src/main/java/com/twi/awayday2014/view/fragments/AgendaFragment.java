package com.twi.awayday2014.view.fragments;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.twi.awayday2014.R;
import com.twi.awayday2014.adapters.SessionsAdapter;
import com.twi.awayday2014.listeners.ScrollListener;
import com.twi.awayday2014.listeners.Scrollable;
import com.twi.awayday2014.models.Presentation;
import com.twi.awayday2014.models.Presenter;

import java.util.Arrays;
import java.util.List;

public class AgendaFragment extends Fragment implements Scrollable {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView agendaListView;
    private ScrollListener scrollListener;

    public static AgendaFragment newInstance(int sectionNumber) {
        AgendaFragment fragment = new AgendaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        agendaListView = (ListView) inflater.inflate(R.layout.fragment_list, container, false);
        agendaListView.addHeaderView(inflater.inflate(R.layout.view_fake_header, agendaListView, false));
        agendaListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(scrollListener != null){
                    scrollListener.onScrollChanged(getScrollY());
                }
            }
        });
        return agendaListView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        agendaListView.setAdapter(new SessionsAdapter(this.getActivity(), getKeynotes(), getPresentations()));
    }

    private int getScrollY() {
        View c = agendaListView.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = agendaListView.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = (int) getResources().getDimension(R.dimen.home_activity_header_height);
        }

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    private List<Presentation> getKeynotes() {
        return Arrays.asList(
                new Presentation(new Presenter("Presenter name1", R.drawable.speaker_00), "keynote title1", "29/09/14 10.00 - 11.00"),
                new Presentation(new Presenter("Presenter name2", R.drawable.speaker_01), "keynote title2", "29/09/14 12.00 - 1.00"),
                new Presentation(new Presenter("Presenter name3", R.drawable.speaker_02), "keynote title3", "29/09/14 1.00 - 4.00")
        );
    }

    private List<Presentation> getPresentations() {
        return Arrays.asList(
                new Presentation(new Presenter("Presenter name1", R.drawable.speaker_00), "session title1", "29/09/14 10.00 - 11.00"),
                new Presentation(new Presenter("Presenter name2", R.drawable.speaker_01), "session title2", "29/09/14 12.00 - 1.00"),
                new Presentation(new Presenter("Presenter name3", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00"),
                new Presentation(new Presenter("Presenter name4", R.drawable.speaker_00), "session title1", "29/09/14 10.00 - 11.00"),
                new Presentation(new Presenter("Presenter name5", R.drawable.speaker_01), "session title2", "29/09/14 12.00 - 1.00"),
                new Presentation(new Presenter("Presenter name6", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00"),
                new Presentation(new Presenter("Presenter name7", R.drawable.speaker_00), "session title1", "29/09/14 10.00 - 11.00"),
                new Presentation(new Presenter("Presenter name8", R.drawable.speaker_01), "session title2", "29/09/14 12.00 - 1.00"),
                new Presentation(new Presenter("Presenter name9", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00"),
                new Presentation(new Presenter("Presenter name10", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00")
        );
    }

    @Override
    public void addListener(ScrollListener listener) {
        scrollListener = listener;
    }

    @Override
    public void removeListener(ScrollListener listener) {
        scrollListener = null;
    }

}
