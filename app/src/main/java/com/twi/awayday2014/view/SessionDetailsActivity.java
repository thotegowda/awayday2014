package com.twi.awayday2014.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.models.Session;

import java.util.List;

public class SessionDetailsActivity extends Activity {

    private ImageView profileImageView;
    private TextView title;
    private TextView description;
    private LinearLayout presenters;
    private TextView duration;
    private ImageView scheduleButton;
    private View topView;

    private Session session;
    private EditText question;
    private LinearLayout questionHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_session_details);

        String sessionId = getIntent().getStringExtra("session_id");
        session = Session.findById(Session.class, Long.valueOf(sessionId));
        setupViews();
        bind(session);
    }

    public void setupViews() {
        profileImageView = ((ImageView) findViewById(R.id.profile_image));
        title = (TextView)findViewById(R.id.session_title);
        description = (TextView) findViewById(R.id.session_description);

        presenters = (LinearLayout) findViewById(R.id.presenters_layout);

        duration = (TextView) findViewById(R.id.session_duration);

        findViewById(R.id.btn_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchFeedback();
            }
        });

        scheduleButton = (ImageView) findViewById(R.id.btn_add_to_my_schedule);
        setScheduleButton(session.isScheduled());
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(session.isScheduled()) {
                    session.setScheduled(false);
                    setScheduleButton(false);
                } else {
                    session.setScheduled(true);
                    setScheduleButton(true);
                }
                session.save();
            }
        });

        topView = findViewById(R.id.top_view);

        questionHolder = (LinearLayout) findViewById(R.id.questions_holder);
        addAllQuestions();

        question = (EditText) findViewById(R.id.question);
        findViewById(R.id.ask).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                askQuestion(question.getText().toString().trim());
            }
        });
    }

    private void askQuestion(String question) {
        ParseObject qn = new ParseObject("Question");
        qn.put("Name", "Thote");
        qn.put("Session", "Keynote");
        qn.put("Question", question);
        qn.saveInBackground();
    }

    private void addAllQuestions() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Question");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("Question", "Retrieved " + parseObjects.size() + " scores");
                    addQuestions(parseObjects);
                } else {
                    Log.d("Question", "Error: " + e.getMessage());
                }
            }
        });
    }

    private void addQuestions(List<ParseObject> questions) {
        for (ParseObject question : questions) {
            Log.d("Question", " question : " + question.get("Name") + " : " + question.get("Session") + " : " + question.get("Question"));

            TextView textView = new TextView(this);
            textView.setText(question.get("Name") + " : " + question.get("Session") + " : " + question.get("Question"));
            questionHolder.addView(textView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    private void setScheduleButton(boolean isScheduled) {
        scheduleButton.setImageResource(isScheduled ?
                R.drawable.add_schedule_button_icon_checked : R.drawable.add_schedule_button_icon_unchecked);
        scheduleButton.setSelected(isScheduled ? true : false);
    }

    public void bind(Session session) {
        profileImageView.setImageResource(session.getSessionResource());
        title.setText(session.getTitle());
        description.setText(session.getDescription());
        duration.setText(session.getDate());
        bind(session.getPresenter());
    }

    private void bind(final Presenter presenter) {
        TextView view = new TextView(this);
        view.setText(presenter.getName());
        view.setPadding(10, 10, 10, 10);
        view.setCompoundDrawablePadding(10);
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setBackgroundColor(Color.parseColor("#88d3d3d3"));
        view.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_speakers), null, null, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                launchSpeakerDetails(presenter);
            }
        });
        presenters.addView(view, lp);
    }

    private void launchSpeakerDetails(Presenter presenter) {
        startActivity(new Intent(this, SpeakerDetailsActivity.class).putExtra("presenter_id", String.valueOf(presenter.getId())));
    }

    private void launchFeedback() {
//        if (!DateUtil.isAlreadyHappened(session)) {
//            Toast.makeText(this, "Please wait for the session to get over!", Toast.LENGTH_SHORT).show();
//            return;
//        }
        startActivity(new Intent(this, FeedbackActivity.class).putExtra("session_id", String.valueOf(session.getId())));
    }
}
