package me.arminb.sara.services;

import me.arminb.sara.entities.User;

import java.util.List;

public interface UserService {
    public List<User> findAll();
    public User find(String id);
    public void create(User user);
    public void delete(String id);
    public void update(String id, User user);//or by User?
}