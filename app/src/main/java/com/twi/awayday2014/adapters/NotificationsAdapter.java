package com.twi.awayday2014.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Notification;
import com.twi.awayday2014.models.NotificationType;
import com.twi.awayday2014.services.NotificationsService;
import com.twi.awayday2014.utils.Fonts;

import java.util.Collections;
import java.util.List;

public class NotificationsAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private List<Notification> notifications;
    private Context context;

    public NotificationsAdapter(Context context) {
        this.context = context;
        NotificationsService notificationsService = AwayDayApplication.notificationsService();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        notifications = notificationsService.getNotifications();
        Collections.sort(notifications, Collections.reverseOrder(new Notification.NotificatonsComparator()));
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Object getItem(int i) {
        return notifications.get(i);
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

        NotificationType type = notifications.get(position).getType();
        Drawable backgroundBitmap = type.getBackgroundBitmap();
        if(backgroundBitmap == null){
            int backgroundResourceId = type.getBackgroundResourceId();
            backgroundBitmap = context.getResources().getDrawable(backgroundResourceId);
            type.setBackgroundBitmap(backgroundBitmap);
        }
        viewSource.background.setImageDrawable(backgroundBitmap);

        viewSource.heading.setText(notifications.get(position).getTitle());
        viewSource.description.setText(notifications.get(position).getDescription());
        viewSource.timeText.setText(notifications.get(position).getDisplayTime());
        viewSource.labelText.setText(type.getDisplayText());
        viewSource.labelText.setBackgroundColor(type.getTagColor());

        return convertView;
    }

    private class ViewSource{
        ImageView background;
        TextView heading;
        TextView description;
        TextView labelText;
        TextView timeText;
    }}
