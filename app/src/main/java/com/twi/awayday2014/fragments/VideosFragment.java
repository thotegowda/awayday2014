package com.twi.awayday2014.fragments;

import android.app.ListFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.twi.awayday2014.R;
import com.twi.awayday2014.adapters.VideosAdapter;
import com.twi.awayday2014.models.Video;
import java.util.Arrays;

public class VideosFragment extends ListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static VideosFragment newInstance(int sectionNumber) {
        VideosFragment fragment = new VideosFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public VideosFragment() {
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setListAdapter(new VideosAdapter(this.getActivity(), Arrays.asList(
                new Video("Away day antakshari", "http://www.youtube.com/watch?v=Ex2hEG5mwM4", R.drawable.thumbnail_video1),
                new Video("Saare janhan se acha", "http://www.youtube.com/watch?v=QcIQa2VDwEw", R.drawable.thumbnail_video2),
                new Video("Fun@gurgaon", "https://www.youtube.com/watch?v=Zb3MsrpEJDM", R.drawable.thumbnail_video3),
                new Video("Pune Antakshari", "https://www.youtube.com/watch?v=fNOThb3e_Q8", R.drawable.thumbnail_video4),
                new Video("TW India Awayday", "http://www.youtube.com/watch?v=mn-peucYveo", R.drawable.thumbnail_video5))));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Video video = (Video) v.getTag();
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(video.link())));
    }
}
