package me.arminb.sara.services;

import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.entities.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import me.arminb.sara.dao.UserDAO;

import java.util.List;

import static me.arminb.sara.constants.PAGE_COUNT;
import static me.arminb.sara.constants.PAGE_NUMBER;


@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public List<User> findAll(Integer pageNumber, Integer pageCount) throws DataAccessException {
        try{
            if (pageNumber == null){
                pageNumber = PAGE_NUMBER;
            }
            if (pageCount == null){
                pageCount = PAGE_COUNT;
            }
            return userDAO.findAll(pageNumber, pageCount);
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
    public List<User> find(String username, String email, Integer pageNumber, Integer pageCount) throws DataAccessException {
        try{
            if (pageNumber == null){
                pageNumber = PAGE_NUMBER;
            }
            if (pageCount == null){
                pageCount = PAGE_COUNT;
            }
            return userDAO.find(username, email, pageNumber, pageCount);
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