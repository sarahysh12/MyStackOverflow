package me.arminb.sara;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class EndPoint {
    @RequestMapping(value = "/hello")
    private String sayHello(){
        return "Hello Spring!!!";
    }

}
