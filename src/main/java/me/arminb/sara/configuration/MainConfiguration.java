package me.arminb.sara.configuration;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@ComponentScan("me.arminb.sara")
@EnableWebMvc
@PropertySource({"classpath:/app.properties","classpath:/local.properties"})
public class MainConfiguration{

}
