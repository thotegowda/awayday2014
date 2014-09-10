package com.twi.awayday2014.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.BreakoutSession;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.models.Session;
import com.twi.awayday2014.utils.Fonts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BreakoutAdapter extends BaseAdapter {
    private List<BreakoutSession> breakoutSessions;
    private Map<String, Presenter> presenters = new HashMap<String, Presenter>();
    private final LayoutInflater inflater;
    private Context context;

    public BreakoutAdapter(Context context, List<BreakoutSession> sessions) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.breakoutSessions = sessions;
    }

    @Override
    public int getCount() {
        return breakoutSessions.size();
    }

    @Override
    public Object getItem(int i) {
        return breakoutSessions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void onDataChange(List<BreakoutSession> sessions){
        breakoutSessions = sessions;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewSource viewSource = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.view_timeline_listitem, parent, false);
            viewSource = new ViewSource();
            viewSource.timeTextView = (TextView) convertView.findViewById(R.id.timeText);
            viewSource.timeTextView.setTypeface(Fonts.openSansRegular(context));
            viewSource.titleTextView = (TextView) convertView.findViewById(R.id.titleText);
            viewSource.titleTextView.setTypeface(Fonts.openSansRegular(context));
            viewSource.speakerTextView = (TextView) convertView.findViewById(R.id.speakersText);
            viewSource.speakerTextView.setTypeface(Fonts.openSansLight(context));
            viewSource.locationTextView = (TextView) convertView.findViewById(R.id.locationText);
            viewSource.locationTextView.setTypeface(Fonts.openSansItalic(context));
            viewSource.sessionImage = (ImageView) convertView.findViewById(R.id.sessionImage);
            viewSource.userImage1 = (ImageView) convertView.findViewById(R.id.userImage1);
            viewSource.userImage2 = (ImageView) convertView.findViewById(R.id.userImage2);
            convertView.setTag(viewSource);
        }

        viewSource = (ViewSource) convertView.getTag();
        Session session = breakoutSessions.get(position);
        viewSource.timeTextView.setText(session.getDisplayTime());
        viewSource.titleTextView.setText(session.getTitle());

        setupSpeakerData(viewSource, session);
        setupLocation(viewSource, session);
        setupSessionImage(viewSource, session);
        return convertView;
    }

    private void setupSessionImage(ViewSource viewSource, Session session) {
        if(session.getImageUrl() != null){
            Picasso.with(context)
                    .load(session.getImageUrl())
                    .placeholder(new ColorDrawable(context.getResources().getColor(R.color.theme_color)))
                    .error(R.drawable.awayday_2014_placeholder)
                    .into(viewSource.sessionImage);
            viewSource.sessionImage.setTag(session.getImageUrl());
        }else{
            Picasso.with(context)
                    .load(R.drawable.awayday_2014_placeholder)
                    .into(viewSource.sessionImage);
            viewSource.sessionImage.setTag(R.drawable.awayday_2014_placeholder);
        }
    }

    private void setupLocation(ViewSource viewSource, Session session) {
        if(session.getLocation() == null){
            viewSource.locationTextView.setVisibility(View.GONE);
        }else {
            viewSource.locationTextView.setVisibility(View.VISIBLE);
            viewSource.locationTextView.setText(session.getLocation());
        }
    }

    private void setupSpeakerData(ViewSource viewSource, Session session) {
        List<String> sessionPresenters = session.getPresenters();
        if(sessionPresenters == null || sessionPresenters.size() == 0){
            viewSource.userImage1.setVisibility(View.GONE);
            viewSource.userImage2.setVisibility(View.GONE);
            viewSource.speakerTextView.setVisibility(View.GONE);
        }else if(sessionPresenters.size() == 1){
            viewSource.userImage1.setVisibility(View.VISIBLE);
            viewSource.speakerTextView.setVisibility(View.VISIBLE);
            if(presentersLoaded(sessionPresenters)){
                Presenter presenter = presenters.get(sessionPresenters.get(0));
                Picasso.with(context)
                        .load(presenter.getImageUrl())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(viewSource.userImage1);
                viewSource.speakerTextView.setText(presenter.getName());
            }
            viewSource.userImage2.setVisibility(View.GONE);
        }else{
            viewSource.userImage1.setVisibility(View.VISIBLE);
            viewSource.speakerTextView.setVisibility(View.VISIBLE);
            viewSource.userImage2.setVisibility(View.VISIBLE);
            if(presentersLoaded(sessionPresenters)){
                Presenter presenter1 = presenters.get(sessionPresenters.get(0));
                Presenter presenter2 = presenters.get(sessionPresenters.get(1));
                Picasso.with(context)
                        .load(presenter1.getImageUrl())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(viewSource.userImage1);
                Picasso.with(context)
                        .load(presenter2.getImageUrl())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(viewSource.userImage2);
                viewSource.speakerTextView.setText(presenter1.getName() + ", " +
                        presenter2.getName());
            }
        }
    }

    private boolean presentersLoaded(List<String> sessionPresenters) {
        return presenters.containsKey(sessionPresenters.get(0));
    }

    public void presentersInfoFetched(List<Presenter> presentersList) {
        for (Presenter presenter : presentersList) {
            presenters.put(presenter.getId(), presenter);
        }
        notifyDataSetChanged();
    }

    private class ViewSource{
        TextView timeTextView;
        TextView titleTextView;
        TextView speakerTextView;
        TextView locationTextView;
        ImageView sessionImage;
        ImageView userImage1;
        ImageView userImage2;
    }
}