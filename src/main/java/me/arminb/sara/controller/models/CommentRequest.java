package me.arminb.sara.controller.models;


import me.arminb.sara.entities.Comment;

import java.util.Date;

public class CommentRequest {

    //TODO: user id should be taken from request after implementing login

    private String content;

    private String userId;

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

    public Comment toComment(){
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setContent(content);
        return comment;
    }

}
