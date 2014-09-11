package com.twi.awayday2014.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.squareup.picasso.Picasso;
import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.models.Question;
import com.twi.awayday2014.models.Session;
import com.twi.awayday2014.services.parse.AgendaParseDataFetcher;
import com.twi.awayday2014.services.parse.PresenterParseDataFetcher;
import com.twi.awayday2014.services.parse.QuestionService;
import com.twi.awayday2014.utils.Fonts;
import com.twi.awayday2014.view.fragments.SpeakersFragment;

import java.util.ArrayList;
import java.util.List;

public class SessionDetailsActivity extends Activity {

    public static final String TAG = "Questions";

    private Session session;
    private List<Presenter> presenters;
    private LinearLayout questionsHolderView;
    private EditText questionView;
    private EditText questionerNameView;
    private View questionFrameView;
    private Button toggleQuestionFrameBtn;
    private TextView questionsTitleView;
    private QuestionService questionService;

    private static final int REFRESH_TIME = 30000;

    private static final int PULL_LATEST_QUESTIONS = 1;

    private Handler H = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PULL_LATEST_QUESTIONS:
                    refreshIfThereAreNewQuestions();
                    sendEmptyMessageDelayed(PULL_LATEST_QUESTIONS, REFRESH_TIME);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

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
        setupQuestionsView();
    }

    @Override
    protected void onPause() {
        super.onPause();

        H.removeMessages(PULL_LATEST_QUESTIONS);
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

        if (presenters.size() == 0) {
            speakerName.setVisibility(View.GONE);
            speakerInfoLayout.setVisibility(View.GONE);
            return;
        } else if (presenters.size() == 1) {
            Picasso.with(this)
                    .load(presenters.get(0).getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(userImage1);
            speakerName.setText(presenters.get(0).getName());
        } else {
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

    private void launchSpeakerDetailActivity(Presenter presenter) {
        startActivity(new Intent(this, SpeakerDetailsActivity.class)
                .putExtra(SpeakersFragment.PRESENTER_ID, presenter.getId()));
    }

    private void setupQuestionsView() {
        View questionsLayout = findViewById(R.id.questions_layout);
        if (session.canAskQuestions()) {
            questionsLayout.setVisibility(View.VISIBLE);
            bindViews();
            loadQuestions();
        } else {
            questionsLayout.setVisibility(View.GONE);
        }
    }

    private void bindViews() {
        questionFrameView = findViewById(R.id.question_frame);
        questionFrameView.setVisibility(View.GONE);

        questionsTitleView = (TextView) findViewById(R.id.questions_title);
        questionsTitleView.setTypeface(Fonts.openSansBold(this));
        toggleQuestionFrameBtn = (Button) findViewById(R.id.btn_toggle_question_frame);

        questionView = (EditText) findViewById(R.id.edt_question);
        questionerNameView = (EditText) findViewById(R.id.edt_name);
        toggleQuestionFrameBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                toggleQuestionFrameView();
            }
        });
        findViewById(R.id.btn_ask_question).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onPostQuestionClick();
            }
        });
        questionsHolderView = (LinearLayout) findViewById(R.id.questions_holder);

        questionService = ((AwayDayApplication) getApplication()).getQuestionService();
    }

    private void refreshIfThereAreNewQuestions() {
        questionService.loadOnlyIfThereAreAnyNewQuestions(
                session.getId(),
                questionsHolderView.getChildCount(),
                new QuestionService.OnQuestionLoadListener() {
                    @Override
                    public void onQuestionLoaded(List<Question> questions) {
                        Log.d(TAG, "Recent question count : " + questions.size());
                        displayQuestions(questions);
                    }
                });
    }

    private void loadQuestions() {

        questionService.loadQuestions(session.getId(), new QuestionService.OnQuestionLoadListener() {
            @Override
            public void onQuestionLoaded(List<Question> questions) {
                displayQuestions(questions);
                H.sendEmptyMessageDelayed(PULL_LATEST_QUESTIONS, REFRESH_TIME);
            }
        });
    }

    private void displayQuestions(List<Question> questions) {
        questionsHolderView.removeAllViewsInLayout();

        if (questions.size() == 0) {
            bindQuestion(newQuestionView(), noQuestion());
        }

        for (Question question : questions) {
            bindQuestion(newQuestionView(), question);
        }
    }

    private void onPostQuestionClick() {
        String questionerName = questionerNameView.getText().toString().trim();
        String question = questionView.getText().toString().trim();

        if (question.length() <= 0) {
            Toast.makeText(this, "Please enter the question ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (questionerName.length() <= 0) {
            Toast.makeText(this, "Please enter your name ", Toast.LENGTH_SHORT).show();
            return;
        }

        questionService.askQuestion(new Question(session.getId(), session.getTitle(), questionerName, question));
        questionFrameView.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshQuestions();
            }
        }, 2000);

        questionerNameView.setText("");
        questionView.setText("");
        toggleQuestionFrameView();
    }

    private void refreshQuestions() {
        loadQuestions();
    }


    private void toggleQuestionFrameView() {
        boolean show = questionFrameView.getVisibility() == View.GONE;
        questionFrameView.setVisibility(show == true ? View.VISIBLE : View.GONE);
        toggleQuestionFrameBtn.setText(show == true ? R.string.cancel : R.string.ask);
    }

    private Question noQuestion() {
        return new Question("", "", "No questions yet.", "You can be the first one!");
    }

    private View newQuestionView() {
        return getLayoutInflater().inflate(R.layout.question_item, questionsHolderView, false);
    }

    private void bindQuestion(View view, Question question) {

        TextView questionerName = (TextView) view.findViewById(R.id.questioner_name);
        questionerName.setTypeface(Fonts.openSansSemiBold(this));
        questionerName.setText(question.getName());

        TextView questionTime = (TextView) view.findViewById(R.id.question_time);
        questionTime.setTypeface(Fonts.openSansLight(this));
        questionTime.setText(QuestionService.formatDate(question.getCreatedDate()));

        TextView questionText = (TextView) view.findViewById(R.id.question_text);
        questionText.setTypeface(Fonts.openSansRegular(this));
        questionText.setText(question.getQuestion());

        view.setTag(question);
        questionsHolderView.addView(view);
    }

    private void launchFeedback() {
        startActivity(new Intent(this, FeedbackActivity.class).putExtra("session_id", String.valueOf(session.getId())));
    }
}
