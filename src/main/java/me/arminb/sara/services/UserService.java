package me.arminb.sara.services;

import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.entities.User;
import java.util.List;

public interface UserService {

    public User findById(String id) throws DataAccessException;
    public List<User> find(String username, String email, Integer pageNumber, Integer pageCount) throws DataAccessException;
    public boolean deleteUser(String id) throws DataAccessException;
    public User saveUser(User user) throws DataAccessException;

}