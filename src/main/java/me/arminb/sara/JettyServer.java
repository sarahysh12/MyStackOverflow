package me.arminb.sara;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Component
public class JettyServer {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Value("${jetty.port:8000}")
    int portNumber;

    public void startJetty() throws Exception{

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setErrorHandler(null);

        Server jettyServer = new Server(portNumber);
        jettyServer.setHandler(context);

        context.addServlet(new ServletHolder(new DispatcherServlet(webApplicationContext)), "/*");
        context.addEventListener(new ContextLoaderListener(webApplicationContext));
        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }

    }

}