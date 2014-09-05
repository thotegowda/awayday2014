package com.twi.awayday2014.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;
import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.models.Session;
import com.twi.awayday2014.services.parse.AgendaParseDataFetcher;
import com.twi.awayday2014.services.parse.PresenterParseDataFetcher;
import com.twi.awayday2014.utils.Fonts;

import java.util.ArrayList;
import java.util.List;

public class SessionDetailsActivity extends Activity {

    private Session session;
    private List<Presenter> presenters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_session_details);

        String sessionId = getIntent().getStringExtra("session_id");
        AgendaParseDataFetcher agendaParseDataFetcher = ((AwayDayApplication) getApplication()).getAgendaParseDataFetcher();
        PresenterParseDataFetcher presenterParseDataFetcher = ((AwayDayApplication) getApplication()).getPresenterParseDataFetcher();
        session = agendaParseDataFetcher.getDataById(sessionId);
        presenters = new ArrayList<Presenter>();
        for (String presenterId : session.getPresenters()) {
            presenters.add(presenterParseDataFetcher.getDataById(presenterId));
        }

        setupHeader();
        setupDetailText();
        setupFeedbackButton();
    }

    private void setupDetailText() {
        TextView detailsText = (TextView) findViewById(R.id.detailsText);
        detailsText.setText(session.getDescription());
    }

    private void setupFeedbackButton() {
        final Button feedbackButton = (Button) findViewById(R.id.feedbackButton);
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFeedback();
            }
        });
    }

    private void setupHeader() {
        TextView sessionHeaderText = (TextView) findViewById(R.id.sessionHeading);
        sessionHeaderText.setTypeface(Fonts.openSansRegular(this));
        sessionHeaderText.setText(session.getTitle());

        setupSpeakerData();

        TextView sessionTimeText = (TextView) findViewById(R.id.sessionTime);
        sessionTimeText.setTypeface(Fonts.openSansLightItalic(this));
        sessionTimeText.setText(session.getDisplayTime());

        ImageView sessionImage = (ImageView) findViewById(R.id.sessionImage);
        Picasso.with(this)
                .load(session.getImageUrl())
                .placeholder(R.drawable.awayday_2014_placeholder)
                .error(R.drawable.awayday_2014_placeholder)
                .into(sessionImage);
    }

    private void setupSpeakerData() {
        TextView speakerName = (TextView) findViewById(R.id.speakerName);
        View speakerInfoLayout = findViewById(R.id.speakerInfoLayout);
        ImageView userImage1 = (ImageView) findViewById(R.id.profile_image1);
        ImageView userImage2 = (ImageView) findViewById(R.id.profile_image2);
        speakerName.setTypeface(Fonts.openSansLight(this));

        if(presenters.size() == 0){
            speakerName.setVisibility(View.GONE);
            speakerInfoLayout.setVisibility(View.GONE);
            return;
        }else if(presenters.size() == 1){
            Picasso.with(this)
                    .load(presenters.get(0).getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(userImage1);
            speakerName.setText(presenters.get(0).getName());
        }else {
            userImage2.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(presenters.get(0).getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(userImage1);
            Picasso.with(this)
                    .load(presenters.get(1).getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(userImage2);
            speakerName.setText(presenters.get(0).getName() + ", " + presenters.get(1).getName());
        }


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
//            questionHolder.addView(textView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    private void launchSpeakerDetails(Presenter presenter) {
        startActivity(new Intent(this, SpeakerDetailsActivity.class).putExtra("presenter_id", String.valueOf(presenter.getId())));
    }

    private void launchFeedback() {
        startActivity(new Intent(this, FeedbackActivity.class).putExtra("session_id", String.valueOf(session.getId())));
    }
}
