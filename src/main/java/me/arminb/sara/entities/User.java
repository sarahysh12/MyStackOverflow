package me.arminb.sara.entities;


import com.sun.istack.internal.NotNull;
import org.bson.types.ObjectId;

//import javax.validation.constraints.Size;

public class User {

    private ObjectId _id;

    private String user_name;

    private String user_email;

    private String user_password;


    public ObjectId getId() {return _id;}

    public void setId(ObjectId id) {this._id = id;}

    public String getUsername() {
        return user_name;
    }

    public void setUsername(String username) {
        this.user_name = username;
    }

    public String getEmail() {
        return user_email;
    }

    public void setEmail(String email) {
        this.user_email = email;
    }

    public String getPassword() {
        return user_password;
    }

    public void setPassword(String password) { this.user_password = password;}
}
