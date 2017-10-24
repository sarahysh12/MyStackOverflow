package Configuration;

import me.arminb.sara.JettyServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@ComponentScan("me.arminb.sara")
@Import({JettyConfiguration.class})
public class MainConfiguration {

}
