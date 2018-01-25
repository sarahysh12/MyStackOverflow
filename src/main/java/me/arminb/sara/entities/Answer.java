package me.arminb.sara.entities;

import java.util.List;

public class Answer extends BaseEntity{

    private String answer;

    private int rate;

    private List<Comment> comments;

    private String user;

    private String question;

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

    public String getQuestionId() {
        return question;
    }

    public void setQuestionId(String questionId) {
        this.question = questionId;
    }
}
