package me.arminb.sara.services;

import me.arminb.sara.entities.User;

import java.util.List;

public interface UserService {
    public List<User> findAll();
    public User find(String id);
    public User create(User user);
    public boolean delete(String id);
    public User update(String id, User user);
}