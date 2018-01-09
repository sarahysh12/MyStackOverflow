package me.arminb.sara.services;

import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.entities.User;

import java.util.List;

public interface UserService {
    public List<User> findAll() throws DataAccessException;
    public User findById(String id) throws DataAccessException;
    public List<User> find(String id, String username, String email) throws DataAccessException;
    public User create(User user) throws DataAccessException;
    public boolean delete(String id) throws DataAccessException;
    public User update(String id, User user) throws DataAccessException;
}