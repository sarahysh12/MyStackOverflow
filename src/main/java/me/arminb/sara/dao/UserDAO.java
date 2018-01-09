package me.arminb.sara.dao;

import me.arminb.sara.entities.User;
import org.bson.types.ObjectId;

import java.util.List;

public interface UserDAO {
    public List<User> findAll() throws DataAccessException;
    public User findById(ObjectId id) throws DataAccessException;
    public List<User> find(ObjectId id, String username, String email) throws DataAccessException;
    public User create(User user) throws DataAccessException;
    public boolean delete(ObjectId id) throws DataAccessException;
    public User update(User user) throws DataAccessException;

}