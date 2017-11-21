package me.arminb.sara.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.arminb.sara.entities.User;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import me.arminb.sara.dao.UserDAO;
import me.arminb.sara.entities.User;

import java.util.List;


@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private MongoDatabase database;

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public void create(User user) {
        userDAO.create(user);
    }

    @Override
    public User find(String id) {return userDAO.find(id);}

    @Override
    public void delete(String id) {
        userDAO.delete(id);
    }

    @Override
    public void update(String id, User user) { userDAO.update(id, user); }
}