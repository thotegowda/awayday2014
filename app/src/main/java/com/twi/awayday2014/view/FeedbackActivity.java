package com.twi.awayday2014.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Session;

public class FeedbackActivity extends Activity {

    private RatingBar[] ratingsView = new RatingBar[3];
    private Session session;
    private TextView sessionTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);

        setupViews();

        String sessionId = getIntent().getStringExtra("session_id");
        session = Session.findById(Session.class, Long.valueOf(sessionId));
        bind(session);
    }

    private void bind(Session presentation) {
//        sessionTitleView.setBackgroundColor(presentation.getModeColor());
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

        findViewById(R.id.submit_feedback).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                submit();
                finish();
            }
        });
    }

    private void submit() {

    }
}
