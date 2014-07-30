package com.twi.awayday2014.view.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.twi.awayday2014.R;
import com.twi.awayday2014.adapters.YoutubeThumbnailAdapter;

import java.util.Arrays;

public class VideosFragment
        extends Fragment
        implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final String TAG = "Youtube";
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 10;
    public static final String DEVELOPER_KEY = "AIzaSyCxTiSWZmwVkud2xY8El0ePCSkJ_v3y4CI";

    public static class YoutubeVideo {
        private String videoKey;

        public YoutubeVideo(String videoKey) {
            this.videoKey = videoKey;
        }

        public String key() {
            return videoKey;
        }
    }

    private YoutubeThumbnailAdapter youtubeVideoAdapter;
    private LocationClient mLocationClient;
    private ListView listView;
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static Fragment newInstance(int sectionNumber) {
        VideosFragment fragment = new VideosFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        youtubeVideoAdapter = new YoutubeThumbnailAdapter(this.getActivity(), Arrays.asList(
                new YoutubeVideo("XMhaNBO03ks"),
                new YoutubeVideo("3DwGYCvYo9A"),
                new YoutubeVideo("O92FgzquyS8"),
                new YoutubeVideo("7FKEy_RWwQk"),
                new YoutubeVideo("e2rGhnGuCy0"),
                new YoutubeVideo("Ex_ESrCD-Uc"),
                new YoutubeVideo("niahjl8eVx8"),
                new YoutubeVideo("kYwYb3Pa9p4"),
                new YoutubeVideo("8UXxo7ILHWU")));

        listView.setAdapter(youtubeVideoAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                launchYoutubeVideo((YoutubeVideo) view.getTag());

            }
        });

        mLocationClient = new LocationClient(this.getActivity().getApplicationContext(), this, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.youtube_videos_list, container, false);
        listView = (ListView) rootView.findViewById(R.id.videos_list);
        return rootView;
    }

    public void onStop() {
        super.onStop();
        //Log.d(TAG, "Trying to disconnect with Google service()");
        //mLocationClient.disconnect();
    }

    public void onPause() {
        super.onPause();
    }

    public void onStart() {
        super.onStart();
        //Log.d(TAG, "Trying to connect with Google service");
       // mLocationClient.connect();
    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected()");
    }

    @Override
    public void onDisconnected() {
        Log.d(TAG, "onDisconnected()");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(
                        this.getActivity(),
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
    }

    public void launchYoutubeVideo(YoutubeVideo video) {
        Intent intent = YouTubeStandalonePlayer.createVideoIntent(this.getActivity(), DEVELOPER_KEY, video.key(), 0, true, false);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_START_STANDALONE_PLAYER && resultCode != Activity.RESULT_OK) {
            YouTubeInitializationResult errorReason =
                    YouTubeStandalonePlayer.getReturnedInitializationResult(data);
            if (errorReason.isUserRecoverableError()) {
                errorReason.getErrorDialog(this.getActivity(), 0).show();
            }
        }
    }
}
