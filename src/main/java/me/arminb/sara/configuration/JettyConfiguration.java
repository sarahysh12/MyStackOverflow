package me.arminb.sara.configuration;

import me.arminb.sara.JettyServer;
import org.springframework.context.annotation.Bean;

public class JettyConfiguration {
    @Bean
    public JettyServer jettyServer() {
        JettyServer server = new JettyServer(8080);
        return server;
    }
}
