package com.twi.awayday2014.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Presentation;

import java.util.List;


public class SessionsAdapter extends BaseAdapter {

    public static int[] backgroundResources = {
            R.drawable.listview_purple_background,
            R.drawable.listview_red_background,
            R.drawable.listview_pink_background,
            R.drawable.listview_green_background,
            R.drawable.listview_blue_background
    };

    private final Context context;
    private List<Presentation> keynotes;
    private List<Presentation> sessions;
    private int currentBackgroundResourceIndex = 0;

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

    private void setupSession(View sessionView, Presentation presentation) {
        if (presentation == null) {
            return;
        }

        sessionView.findViewById(R.id.session_text_layout).setBackgroundResource(getNextBackgroundResource());
        ((ImageView) sessionView.findViewById(R.id.profile_image)).setImageResource(presentation.presenter().profileResource());
        ((TextView)sessionView.findViewById(R.id.presenter_title)).setText(presentation.title());
        ((TextView)sessionView.findViewById(R.id.presenter_name)).setText(presentation.presenter().name());
        ((TextView)sessionView.findViewById(R.id.presentation_date)).setText(presentation.formatedDate());
    }

    private int getNextBackgroundResource() {
        if (currentBackgroundResourceIndex < 0 || currentBackgroundResourceIndex > backgroundResources.length - 1) {
            currentBackgroundResourceIndex = 0;
        }
        return backgroundResources[currentBackgroundResourceIndex++];
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
