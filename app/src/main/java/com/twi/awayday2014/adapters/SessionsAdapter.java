package com.twi.awayday2014.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.twi.awayday2014.R;
import com.twi.awayday2014.RandomColorSelector;
import com.twi.awayday2014.SessionsOrganizer;
import com.twi.awayday2014.models.Presentation;
import com.twi.awayday2014.ui.SessionDetailsActivity;

import java.util.List;


public class SessionsAdapter extends BaseAdapter {

    private final Context context;
    private SessionsOrganizer sessionOrganizer;
    private RandomColorSelector randomColorSelector;

    public SessionsAdapter(Context context, SessionsOrganizer sessionOrganizer, RandomColorSelector randomColorSelector) {
        this.context = context;
        this.sessionOrganizer = sessionOrganizer;
        this.randomColorSelector = randomColorSelector;
    }

    @Override
    public int getCount() {
        return (int) (getKeynotes().size() + Math.floor((double) getSessions().size() / (double) 2));
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

        if (i < getKeynotes().size()) {
            convertToKeynoteView(leftView, rightView);
            setupSession(leftView, getKeynoteSession(i));
        } else {
            convertToSessionView(leftView, rightView);
            setupSession(leftView, leftSession(i));
            setupSession(rightView, rightSession(i));
        }

        view.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, 400));
        return view;
    }

    private void setupSession(View sessionView, Presentation presentation) {
        if (presentation == null) {
            return;
        }

        View sessionLayout = sessionView.findViewById(R.id.session_text_layout);
        sessionLayout.setBackgroundResource(randomColorSelector.next());
        sessionLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                launchSessionDetails();
            }
        });
        ((ImageView) sessionView.findViewById(R.id.profile_image)).setImageResource(presentation.presenter().profileResource());
        ((TextView) sessionView.findViewById(R.id.presenter_title)).setText(presentation.title());
        ((TextView) sessionView.findViewById(R.id.presenter_name)).setText(presentation.presenter().name());
        ((TextView) sessionView.findViewById(R.id.presentation_date)).setText(presentation.formattedDate());
    }

    private void convertToKeynoteView(View leftView, View rightView) {
        leftView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 2));
        rightView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0));
    }

    private void convertToSessionView(View leftView, View rightView) {
        leftView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        rightView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
    }

    private Presentation leftSession(int position) {
        position -= getKeynotes().size();
        return (getSessions().size() > 2 * position) ? getSessions().get(2 * position) : null;
    }

    public Presentation rightSession(int position) {
        position -= getKeynotes().size();
        return (getSessions().size() > 2 * position + 1) ? getSessions().get(2 * position + 1) : null;
    }

    private void launchSessionDetails() {
        context.startActivity(new Intent(context, SessionDetailsActivity.class));
    }

    private List<Presentation> getSessions() {
        return sessionOrganizer.sessions();
    }

    public Presentation getKeynoteSession(int position) {
        return getKeynotes().get(position);
    }

    private List<Presentation> getKeynotes() {
        return sessionOrganizer.keynotes();
    }
}
