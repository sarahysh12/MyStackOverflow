package me.arminb.sara.controller;

import me.arminb.sara.entities.User;
import me.arminb.sara.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public void findAll() {
        userService.findAll();
        /*
        try {
            return new ResponseEntity<Void>(userService.findAll(), HttpStatus.OK);
        }
        catch (Exception e){

            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST_400);
        }
        */
    }

    @RequestMapping(value="/?email={email}", method = RequestMethod.GET)
    public void findUser(@PathVariable("email") String email) {
        userService.findUser(email);
        /*
        try {
            return new ResponseEntity<User>(userService.findUser(email), HttpStatus.OK);
        }
        catch (Exception e){

            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST_400);
        }
        */
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> addUser(
            @RequestBody User user) {

        try {
            userService.addUser(user);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        catch (Exception e){

            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(
            @PathVariable("id") String email) {

        try {
            userService.delete(email);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        catch (Exception e){

            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
    }



}
