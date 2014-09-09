package com.twi.awayday2014.models;


import com.parse.ParseObject;
import com.twi.awayday2014.services.parse.QuestionService;

import java.util.Date;

public class Question {

   private String sessionId;
   private String sessionTitle;
   private String Name;
   private String question;
   private Date createdAt;

    public Question() {

    }

    public Question(String sessionId, String sessionTitle, String name, String question) {
        this.sessionId = sessionId;
        this.sessionTitle = sessionTitle;
        Name = name;
        this.question = question;
    }

    public Question(String sessionId, String sessionTitle, String name, String question, Date createdAt) {
        this.sessionId = sessionId;
        this.sessionTitle = sessionTitle;
        Name = name;
        this.question = question;
        this.createdAt = createdAt;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getSessionTitle() {
        return sessionTitle;
    }

    public String getName() {
        return Name;
    }

    public String getQuestion() {
        return question;
    }

    public Date getCreatedDate() {
        return createdAt;
    }

    public static Question newQuestion(ParseObject parseObject) {
        return new Question(
                (String) parseObject.get(QuestionService.SESSION_ID),
                (String) parseObject.get(QuestionService.SESSION_TITLE),
                (String) parseObject.get(QuestionService.QUESTIONER_NAME),
                (String) parseObject.get(QuestionService.QUESTION_TEXT),
                parseObject.getCreatedAt());

    }

}
