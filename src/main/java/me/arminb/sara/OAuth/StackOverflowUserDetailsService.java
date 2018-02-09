package me.arminb.sara.OAuth;

import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.dao.UserDAO;
import me.arminb.sara.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StackOverflowUserDetailsService implements UserDetailsService {
    @Autowired
    UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            List<User> user = userDAO.find(username, null, null, null);
            if (user.isEmpty()) {
                throw new UsernameNotFoundException("Bad Credentials!");
            }

            return new StackOverflowUserDetails(user.get(0).getUsername(),  user.get(0).getPassword());
        } catch (DataAccessException e) {
            throw new UsernameNotFoundException("Bad Credentials!");
        }
    }
}