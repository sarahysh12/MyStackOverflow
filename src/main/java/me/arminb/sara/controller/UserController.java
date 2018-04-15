package me.arminb.sara.controller;

import me.arminb.sara.controller.models.UserRequest;
import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.entities.User;
import me.arminb.sara.services.UserService;
import org.bson.types.ObjectId;
import org.eclipse.jetty.server.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> findUserById(@PathVariable("id") String id) {
        try {
            User user = userService.findUserById(id);
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


    @RequestMapping(method = RequestMethod.GET)
    public  ResponseEntity<List<User>> findAll( @RequestParam(value="username", required=false) String username,
                                             @RequestParam(value="email", required = false) String email,
                                             @RequestParam(value="page", required = false) Integer pageNumber,
                                             @RequestParam(value="pageCount", required = false) Integer pageCount) {
        try {
            List<User> users = userService.findUser(username, email, pageNumber, pageCount);
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
    public ResponseEntity<User> addUser(@RequestBody UserRequest userReq) {
        try {
            User user = userReq.toUser();
            return new ResponseEntity<User>(userService.saveUser(user), HttpStatus.OK);
        }
        catch (DataAccessException e){
            return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value= "/{id}", method = RequestMethod.PUT, consumes =  MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<User> updateUser(@PathVariable("id") String id, @RequestBody UserRequest userReq) {
        try {
            User user = userReq.toUser();
            user.setId(id);
            User userResponse = userService.saveUser(user);
            if(userResponse != null) {
                return new ResponseEntity<User>(userResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
            }
        }
        catch (DataAccessException e){
            return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable("id") String id) {
        try {
            Boolean userResponse = userService.deleteUser(id);
            if(userResponse == false) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            else{
                return new ResponseEntity(HttpStatus.OK);
            }
        }
        catch (DataAccessException e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}