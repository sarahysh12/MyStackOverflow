package me.arminb.sara.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@ComponentScan("me.arminb.sara")
@Import({JettyConfiguration.class})
public class MainConfiguration {

}
