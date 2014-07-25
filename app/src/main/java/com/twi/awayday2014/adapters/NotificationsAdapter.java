package com.twi.awayday2014.adapters;

import android.app.ActionBar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.ShortNotification;

import java.util.LinkedList;
import java.util.List;

public class NotificationsAdapter extends BaseAdapter {

    private final List<ShortNotification> notifications;
    private final LayoutInflater inflater;

    public NotificationsAdapter(Context context, List<ShortNotification> notifications) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.notifications = new LinkedList<ShortNotification>(notifications);
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootView = view;
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.notification_item, viewGroup, false);
        }

        ((TextView)rootView.findViewById(R.id.message)).setText(notifications.get(i).message());
        ((TextView)rootView.findViewById(R.id.message_time)).setText(notifications.get(i).messageTime());

        return rootView;
    }

    public void remove(int position) {
        notifications.remove(getItem(position));
    }
}
