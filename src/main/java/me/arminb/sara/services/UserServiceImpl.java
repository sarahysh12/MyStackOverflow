package me.arminb.sara.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.entities.User;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import me.arminb.sara.dao.UserDAO;
import me.arminb.sara.entities.User;

import java.util.List;


@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public List<User> findAll() throws DataAccessException {
        try{
            return userDAO.findAll();
        }
        catch(DataAccessException e){
            throw e;
        }
    }

    @Override
    public User create(User user) throws DataAccessException {
        try{
            return userDAO.create(user);
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
    public List<User> find(String id, String username, String email) throws DataAccessException {
        try{
            if (id != null)
                return userDAO.find(new ObjectId(id), username, email);
            else
                return userDAO.find(null, username, email);
    }catch(DataAccessException e){
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
    public User update(String id, User user) throws DataAccessException {
        try{
            user.setId(new ObjectId(id));
            return userDAO.update(user);
        }
        catch(DataAccessException e){
            throw e;
        }
    }
}