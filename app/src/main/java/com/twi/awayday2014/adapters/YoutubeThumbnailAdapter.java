package com.twi.awayday2014.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.twi.awayday2014.R;
import com.twi.awayday2014.fragments.VideosFragment;

import java.util.List;

import static com.twi.awayday2014.fragments.VideosFragment.*;

public class YoutubeThumbnailAdapter extends BaseAdapter {

    private final List<YoutubeVideo> videos;
    private final Context context;

    public YoutubeThumbnailAdapter(Context context, List<YoutubeVideo> videos) {
        this.context = context;
        this.videos = videos;
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Object getItem(int i) {
        return videos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // TODO : optimize it, and see why its crashing.
        if (view == null) {
            view = View.inflate(context, R.layout.youtube_thumbnail_item, null);
            YouTubeThumbnailView thumbnailView = (YouTubeThumbnailView) view.findViewById(R.id.youtube_thumbnail_view);

            thumbnailView.initialize(DEVELOPER_KEY, new YoutubeInitializer(videos.get(i)));
       }

        //YouTubeThumbnailView thumbnailView = (YouTubeThumbnailView) view.findViewById(R.id.youtube_thumbnail_view);

        //thumbnailView.initialize(DEVELOPER_KEY, new YoutubeInitializer(videos.get(i)));

        view.setLayoutParams(
                new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.MATCH_PARENT));
        view.setTag(videos.get(i));
        return view;
    }

    private class YoutubeInitializer implements YouTubeThumbnailView.OnInitializedListener {

        private YoutubeVideo video;

        public YoutubeInitializer(YoutubeVideo video) {
            this.video = video;
        }


        @Override
        public void onInitializationSuccess(
                YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
            youTubeThumbnailLoader.setVideo(video.key());
        }

        @Override
        public void onInitializationFailure(
                YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

        }
    }
}
