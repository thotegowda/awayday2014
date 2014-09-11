package com.twi.awayday2014.view;

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

public class FeedbackActivity extends SessionDetailsBaseActivity {
    private static final String TAG = "SessionDetailsActivity";
    private RatingBar[] ratingsView = new RatingBar[3];
    private EditText tellUsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupRatingsView();
        setupFeedbackButton();
        setupTextFonts();
    }

    private void setupTextFonts() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_feedback;
    }

    private void setupRatingsView() {
        ratingsView[0] = (RatingBar) findViewById(R.id.overall_rating);
        ratingsView[0].setNumStars(5);

        ratingsView[1] = (RatingBar) findViewById(R.id.content_rating);
        ratingsView[1].setNumStars(5);

        ratingsView[2] = (RatingBar) findViewById(R.id.speaker_quality_rating);
        ratingsView[2].setNumStars(5);

        tellUsView = (EditText) findViewById(R.id.edt_tell_us);
    }

    private void setupFeedbackButton() {
        findViewById(R.id.feedbackButton).setOnClickListener(new View.OnClickListener() {

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
