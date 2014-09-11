package com.twi.awayday2014.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.BreakoutSession;
import com.twi.awayday2014.models.SavedSession;
import com.twi.awayday2014.models.Session;
import com.twi.awayday2014.services.MyAgendaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.twi.awayday2014.services.MyAgendaService.MyAgendaServiceListener;


public class BreakoutAdapter extends AgendaAdapter implements MyAgendaServiceListener{
    private static final String TAG = "BreakoutAdapter";
    private Map<String, SavedSession> savedSessions = new HashMap<String, SavedSession>();
    private Toast currentToast;
    private final MyAgendaService myAgendaService;
    private ListView listView;
    private AlertDialog.Builder alertDialogBuilder;

    public BreakoutAdapter(Context context, List<Session> sessions, ListView listView) {
        super(context, sessions);
        this.listView = listView;
        AwayDayApplication application = (AwayDayApplication) ((Activity) context).getApplication();
        myAgendaService = application.getMyAgendaService();
        myAgendaService.addListener(this);
        myAgendaService.fetchSavedSessions(this);
        setupDialog();
    }

    public void onStart() {
        myAgendaService.addListener(this);
        myAgendaService.fetchSavedSessions(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        myAgendaService.removeListener(this);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        final ViewSource viewSource = (ViewSource) view.getTag();
        String trackColor;
        int parsedColor  = 0;
        try {
            trackColor = ((BreakoutSession) agenda.get(position)).getTrackColor();
            if(trackColor != null) {
                parsedColor = Color.parseColor(trackColor);
                viewSource.timeTextView.setBackgroundColor(parsedColor);
                viewSource.timeTextView.setTextColor(Color.WHITE);
            }else{
                viewSource.timeTextView.setBackgroundColor(Color.WHITE);
                viewSource.timeTextView.setTextColor(Color.BLACK);
            }
        }catch (IllegalArgumentException e){
            Log.e(TAG, "Color format is wrong");
        }

        String sessionId = agenda.get(position).getId();

        setupListeners(viewSource, position);

        if (savedSessions.containsKey(sessionId)) {
            viewSource.addSession.setVisibility(View.GONE);
            viewSource.removeSession.setVisibility(View.VISIBLE);

        } else {
            viewSource.addSession.setVisibility(View.VISIBLE);
            viewSource.removeSession.setVisibility(View.GONE);
        }

        return view;
    }

    private void setupListeners(final ViewSource viewSource, final int position) {
        viewSource.removeSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAgendaService.removeSessionFromAgenda(agenda.get(position), BreakoutAdapter.this);
            }
        });

        viewSource.addSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAgendaService.addSessionToAgenda(agenda.get(position),BreakoutAdapter.this);
            }
        });
    }

    @Override
    public void onSavedSessionsFetched(List<SavedSession> savedSessionList, MyAgendaServiceListener caller) {
        if(caller != this){
            return;
        }
        savedSessions.clear();
        for (SavedSession savedSession : savedSessionList) {
            savedSessions.put(savedSession.sessionId, savedSession);
        }
        Log.d(TAG, "Number of saved sessions " + savedSessions.size());
        notifyDataSetChanged();
    }

    @Override
    public void onSessionAddedToAgenda(SavedSession session, Session breakoutSession, MyAgendaServiceListener caller) {
        savedSessions.put(session.sessionId, session);
        if(caller != this){
            return;
        }
        Log.d(TAG, "Session \"" + session.getId() + "\" saved");
        showToast("We will see you at the session.");
        notifyDataSetChanged();
    }

    @Override
    public void onSessionRemoved(SavedSession session, Session breakoutSession, MyAgendaServiceListener caller) {
        savedSessions.remove(session.sessionId);
        if(caller != this){
            return;
        }
        showToast("We will miss you at this session.");
        notifyDataSetChanged();
    }

    @Override
    public void onSessionRemovedError(MyAgendaServiceListener caller) {
        if(caller != this){
            return;
        }
        showToast("Something went wrong while removing the session");
    }

    @Override
    public void onSessionCollide(Session currentSession, Session savedSession, MyAgendaServiceListener caller) {
        if(caller != this){
            return;
        }
        launchDialog(currentSession, savedSession);
    }

    @Override
    public void onSessionReplaced(SavedSession newSession, SavedSession oldSession) {
        savedSessions.remove(oldSession.sessionId);
        savedSessions.put(newSession.sessionId, newSession);
        notifyDataSetChanged();
    }

    private void launchDialog(final Session currentSession, final Session savedSession) {
        String currentSessionTitle = currentSession.getTitle();
        String savedSessionTitle = savedSession.getTitle();

        alertDialogBuilder.setMessage("Timing of \"" + currentSessionTitle + "\" overlaps with timing of \""
                + savedSessionTitle + "\". \n\nAre you sure you want to replace it?");

        alertDialogBuilder.setPositiveButton(R.string.replace, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                myAgendaService.replaceSession(currentSession, savedSession, BreakoutAdapter.this);
            }
        });

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }

    //this is a workaround to update just one child in the listview
    private void updateViewFor(Session session, boolean sessionAdded) {
        int start = listView.getFirstVisiblePosition();
        for(int i=start, j=listView.getLastVisiblePosition();i<=j;i++) {
            Session itemAtPosition = (Session) listView.getItemAtPosition(i);
            if(itemAtPosition == null){
                Log.e(TAG, "null item for pos "+ i);
                continue;
            }
            Log.e(TAG, "Equating " + session.getTitle() + " with " + itemAtPosition.getTitle());
            if (session.getId().equals(itemAtPosition.getId())) {
                Log.e(TAG, "Match found. Updating");
                View view = listView.getChildAt(i - start);

                final ViewSource viewSource = (ViewSource) view.getTag();

                if(sessionAdded){
                    viewSource.addSession.setVisibility(View.GONE);
                    viewSource.removeSession.setVisibility(View.VISIBLE);
                }else {
                    viewSource.addSession.setVisibility(View.VISIBLE);
                    viewSource.removeSession.setVisibility(View.GONE);
                }
                viewSource.addSession.invalidate();
                viewSource.removeSession.invalidate();
                return;
            }
        }
    }

    private void showToast(String msg) {
        if(currentToast != null){
            currentToast.cancel();
        }
        currentToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        currentToast.show();
    }

    private void setupDialog() {
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(R.string.agenda_overlap_dialog_title);

        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // don't have to do anything
            }
        });
    }
}
