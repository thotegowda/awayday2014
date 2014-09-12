package com.twi.awayday2014.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Question;
import com.twi.awayday2014.services.parse.QuestionService;
import com.twi.awayday2014.utils.Fonts;
import com.twi.awayday2014.view.fragments.AskQuestionFragment;

import java.util.List;

public class SessionDetailsActivity extends SessionDetailsBaseActivity {
    private static final String TAG = "SessionDetailsActivity";
    private static final int MAX_QUESTIONS_COUNT = 3;

    private QuestionService questionService;
    private AllQuestionsViewHelper allQuestionsViewHelper;

    private static final int REFRESH_TIME = 10000;

    private static final int PULL_LATEST_QUESTIONS = 1;

    private Handler handler = new Handler();
    private Runnable fetchQuestionsRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "Polling for new questions");
            refreshIfThereAreNewQuestions();
            handler.postDelayed(this, REFRESH_TIME);
        }
    };

    private AskQuestionFragment askQuestionFragment;
    private LinearLayout questionsHolderView1;
    private TextView noQuestionsView;
    private Button allQuestions;
    private Button askButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View allQuestionsLayout = findViewById(R.id.allQuestionsLayout);
        allQuestionsViewHelper = new AllQuestionsViewHelper(this, allQuestionsLayout);
        allQuestionsViewHelper.onCreate();

        setupDetailText();
        setupFragments();
        setupButtons();
        setupQuestionsView();
    }

    private void setupFragments() {
        askQuestionFragment = (AskQuestionFragment) getSupportFragmentManager().findFragmentById(R.id.askQuestionFragment);
    }

    private void setupButtons() {
        askButton = (Button) findViewById(R.id.askButton);
        askButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!askQuestionFragment.isOpen()) {
                    askQuestionFragment.slideIn();
                }
            }
        });

        final Button feedbackButton = (Button) findViewById(R.id.feedbackButton);
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFeedback();
            }
        });

        final Button allQuestionsButton = (Button) findViewById(R.id.allQuestionsButton);
        allQuestionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allQuestionsViewHelper.show();
                allQuestionsButton.setClickable(false);
                askButton.setClickable(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (askQuestionFragment.isOpen()) {
            askQuestionFragment.slideOut();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (session.canAskQuestions()){
            fetchQuestionsRunnable.run();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (session.canAskQuestions()){
            handler.removeCallbacks(fetchQuestionsRunnable);
        }
    }

    protected int getContentView() {
        return R.layout.activity_session_details;
    }

    private void setupDetailText() {
        TextView detailsText = (TextView) findViewById(R.id.detailsText);
        detailsText.setText(session.getDescription());
    }


    private void setupQuestionsView() {
        View questionsLayout = findViewById(R.id.questionsLayout);
        if (session.canAskQuestions()) {
            questionsLayout.setVisibility(View.VISIBLE);
            setupQuestionHeader();
            setupLatestQuestions();
        }
    }

    private void setupQuestionHeader() {
        TextView questionsHeaderText = (TextView) findViewById(R.id.questionsHeaderText);
        questionsHeaderText.setTypeface(Fonts.openSansSemiBold(this));
    }

    private void setupLatestQuestions() {
        questionsHolderView1 = (LinearLayout) findViewById(R.id.questionsHolder);
        noQuestionsView = (TextView) findViewById(R.id.noQuestionView);
        noQuestionsView.setTypeface(Fonts.openSansRegular(this));
        allQuestions = (Button) findViewById(R.id.allQuestionsButton);

        questionService = ((AwayDayApplication) getApplication()).getQuestionService();
        List<Question> fetchedQuestions = questionService.getFetchedQuestionsFor(session.getTitle());
        setupQuestionsHolder(fetchedQuestions);
        allQuestionsViewHelper.onDataChange(fetchedQuestions);
    }

    private void setupQuestionsHolder(List<Question> questions) {
        questionsHolderView1.removeAllViewsInLayout();
        if (questions == null || questions.size() == 0) {
            noQuestionsView.setVisibility(View.VISIBLE);
            allQuestions.setVisibility(View.GONE);
        } else {
            noQuestionsView.setVisibility(View.GONE);
            LayoutInflater layoutInflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < questions.size(); i++) {
                if (i == MAX_QUESTIONS_COUNT) {
                    allQuestions.setVisibility(View.VISIBLE);
                    break;
                }
                View questionView = layoutInflator.inflate(R.layout.view_question_item, null);
                TextView questionText = (TextView) questionView.findViewById(R.id.questionText);
                questionText.setTypeface(Fonts.openSansRegular(this));
                Question question = questions.get(i);
                questionText.setText(question.getQuestion());

                TextView name = (TextView) questionView.findViewById(R.id.name);
                name.setTypeface(Fonts.openSansLight(this));
                name.setText("- " + question.getName());

                TextView time = (TextView) questionView.findViewById(R.id.time);
                time.setTypeface(Fonts.openSansLight(this));
                time.setText(question.getDisplayTime());

                questionsHolderView1.addView(questionView);
            }
        }
    }

    private void refreshIfThereAreNewQuestions() {
        questionService.loadOnlyIfThereAreAnyNewQuestions(
                session.getTitle(),
                new QuestionService.OnQuestionLoadListener() {
                    @Override
                    public void onQuestionLoaded() {
                        List<Question> questions = questionService.getFetchedQuestionsFor(session.getTitle());
                        setupQuestionsHolder(questions);
                        allQuestionsViewHelper.onDataChange(questions);
                    }
                }
        );
    }


    private void launchFeedback() {
        Intent intent = new Intent(this, FeedbackActivity.class);
        intent.putExtra(SESSION_ID, sessionId)
                .putExtra(SESSION_TYPE, sessionType);

        startActivity(intent);
    }

    public void onQuestionsListHide() {
        allQuestions.setClickable(true);
        askButton.setClickable(true);
    }
}
