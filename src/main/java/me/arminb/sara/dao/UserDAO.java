package me.arminb.sara.dao;

import me.arminb.sara.entities.User;
import org.bson.types.ObjectId;

import java.util.List;

public interface UserDAO {
    public List<User> findAll(int pageNumber) throws DataAccessException;
    public User findById(ObjectId id) throws DataAccessException;
    public List<User> find(String username, String email, int pageNumber) throws DataAccessException;
    public boolean delete(ObjectId id) throws DataAccessException;
    public User save(User user) throws DataAccessException;

}