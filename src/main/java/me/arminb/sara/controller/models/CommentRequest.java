package me.arminb.sara.controller.models;


import me.arminb.sara.entities.Comment;

import java.util.Date;

public class CommentRequest {

    //TODO: user id should be taken from request after implementing login

    private String content;

    private String user;

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

    public Comment toComment(){
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setContent(content);
        comment.setModifiedAt(new Date());
        return comment;
    }

}
