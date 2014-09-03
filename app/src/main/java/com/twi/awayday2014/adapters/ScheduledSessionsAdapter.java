package com.twi.awayday2014.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Session;
import com.twi.awayday2014.services.AgendaService;
import com.twi.awayday2014.view.SessionDetailsActivity;

import java.util.List;


public class ScheduledSessionsAdapter extends BaseAdapter {

    private final Context context;
    private final List<Session> scheduledSessions;

    public ScheduledSessionsAdapter(Context context, AgendaService agendaService) {
        this.context = context;
        this.scheduledSessions = agendaService.getScheduledSessions();
    }

    @Override
    public int getCount() {
        return scheduledSessions.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(context, R.layout.session_row, null);
        }

        View leftView = view.findViewById(R.id.left_session);
        View rightView = view.findViewById(R.id.right_session);

        convertToKeynoteView(leftView, rightView);
        setupSession(leftView, scheduledSessions.get(i));

        view.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, 400));
        return view;
    }

    private void setupSession(View sessionView, Session session) {
        if (session == null) {
            return;
        }

        View sessionLayout = sessionView.findViewById(R.id.session_text_layout);
        sessionLayout.setTag(session);
        sessionLayout.setBackgroundColor(R.color.transparent_blue);
        sessionLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                launchSessionDetails(((Session)view.getTag()).getId());
            }
        });
        ((ImageView) sessionView.findViewById(R.id.profile_image)).setImageResource(session.getPresenter().profileResource());
        ((TextView) sessionView.findViewById(R.id.presenter_title)).setText(session.getTitle());
        ((TextView) sessionView.findViewById(R.id.presenter_name)).setText(session.getPresenter().getName());
        ((TextView) sessionView.findViewById(R.id.presentation_date)).setText(session.getDate());
    }

    private void convertToKeynoteView(View  leftView, View rightView) {
        leftView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 2));
        rightView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0));
    }

    private void launchSessionDetails(Long id) {
        context.startActivity(new Intent(context, SessionDetailsActivity.class).putExtra("session_id", String.valueOf(id)));
    }

}
