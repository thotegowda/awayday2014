package com.twi.awayday2014.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Presentation;
import com.twi.awayday2014.models.Presenter;

public class SessionDetailsActivity extends Activity {

    private ImageView profileImageView;
    private TextView title;
    private TextView description;
    private LinearLayout presenters;
    private TextView duration;
    private Presentation presentation;
    private Button scheduleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_session_details);
        setupViews();

        String presentation_id = getIntent().getStringExtra("presentation_id");
        presentation = Presentation.findById(Presentation.class, Long.valueOf(presentation_id));
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
        scheduleButton = (Button) findViewById(R.id.btn_add_to_my_schedule);
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToMySchedule();
                ((Button) view).setText(getScheduleText(presentation));
            }
        });
    }

    private String getScheduleText(Presentation presentation) {
        return presentation.isScheduled() ? "O" : "+";
    }

    public void bind(Presentation presentation) {
        profileImageView.setImageResource(presentation.presenter().profileResource());
        title.setText(presentation.title());
        description.setText(presentation.description());
        duration.setText(presentation.formattedDate());
        scheduleButton.setText(getScheduleText(presentation));
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
        startActivity(new Intent(this, FeedbackActivity.class));
    }

    private void addToMySchedule() {
        presentation.setScheduled(!presentation.isScheduled());
        presentation.save();
    }
}
