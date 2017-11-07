
package me.arminb.sara;
import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class Main {

    static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws Exception {
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.setConfigLocation("me.arminb.sara.configuration.MainConfiguration");
        applicationContext.refresh();
        JettyServer jetty = applicationContext.getBean("jettyServer", JettyServer.class);
        jetty.startJetty();
        //logger.info("Jetty has been started on port ?");

    }

}




