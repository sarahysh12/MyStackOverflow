
package me.arminb.sara;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class Main {

    static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws Exception {
        logger.info("*********************HELLO****************************");
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext(MyConfiguration.class);
        JettyServer jetty = applicationContext.getBean("jettyServer", JettyServer.class);
        jetty.startJetty();


    }

}