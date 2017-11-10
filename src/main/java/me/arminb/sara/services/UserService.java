package me.arminb.sara.services;


import me.arminb.sara.entities.User;

public interface UserService {
    public void findAll(); //or List<User>
    public void addUser(User user); //set adduser as boolean
    public void findUser(String email); //User
    public void delete(String email);
    //update
}