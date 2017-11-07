package me.arminb.sara;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Scanner;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @RequestMapping(value = "/signup")
    private void insertUser(){
        UserBean bean = new UserBean();
        System.out.println("Enter your username: ");
        Scanner scanner = new Scanner(System.in);
        bean.setUserName(scanner.nextLine());
        System.out.println("Enter your email: ");
        bean.setEmail(scanner.nextLine());
        System.out.println("Enter your password: ");
        bean.setPassword(scanner.nextLine());
        userService.addUser(bean);

    }

}
