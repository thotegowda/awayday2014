package com.twi.awayday2014.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Question;
import com.twi.awayday2014.services.parse.QuestionService;
import com.twi.awayday2014.utils.Fonts;

import java.util.List;

public class SessionDetailsActivity extends SessionDetailsBaseActivity {
    private static final String TAG = "SessionDetailsActivity";

    private LinearLayout questionsHolderView;
    private EditText questionView;
    private EditText questionerNameView;
    private View questionFrameView;
    private Button toggleQuestionFrameBtn;
    private TextView questionsTitleView;
    private QuestionService questionService;

    private static final int REFRESH_TIME = 10000;

    private static final int PULL_LATEST_QUESTIONS = 1;

    private Handler H = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PULL_LATEST_QUESTIONS:
                    Log.d(TAG, "Checking for latest questions");
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
        setupDetailText();
        setupFeedbackButton();
        setupQuestionsView();
    }

    @Override
    protected void onPause() {
        super.onPause();

        H.removeMessages(PULL_LATEST_QUESTIONS);
    }

    protected int getContentView() {
        return R.layout.activity_session_details;
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
        Intent intent = new Intent(this, FeedbackActivity.class);
        intent.putExtra(SESSION_ID, sessionId)
                .putExtra(SESSION_TYPE, sessionType);

        startActivity(intent);
    }
}
