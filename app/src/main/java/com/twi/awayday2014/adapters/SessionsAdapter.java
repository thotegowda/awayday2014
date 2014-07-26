package com.twi.awayday2014.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Presentation;

import java.util.List;


public class SessionsAdapter extends BaseAdapter {

    private final Context context;
    private List<Presentation> keynotes;
    private List<Presentation> sessions;

    public SessionsAdapter(Context context, List<Presentation> keynotes, List<Presentation> sessions) {
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

    private void setupSession(View leftView, Presentation presentation) {
        if (presentation == null) {
            return;
        }

        ((ImageView)leftView.findViewById(R.id.profile_image)).setImageResource(presentation.presenter().profileResource());
        ((TextView)leftView.findViewById(R.id.presenter_title)).setText(presentation.title());
        ((TextView)leftView.findViewById(R.id.presenter_name)).setText(presentation.presenter().name());
        ((TextView)leftView.findViewById(R.id.presentation_date)).setText(presentation.formatedDate());
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
        position -= keynotes.size();
       return (sessions.size() > 2 * position) ? sessions.get(2 * position) : null;
    }

    public Presentation rightSession(int position) {
        position -= keynotes.size();
        return (sessions.size() > 2 * position + 1) ? sessions.get(2 * position + 1) : null;
    }

    public Presentation getKeynoteSession(int position) {
        return keynotes.get(position);
    }
}
