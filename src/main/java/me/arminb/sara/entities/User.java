package me.arminb.sara.entities;


import com.sun.istack.internal.NotNull;
import org.bson.types.ObjectId;

//import javax.validation.constraints.Size;

public class User {

    private ObjectId id;

    private String username;

    private String email;

    private String password;


    public ObjectId getId() {return id;}

    public void setId(ObjectId id) {this.id = id;}

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

    public void setPassword(String password) {
        this.password = password;
    }
}
