package me.arminb.sara.entities;


public class Comment extends HistoricalBaseEntity{

    private String content;

    private String userId;

    private String answerId;

    public String getContent() {
        return content;
    }

    public void setContent(String comment) {
        this.content = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String user) {
        this.userId = user;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }
}
