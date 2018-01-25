package me.arminb.sara.controller.models;

import me.arminb.sara.entities.User;
import org.bson.types.ObjectId;

import java.util.Date;

public class UserRequest {

    private String username;

    private String email;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { this.password = password;}


    public User toUser() {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setModifiedAt(new Date());
        return user;
    }
}
