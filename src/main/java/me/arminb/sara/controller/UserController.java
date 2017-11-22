package me.arminb.sara.controller;

import me.arminb.sara.entities.User;
import me.arminb.sara.services.UserService;
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
    public ResponseEntity<List<User>> findAll() {
        try {
            List<User> users = userService.findAll();
            return new ResponseEntity<List<User>>(users, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<List<User>>(HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> findUser(@PathVariable("id") String id) {
        try {
            User user = userService.find(id);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            return new ResponseEntity<User>(userService.create(user), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> delete(@PathVariable("id") String id) {
        try {
            return new ResponseEntity<Boolean>(userService.delete(id), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value= "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<User> update(@PathVariable("id") String id, @RequestBody User user) {
        try {
            return new ResponseEntity<User>(userService.update(id, user), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }
    }

}