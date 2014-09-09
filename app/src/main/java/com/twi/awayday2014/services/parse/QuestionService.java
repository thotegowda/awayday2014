package com.twi.awayday2014.services.parse;


import android.util.Log;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.twi.awayday2014.models.Question;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.twi.awayday2014.models.Question.newQuestion;

public class QuestionService {

    public interface OnQuestionLoadListener {
        public void onQuestionLoaded(List<Question> questions);
    }

    public static String TABLE_SESSION = "Questions";
    public static String SESSION_ID = "sessionId";
    public static String SESSION_TITLE = "sessionTitle";
    public static String QUESTIONER_NAME = "questionerName";
    public static String QUESTION_TEXT = "questionText";
    public static String CREATED_AT = "createdAt";

    public void askQuestion(Question question) {
        ParseObject qn = new ParseObject(TABLE_SESSION);
        qn.put(SESSION_ID, question.getSessionId());
        qn.put(SESSION_TITLE, question.getSessionTitle());
        qn.put(QUESTIONER_NAME, question.getName());
        qn.put(QUESTION_TEXT, question.getQuestion());
        qn.saveInBackground();
    }

    public void loadQuestions(String sessionId, final OnQuestionLoadListener listener) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_SESSION)
                .whereMatches(SESSION_ID, sessionId)
                .orderByDescending(CREATED_AT);
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
                    questions.add(new Question("", "", "Failed", "Server is down or network not reachable"));
                }
                listener.onQuestionLoaded(questions);
            }
        });
    }

    public static String getDisplayTime(LocalDateTime then) {
        LocalDateTime now = LocalDateTime.now();

        int seconds = Math.abs(Seconds.secondsBetween(now, then).getSeconds());
        if (seconds <= 10) {
            return "now";
        }
        if (seconds < 60) {
            return String.valueOf(seconds) + "s";
        }

        int minutes = Math.abs(Minutes.minutesBetween(now, then).getMinutes());
        if (minutes < 60) {
            return String.valueOf(minutes) + "m";
        }

        int hours = Math.abs(Hours.hoursBetween(now, then).getHours());
        if (hours < 24) {
            return String.valueOf(hours) + "h";
        }

        int days = Math.abs(Days.daysBetween(now, then).getDays());
        if (days < 9) {
            return String.valueOf(days) + "d";
        }

        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
        return then.toString(formatter);
    }

    public static String formatDate(Date date) {
        return getDisplayTime(new LocalDateTime(date));
    }
}
