package me.arminb.sara.controller;

import me.arminb.sara.entities.User;
import me.arminb.sara.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.util.Scanner;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(value="findall", method = RequestMethod.GET)
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

    @RequestMapping(value="find/{email}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
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

    @RequestMapping(value="create", method = RequestMethod.POST)
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


    @RequestMapping(value="delete/{id}", method = RequestMethod.DELETE)
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
