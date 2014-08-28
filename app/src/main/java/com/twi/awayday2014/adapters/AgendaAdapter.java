package com.twi.awayday2014.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.utils.Fonts;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Session;
import com.twi.awayday2014.service.AgendaService;
import org.joda.time.DateTime;

import java.util.List;


public class AgendaAdapter extends BaseAdapter {
    private final List<Session> agenda;
    private AgendaService agendaService;
    private Context context;

    public AgendaAdapter(Context context, DateTime day) {
        this.context = context;
        agendaService = AwayDayApplication.agendaService();
        agenda = agendaService.getAgendaFor(day);
    }

    @Override
    public int getCount() {
        return agenda.size();
    }

    @Override
    public Object getItem(int i) {
        return agenda.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewSource viewSource = null;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.view_timeline_listitem, null);
            viewSource = new ViewSource();
            viewSource.timeTextView = (TextView) convertView.findViewById(R.id.timeText);
            viewSource.timeTextView.setTypeface(Fonts.openSansRegular(context));
            viewSource.titleTextView = (TextView) convertView.findViewById(R.id.titleText);
            viewSource.titleTextView.setTypeface(Fonts.openSansRegular(context));
            viewSource.descriptionTextView = (TextView) convertView.findViewById(R.id.descriptionText);
            viewSource.descriptionTextView.setTypeface(Fonts.openSansLight(context));
            convertView.setTag(viewSource);
        }

        viewSource = (ViewSource) convertView.getTag();
        viewSource.timeTextView.setText(agenda.get(position).getTime());
        viewSource.titleTextView.setText(agenda.get(position).getTitle());
        String description = agenda.get(position).getDescription();
        if(description != null){
            viewSource.descriptionTextView.setText(description);
            viewSource.descriptionTextView.setVisibility(View.VISIBLE);
        }else {
            viewSource.descriptionTextView.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class ViewSource{
        TextView timeTextView;
        TextView titleTextView;
        TextView descriptionTextView;
    }
}
