package com.twi.awayday2014.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.utils.Fonts;

import java.util.List;

public class SpeakersAdapter extends BaseAdapter {

    private Context context;
    private List<Presenter> speakers;
    private LayoutInflater inflater;

    public SpeakersAdapter(Context context, List<Presenter> speakers) {
        this.context = context;
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
        ViewSource viewSource;
        if (view == null) {
            view = inflater.inflate(R.layout.view_speaker_listitem, viewGroup, false);
            viewSource = new ViewSource();
            viewSource.speakerName = (TextView) view.findViewById(R.id.speakerName);
            viewSource.speakerName.setTypeface(Fonts.openSansRegular(context));
            view.setTag(viewSource);
        }

        viewSource = (ViewSource) view.getTag();
        return view;
    }

    private class ViewSource{
        TextView speakerName;
    }

}
