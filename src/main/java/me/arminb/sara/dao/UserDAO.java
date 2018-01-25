package me.arminb.sara.dao;

import me.arminb.sara.entities.User;

import java.util.List;

public interface UserDAO {

    public User findById(String id) throws DataAccessException;
    public List<User> find(String username, String email, Integer pageNumber, Integer pageCount) throws DataAccessException;
    public boolean deleteUser(String id) throws DataAccessException;
    public User saveUser(User user) throws DataAccessException;

}