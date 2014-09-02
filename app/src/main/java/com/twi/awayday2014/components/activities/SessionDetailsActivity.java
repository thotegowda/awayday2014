package com.twi.awayday2014.components.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Presentation;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.utils.DateUtil;

import java.text.ParseException;
import java.util.List;

public class SessionDetailsActivity extends Activity {

    private ImageView profileImageView;
    private TextView title;
    private TextView description;
    private LinearLayout presenters;
    private TextView duration;
    private ImageView scheduleButton;
    private View topView;

    private Presentation presentation;
    private EditText question;
    private LinearLayout questionHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_session_details);

        String presentation_id = getIntent().getStringExtra("presentation_id");
        presentation = Presentation.findById(Presentation.class, Long.valueOf(presentation_id));
        setupViews();
        bind(presentation);
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
        setScheduleButton(presentation.isScheduled());
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(presentation.isScheduled()) {
                    presentation.setScheduled(false);
                    setScheduleButton(false);
                } else {
                    presentation.setScheduled(true);
                    setScheduleButton(true);
                }
                presentation.save();
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

    public void bind(Presentation presentation) {
        topView.setBackgroundColor(presentation.getModeColor());
        profileImageView.setImageResource(presentation.presenter().profileResource());
        title.setText(presentation.title());
        description.setText(presentation.description());
        duration.setText(presentation.formattedDate());
        bind(presentation.presenter());
    }

    private void bind(Presenter presenter) {
        TextView view = new TextView(this);
        view.setText(presenter.name());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        presenters.addView(view, lp);
    }

    private void launchFeedback() {
        if (!DateUtil.isAlreadyHappened(presentation)) {
            Toast.makeText(this, "Please wait for the session to get over!", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(this, FeedbackActivity.class).putExtra("presentation_id", String.valueOf(presentation.getId())));
    }
}
