package com.twi.awayday2014.services.parse;


import android.util.Log;
import com.parse.*;
import com.twi.awayday2014.models.Question;
import com.twi.awayday2014.utils.Constants;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.twi.awayday2014.models.Question.newQuestion;
import static com.twi.awayday2014.utils.Constants.Parse.*;

public class QuestionService {
    private static final String TAG = "QuestionService";
    private Map<String, List<Question>> questionsMap = new HashMap<String, List<Question>>();

    public interface OnQuestionLoadListener {
        public void onQuestionLoaded();
    }

    public List<Question> getFetchedQuestionsFor(String sessionTitle){
        return questionsMap.get(sessionTitle);
    }

    public void askQuestion(Question question) {
        ParseObject qn = new ParseObject(Constants.Parse.TABLE_SESSION);
        qn.put(SESSION_ID, question.getSessionId());
        qn.put(SESSION_TITLE, question.getSessionTitle());
        qn.put(QUESTIONER_NAME, question.getName());
        qn.put(QUESTION_TEXT, question.getQuestion());
        qn.saveInBackground();
    }

    public void loadOnlyIfThereAreAnyNewQuestions(final String sessionTitle, final OnQuestionLoadListener listener) {
        final ParseQuery<ParseObject> query = newQuestionQuery(sessionTitle);
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                if (!questionsMap.containsKey(sessionTitle) || questionsMap.get(sessionTitle).size() != count) {
                    loadQuestions(sessionTitle, query, listener);
                }
            }
        });
    }

    private void loadQuestions(final String sessionTitle, ParseQuery<ParseObject> query, final OnQuestionLoadListener listener) {
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                List<Question> questions = new ArrayList<Question>();
                if (e == null) {
                    Log.d("Question", "Retrieved " + parseObjects.size() + " questions");
                    for (ParseObject parseObject : parseObjects) {
                        questions.add(newQuestion(parseObject));
                    }
                } else {
                    e.printStackTrace();
                    Log.e(TAG, "Something went wrong while fetching questions");
                }
                questionsMap.put(sessionTitle, questions);
                listener.onQuestionLoaded();
            }
        });
    }

    private ParseQuery<ParseObject> newQuestionQuery(String sessionTitle) {
        return ParseQuery.getQuery(TABLE_SESSION)
                        .whereMatches(SESSION_TITLE, sessionTitle)
                        .orderByDescending(CREATED_AT);
    }

}
