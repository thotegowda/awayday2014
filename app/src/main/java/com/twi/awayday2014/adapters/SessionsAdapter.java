package com.twi.awayday2014.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Session;

import java.util.List;


public class SessionsAdapter extends BaseAdapter {

    private final Context context;
    private List<Session> keynotes;
    private List<Session> sessions;

    public SessionsAdapter(Context context, List<Session> keynotes, List<Session> sessions) {
        this.context = context;
        this.keynotes = keynotes;
        this.sessions = sessions;
    }
    @Override
    public int getCount() {
        return (int) (keynotes.size() + Math.floor((double)sessions.size() / (double)2));
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

        if (i < keynotes.size()) {
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

    private void setupSession(View leftView, Session session) {
        if (session == null) {
            return;
        }

        ((TextView)leftView.findViewById(R.id.presenter_title)).setText(session.getTitle());
    }

    private void convertToKeynoteView(View leftView, View rightView) {
        leftView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 2));
        rightView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0));
    }

    private void convertToSessionView(View leftView, View rightView) {
        leftView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        rightView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
    }

    private Session leftSession(int position) {
        position -= keynotes.size();
       return (sessions.size() > 2 * position) ? sessions.get(2 * position) : null;
    }

    public Session rightSession(int position) {
        position -= keynotes.size();
        return (sessions.size() > 2 * position + 1) ? sessions.get(2 * position + 1) : null;
    }

    public Session getKeynoteSession(int position) {
        return keynotes.get(position);
    }
}
