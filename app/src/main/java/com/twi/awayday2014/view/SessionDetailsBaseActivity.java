package com.twi.awayday2014.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.models.Question;
import com.twi.awayday2014.models.Session;
import com.twi.awayday2014.services.parse.AgendaParseDataFetcher;
import com.twi.awayday2014.services.parse.BreakoutSessionsParseDataFetcher;
import com.twi.awayday2014.services.parse.PresenterParseDataFetcher;
import com.twi.awayday2014.services.parse.QuestionService;
import com.twi.awayday2014.utils.Fonts;
import com.twi.awayday2014.view.fragments.SpeakersFragment;

import java.util.ArrayList;
import java.util.List;

public abstract class SessionDetailsBaseActivity extends FragmentActivity {
    private static final String TAG = "SessionDetailsBaseActivity";

    public static final String SESSION_ID = "id";
    public static final String SESSION_TYPE = "type";
    protected Session session;
    protected List<Presenter> presenters = new ArrayList<Presenter>();
    protected String sessionId;
    protected String sessionType;
    private Drawable appIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentView());

        fetchPresenter();
        setupHeader();
        setupActionbar();
    }

    private void setupActionbar() {
        appIcon = getResources().getDrawable(R.drawable.ic_launcher);
        appIcon.setAlpha(255);
        getActionBar().setIcon(appIcon);
    }

    protected abstract int getContentView();

    private void fetchPresenter() {
        sessionId = getIntent().getStringExtra(SESSION_ID);
        sessionType = getIntent().getStringExtra(SESSION_TYPE);

        if(sessionType.equals("regularSession")){
            AgendaParseDataFetcher agendaParseDataFetcher = ((AwayDayApplication) getApplication()).getAgendaParseDataFetcher();
            session = agendaParseDataFetcher.getDataById(sessionId);
        }else if(sessionType.equals("breakoutSession")){
            BreakoutSessionsParseDataFetcher breakoutSessionsParseDataFetcher = ((AwayDayApplication) getApplication()).getBreakoutSessionsParseDataFetcher();
            session = breakoutSessionsParseDataFetcher.getDataById(sessionId);
        }

        if(session.getPresenters() != null && !session.getPresenters().isEmpty()){
            PresenterParseDataFetcher presenterParseDataFetcher = ((AwayDayApplication) getApplication()).getPresenterParseDataFetcher();
            for (String presenterId : session.getPresenters()) {
                presenters.add(presenterParseDataFetcher.getDataById(presenterId));
            }
        }
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
        userImage1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                launchSpeakerDetailActivity(presenters.get(0));
            }
        });

        ImageView userImage2 = (ImageView) findViewById(R.id.profile_image2);
        userImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSpeakerDetailActivity(presenters.get(1));
            }
        });
        speakerName.setTypeface(Fonts.openSansLight(this));

        ImageView userImage3 = (ImageView) findViewById(R.id.profile_image3);
        userImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSpeakerDetailActivity(presenters.get(2));
            }
        });
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
        }else if(presenters.size() == 2){
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
        }else {
            userImage2.setVisibility(View.VISIBLE);
            userImage3.setVisibility(View.VISIBLE);
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
            Picasso.with(this)
                    .load(presenters.get(2).getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(userImage3);
            speakerName.setText(presenters.get(0).getName()
                    + ", " + presenters.get(1).getName()
                    + ", " + presenters.get(2).getName());
        }
    }

    private void launchSpeakerDetailActivity(Presenter presenter) {
        startActivity(new Intent(this, SpeakerDetailsActivity.class)
                .putExtra(SpeakersFragment.PRESENTER_ID, presenter.getId()));
    }

    public Session getSession() {
        return session;
    }
}
