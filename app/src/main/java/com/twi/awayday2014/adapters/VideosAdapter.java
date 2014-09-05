package com.twi.awayday2014.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Video;

import java.util.List;

public class VideosAdapter extends BaseAdapter {

    private List<Video> videos;
    private LayoutInflater inflater;

    public VideosAdapter(Context context, List<Video> videos) {
        this.videos = videos;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        if (view == null) {
            view = inflater.inflate(R.layout.view_speaker_listitem, viewGroup, false);
        }


        view.setTag(videos.get(i));
        return view;
    }
}
