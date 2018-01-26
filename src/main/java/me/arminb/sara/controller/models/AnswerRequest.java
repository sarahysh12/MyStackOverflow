package me.arminb.sara.controller.models;

import me.arminb.sara.entities.Answer;
import me.arminb.sara.entities.Comment;

import java.util.Date;
import java.util.List;


public class AnswerRequest {

    //TODO: user id should be taken from request after implementing login

    private String answer;

    private int rate;

    private String userId;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String user) {
        this.userId = user;
    }

    public Answer toAnswer(){
        Answer ans = new Answer();
        ans.setAnswer(answer);
        ans.setUserId(userId);
        ans.setRate(rate);
        return ans;
    }

}
