package me.arminb.sara.configuration;

import org.springframework.context.annotation.*;


@Configuration
@ComponentScan("me.arminb.sara")
@PropertySource({"classpath:/app.properties","classpath:/local.properties"})
public class MainConfiguration {

}
