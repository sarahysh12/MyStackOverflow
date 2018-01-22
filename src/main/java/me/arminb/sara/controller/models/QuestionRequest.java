package me.arminb.sara.controller.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import me.arminb.sara.entities.Answer;
import me.arminb.sara.entities.Question;

import java.io.IOException;
import java.util.Date;
import java.util.List;


public class QuestionRequest {

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private Date date;

    private String title;

    private String content;

    private List<Answer> answers;

    private int rate;

    private List<String> tags;

    private String user;

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

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String arrayToJson = null;
        try {
            arrayToJson = objectMapper.writeValueAsString(answers);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        TypeReference<Answer> mapType = new TypeReference<Answer>() {};
        try {
            Answer jsonToPersonList = objectMapper.readValue(arrayToJson, mapType);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Question toQuestion() {
        Question question = new Question();
        question.setTitle(title);
        question.setRate(rate);
        question.setDate(new Date());
        question.setContent(content);
        question.setAnswers(answers);
        question.setUser(user);
        question.setTags(tags);
        return question;
    }


}
