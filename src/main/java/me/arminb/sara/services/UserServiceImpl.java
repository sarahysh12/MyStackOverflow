package me.arminb.sara.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.arminb.sara.entities.User;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import me.arminb.sara.dao.UserDAO;
import me.arminb.sara.entities.User;


@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private MongoDatabase database;

    @Override
    public void findAll() {
        userDAO.findAll();
    }

    @Override
    public void addUser(User user) {
        userDAO.addUser(user);
    }

    @Override
    public void findUser(String email) {
        userDAO.findUser(email);
    }

    @Override
    public void delete(String email) {
        userDAO.delete(email);
    }
}