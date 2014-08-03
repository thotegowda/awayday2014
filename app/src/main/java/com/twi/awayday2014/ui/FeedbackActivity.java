package com.twi.awayday2014.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import com.twi.awayday2014.R;

public class FeedbackActivity extends Activity {

    private RatingBar[] ratingsView = new RatingBar[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);

        setupViews();
    }

    private void setupViews() {
        ratingsView[0] = (RatingBar) findViewById(R.id.overall_rating);
        ratingsView[0].setNumStars(5);

        ratingsView[1] = (RatingBar) findViewById(R.id.content_rating);
        ratingsView[1].setNumStars(5);

        ratingsView[2] = (RatingBar) findViewById(R.id.content_rating);
        ratingsView[2].setNumStars(5);

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
