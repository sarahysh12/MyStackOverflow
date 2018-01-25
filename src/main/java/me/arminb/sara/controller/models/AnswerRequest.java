package me.arminb.sara.controller.models;

import me.arminb.sara.entities.Answer;
import me.arminb.sara.entities.Comment;

import java.util.Date;
import java.util.List;


public class AnswerRequest {

    //TODO: user id should be taken from request after implementing login

    private String answer;

    private int rate;

    private List<Comment> comments;

    private String user;

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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Answer toAnswer(){
        Answer ans = new Answer();
        ans.setAnswer(answer);
        ans.setUser(user);
        ans.setRate(rate);
        ans.setComments(comments);
        ans.setModifiedAt(new Date());
        return ans;
    }

}
