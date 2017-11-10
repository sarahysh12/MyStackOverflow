package me.arminb.sara.jetty;

import org.eclipse.jetty.annotations.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.web.WebApplicationInitializer;

import java.io.IOException;
import java.util.Properties;

public class JettyServer {

    private final String[] PROPERTIES_FILES = {"/app.properties", "/local.properties"};
    Properties properties;

    public JettyServer() {
        try {
            properties = new Properties();
            for (String file: PROPERTIES_FILES) {
                properties.load(this.getClass().getResourceAsStream(file));
            }
        } catch (IOException e) { }
    }

    public void startJetty() throws Exception{

        WebAppContext context = new WebAppContext();

        context.setContextPath("/");
        context.setErrorHandler(null);

        context.setConfigurations(
                new org.eclipse.jetty.webapp.Configuration[] { new AnnotationConfiguration() {
                    @Override
                    public void preConfigure(WebAppContext context) throws Exception {
                        final ClassInheritanceMap map = new ClassInheritanceMap();
                        final ConcurrentHashSet<String> set = new ConcurrentHashSet<String>();
                        set.add(WebAppInitializer.class.getName());
                        map.put(WebApplicationInitializer.class.getName(), set);
                        context.setAttribute(CLASS_INHERITANCE_MAP, map);
                        _classInheritanceHandler = new ClassInheritanceHandler(map);
                    }
                } });

        Server jettyServer = new Server(Integer.parseInt(properties.getProperty("jetty.port")));
        jettyServer.setHandler(context);


        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }

    }
}