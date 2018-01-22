package me.arminb.sara.dao;

import me.arminb.sara.entities.User;
import org.bson.types.ObjectId;

import java.util.List;

public interface UserDAO {
    public List<User> findAll(Integer pageNumber, Integer pageCount) throws DataAccessException;
    public User findById(String id) throws DataAccessException;
    public List<User> find(String username, String email, Integer pageNumber, Integer pageCount) throws DataAccessException;
    public boolean delete(String id) throws DataAccessException;
    public User save(User user) throws DataAccessException;

}