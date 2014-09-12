package com.twi.awayday2014.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Question;
import com.twi.awayday2014.models.Session;
import com.twi.awayday2014.services.parse.QuestionService;
import com.twi.awayday2014.utils.Fonts;
import com.twi.awayday2014.view.SessionDetailsActivity;
import com.twi.awayday2014.view.SessionDetailsBaseActivity;

public class AskQuestionFragment extends SlidingFragment {

    private EditText questionerNameView;
    private EditText questionView;
    private AskQuestionFragmentListener listener;

    @Override
    protected View getLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_ask_question, container, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setupTexts();
        setupButtons();

        return mRootLayout;
    }

    public void setListener(AskQuestionFragmentListener listener) {
        this.listener = listener;
    }

    private void setupTexts() {
        TextView infoText = (TextView) mRootLayout.findViewById(R.id.infoText);
        infoText.setTypeface(Fonts.openSansRegular(getActivity()));
        questionerNameView = (EditText) mRootLayout.findViewById(R.id.edt_name);
        questionView = (EditText) mRootLayout.findViewById(R.id.edt_question);
    }

    private void setupButtons() {
        final Button cancelButton = (Button) mRootLayout.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideOut();
            }
        });

        final Button postButton = (Button) mRootLayout.findViewById(R.id.postButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String questionerName = questionerNameView.getText().toString().trim();
                String question = questionView.getText().toString().trim();
                if (question.length() <= 0) {
                    showToast("Please enter the question ");
                    return;
                }

                if (questionerName.length() <= 0) {
                    showToast("Please enter your name ");
                    return;
                }

                AwayDayApplication application = (AwayDayApplication) getActivity().getApplication();
                QuestionService questionService = application.getQuestionService();
                Session session = ((SessionDetailsBaseActivity) getActivity()).getSession();
                questionService.askQuestion(new Question(session.getId(), session.getTitle(), questionerName, question));

                questionerNameView.setText("");
                questionView.setText("");

                if(listener != null){
                    listener.onQuestionAsked();
                }

                slideOut();
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void onPostQuestionClick() {

    }

    public interface AskQuestionFragmentListener{
        void onQuestionAsked();
    }

}
