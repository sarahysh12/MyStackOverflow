package me.arminb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MyConfiguration {

    @Bean
    public JettyServer jettyServer() {
        JettyServer server = new JettyServer();
        //server.setName("your foo");
        return server;
    }
}
