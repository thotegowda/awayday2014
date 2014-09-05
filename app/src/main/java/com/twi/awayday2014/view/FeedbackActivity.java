package com.twi.awayday2014.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Feedback;
import com.twi.awayday2014.models.Session;
import com.twi.awayday2014.services.parse.AgendaParseDataFetcher;
import com.twi.awayday2014.services.parse.PresenterParseDataFetcher;

public class FeedbackActivity extends Activity {

    private RatingBar[] ratingsView = new RatingBar[3];
    private Session session;
    private TextView sessionTitleView;
    private EditText tellUsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);

        setupViews();

        String sessionId = getIntent().getStringExtra("session_id");
        AgendaParseDataFetcher agendaParseDataFetcher = ((AwayDayApplication) getApplication()).getAgendaParseDataFetcher();
        session = agendaParseDataFetcher.getDataById(sessionId);
        bind(session);
    }

    private void bind(Session presentation) {
        sessionTitleView.setText(presentation.getTitle());
    }

    private void setupViews() {
        ratingsView[0] = (RatingBar) findViewById(R.id.overall_rating);
        ratingsView[0].setNumStars(5);

        ratingsView[1] = (RatingBar) findViewById(R.id.content_rating);
        ratingsView[1].setNumStars(5);

        ratingsView[2] = (RatingBar) findViewById(R.id.content_rating);
        ratingsView[2].setNumStars(5);

        sessionTitleView = (TextView) findViewById(R.id.session_title);

        tellUsView = (EditText) findViewById(R.id.edt_tell_us);

        findViewById(R.id.submit_feedback).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (submit()) {
                    finish();
                }
            }
        });
    }

    private boolean submit() {
        Feedback feedback = new Feedback(session.getId(), session.getTitle());
        feedback.setOverallRating((int) ratingsView[0].getRating());
        feedback.setRelevantContentRating((int) ratingsView[1].getRating());
        feedback.setSpeakerQuality((int) ratingsView[2].getRating());
        feedback.setAnythingElse(tellUsView.getText().toString().trim());

        if (!feedback.isEmpty()) {
            return submit(feedback);
        } else {
            Toast.makeText(this, "Feedback is very valuable. Please provide valid feedback", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean submit(Feedback feedback) {
        return ((AwayDayApplication) getApplication()).getParseDataService().post(feedback);
    }
}
