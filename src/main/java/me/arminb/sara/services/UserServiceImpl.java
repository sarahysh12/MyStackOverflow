package me.arminb.sara.services;

import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import me.arminb.sara.dao.UserDAO;

import java.util.List;



@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public User findUserById(String id) throws DataAccessException {
        try{
            return userDAO.findById(id);
        }catch(DataAccessException e){
            throw e;
        }
    }

    @Override
    public List<User> findUser(String username, String email, Integer pageNumber, Integer pageCount) throws DataAccessException {
        try{
            return userDAO.find(username, email, pageNumber, pageCount);
        }
        catch(DataAccessException e){
        throw e;
        }
    }

    @Override
    public boolean deleteUser(String id) throws DataAccessException {
        try{
            return userDAO.delete(id);
        }
        catch(DataAccessException e){
            throw e;
        }
    }

    @Override
    public User saveUser(User user) throws DataAccessException {
        try{
            user.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(user.getPassword()));
            return userDAO.save(user);
        }
        catch(DataAccessException e){
            throw e;
        }
    }

}