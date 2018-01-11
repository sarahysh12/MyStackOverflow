package me.arminb.sara.controller;

import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.entities.User;
import me.arminb.sara.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<User>> findAll(@RequestParam(value="page_number", required=false) Integer pageNumber,
                                              @RequestParam(value="page_count", required = false) Integer pageCount) {
        try {
            List<User> users = userService.findAll(pageNumber, pageCount);

            return new ResponseEntity<List<User>>(users, HttpStatus.OK);
        }
        catch (DataAccessException e){
            return new ResponseEntity<List<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> findById(@PathVariable("id") String id) {
        try {
            User user = userService.findById(id);
            if(user != null) {
                return new ResponseEntity<User>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
            }
        }
        catch (DataAccessException e){
            return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value="/search",method = RequestMethod.GET)
    public  ResponseEntity<List<User>> find( @RequestParam(value="username", required=false) String username,
                                             @RequestParam(value="email", required = false) String email,
                                             @RequestParam(value="page_number", required = false) Integer pageNumber,
                                             @RequestParam(value="page_count", required = false) Integer pageCount) {
        try {
            List<User> users = userService.find(username, email, pageNumber, pageCount);
            if(users != null) {
                return new ResponseEntity<List<User>>(users, HttpStatus.OK);
            } else {
                return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);

            }
        }
        catch (DataAccessException e){
            return new ResponseEntity<List<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            return new ResponseEntity<User>(userService.save(user), HttpStatus.OK);
        }
        catch (DataAccessException e){
            return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> delete(@PathVariable("id") String id) {
        try {
            return new ResponseEntity<Boolean>(userService.delete(id), HttpStatus.OK);
        }
        catch (DataAccessException e){
            return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value= "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<User> update(@PathVariable("id") String id, @RequestBody User user) {
        try {
            user.setId(new ObjectId(id));
            User user_obj = userService.save(user);
            if(user_obj != null) {
                return new ResponseEntity<User>(user_obj, HttpStatus.OK);
            } else {
                return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
            }
        }
        catch (DataAccessException e){
            return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}