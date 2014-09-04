package com.twi.awayday2014.models;

import com.parse.codec.binary.StringUtils;

public class Feedback {

    private Long sessionId;
    private String sessionTitle;
    private int overallRating = 0;
    private int relevantContentRating = 0;
    private int speakerQuality = 0;
    private String anythingElse;

    public Feedback(Long sessionId, String SessionTitle) {
        this.sessionId = sessionId;
        sessionTitle = SessionTitle;
    }

    public void setOverallRating(int overallRating) {
        this.overallRating = overallRating;
    }

    public void setRelevantContentRating(int relevantContentRating) {
        this.relevantContentRating = relevantContentRating;
    }

    public void setSpeakerQuality(int speakerQuality) {
        this.speakerQuality = speakerQuality;
    }

    public void setAnythingElse(String anythingElse) {
        this.anythingElse = anythingElse;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public String getSessionTitle() {
        return sessionTitle;
    }

    public int getOverallRating() {
        return overallRating;
    }

    public int getRelevantContentRating() {
        return relevantContentRating;
    }

    public int getSpeakerQuality() {
        return speakerQuality;
    }

    public String getAnythingElse() {
        return anythingElse;
    }

    public boolean isEmpty() {
        return overallRating == 0 && relevantContentRating == 0
                && speakerQuality == 0  && (anythingElse == null || anythingElse.length() >= 0);
    }
}
