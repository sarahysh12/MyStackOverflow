package me.arminb.sara;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan("me.arminb")
public class MyConfiguration {

    @Bean
    public JettyServer jettyServer() {
        JettyServer server = new JettyServer();
        return server;
    }


}
