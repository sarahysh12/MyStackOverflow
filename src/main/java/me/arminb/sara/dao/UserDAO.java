package me.arminb.sara.dao;

import me.arminb.sara.entities.User;
import java.util.List;

public interface UserDAO {
    public void findAll(); //or List<User>
    public void addUser(User user); //set adduser as boolean
    public void findUser(String email); //User
    public void delete(String email);
    //update

}