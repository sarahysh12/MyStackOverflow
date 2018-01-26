package me.arminb.sara.controller.models;

import me.arminb.sara.entities.Question;

import java.util.Date;
import java.util.List;


public class QuestionRequest {

    //TODO: user id should be taken from request after implementing login

    private String title;

    private String content;

    private int rate;

    private List<String> tags;

    private String userId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getUser() {
        return userId;
    }

    public void setUser(String user) {
        this.userId = user;
    }

    public Question toQuestion() {
        Question question = new Question();
        question.setTitle(title);
        question.setRate(rate);
        question.setContent(content);
        question.setUser(userId);
        question.setTags(tags);
        return question;
    }


}
