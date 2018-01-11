package me.arminb.sara.services;

import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.entities.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import me.arminb.sara.dao.UserDAO;

import java.util.List;


@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public List<User> findAll(int pageNumber) throws DataAccessException {
        try{
            return userDAO.findAll(pageNumber);
        }
        catch(DataAccessException e){
            throw e;
        }
    }

    @Override
    public User findById(String id) throws DataAccessException {
        try{
            return userDAO.findById(new ObjectId(id));
        }catch(DataAccessException e){
            throw e;
        }
    }

    @Override
    public List<User> find(String username, String email, int pageNumber) throws DataAccessException {
        try{
            return userDAO.find(username, email, pageNumber);
        }
        catch(DataAccessException e){
        throw e;
        }
    }

    @Override
    public boolean delete(String id) throws DataAccessException {
        try{
            return userDAO.delete(new ObjectId(id));
        }
        catch(DataAccessException e){
            throw e;
        }
    }

    @Override
    public User save(User user) throws DataAccessException {
        try{
            return userDAO.save(user);
        }
        catch(DataAccessException e){
            throw e;
        }
    }

}