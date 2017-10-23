
package me.arminb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class main {

    static final Logger logger = LoggerFactory.getLogger(main.class);
    public static void main(String[] args) throws Exception {
        logger.info("*********************HELLO****************************");
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MyConfiguration.class);
        JettyServer jetty = applicationContext.getBean("jettyServer", JettyServer.class);
        jetty.startJetty();

    }

}