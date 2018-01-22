package me.arminb.sara.services;

import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.entities.User;
import java.util.List;

public interface UserService {
    public List<User> findAll(Integer pageNumber, Integer pageCount) throws DataAccessException;
    public User findById(String id) throws DataAccessException;
    public List<User> find(String username, String email, Integer pageNumber, Integer pageCount) throws DataAccessException;
    public boolean delete(String id) throws DataAccessException;
    public User save(User user) throws DataAccessException;
}