package com.twi.awayday2014.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.AwayDayNotification;
import com.twi.awayday2014.models.NotificationType;
import com.twi.awayday2014.services.NotificationsService;
import com.twi.awayday2014.utils.Fonts;

import java.util.Collections;
import java.util.List;

public class NotificationsAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private List<AwayDayNotification> awayDayNotifications;
    private Context context;

    public NotificationsAdapter(Context context, List<AwayDayNotification> awayDayNotifications){
        this.context = context;
        this.awayDayNotifications = awayDayNotifications;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return awayDayNotifications.size();
    }

    @Override
    public Object getItem(int i) {
        return awayDayNotifications.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewSource viewSource = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.view_notification_listitem, parent, false);
            viewSource = new ViewSource();
            viewSource.background = (ImageView) convertView.findViewById(R.id.notifications_background);
            viewSource.heading = (TextView) convertView.findViewById(R.id.headingText);
            viewSource.heading.setTypeface(Fonts.openSansRegular(context));
            viewSource.description = (TextView) convertView.findViewById(R.id.descriptionText);
            viewSource.description.setTypeface(Fonts.openSansItalic(context));
            viewSource.timeText = (TextView) convertView.findViewById(R.id.timeText);
            viewSource.timeText.setTypeface(Fonts.openSansItalic(context));
            viewSource.labelText = (TextView) convertView.findViewById(R.id.labelText);
            viewSource.labelText.setTypeface(Fonts.openSansRegular(context));
            convertView.setTag(viewSource);
        }

        viewSource = (ViewSource) convertView.getTag();

        NotificationType type = awayDayNotifications.get(position).getType();
        Drawable backgroundBitmap = type.getBackgroundBitmap();
        if(backgroundBitmap == null){
            int backgroundResourceId = type.getBackgroundResourceId();
            backgroundBitmap = context.getResources().getDrawable(backgroundResourceId);
            type.setBackgroundBitmap(backgroundBitmap);
        }
        viewSource.background.setImageDrawable(backgroundBitmap);

        viewSource.heading.setText(awayDayNotifications.get(position).getTitle());
        viewSource.description.setText(awayDayNotifications.get(position).getDescription());
        viewSource.timeText.setText(awayDayNotifications.get(position).getDisplayTime());
        viewSource.labelText.setText(type.getDisplayText());
        viewSource.labelText.setBackgroundColor(type.getTagColor());

        return convertView;
    }

    public void dataSetChanged(List<AwayDayNotification> awayDayNotifications) {
        this.awayDayNotifications = awayDayNotifications;
        notifyDataSetChanged();
    }

    private class ViewSource{
        ImageView background;
        TextView heading;
        TextView description;
        TextView labelText;
        TextView timeText;
    }}
