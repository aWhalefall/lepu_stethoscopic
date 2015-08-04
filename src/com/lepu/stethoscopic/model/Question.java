package com.lepu.stethoscopic.model;

/**
 * Created by guangdye on 2015/4/24.
 */
public class Question {
    private int SQID;
    private String Question;

    public void setSQID(int SQID) {
        this.SQID = SQID;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public int getSQID() {
        return SQID;
    }

    public String getQuestion() {
        return Question;
    }
}
