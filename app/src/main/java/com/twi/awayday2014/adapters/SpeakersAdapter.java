package com.twi.awayday2014.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Speaker;

import java.util.List;

public class SpeakersAdapter extends BaseAdapter {

    private List<Speaker> speakers;
    private LayoutInflater inflater;

    public SpeakersAdapter(Context context, List<Speaker> speakers) {
        this.speakers = speakers;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return speakers.size();
    }

    @Override
    public Object getItem(int i) {
        return speakers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       if (view == null) {
           view = inflater.inflate(R.layout.video_item, viewGroup, false);
       }

        ((ImageView)view.findViewById(R.id.video_thumbnail)).setImageResource(speakers.get(i).imageResourceId());
        ((TextView)view.findViewById(R.id.video_title)).setText(speakers.get(i).name());
        ((TextView)view.findViewById(R.id.video_sub_title)).setText(speakers.get(i).shortBio());
        view.setTag(speakers.get(i));
        return view;
    }
}
