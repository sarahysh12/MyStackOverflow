package me.arminb.sara.entities;


public class Comment extends HistoricalBaseEntity{

    private String content;

    private String user;

    private String answer;

    public String getContent() {
        return content;
    }

    public void setContent(String comment) {
        this.content = comment;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAnswerId() {
        return answer;
    }

    public void setAnswerId(String answerId) {
        this.answer = answerId;
    }
}
