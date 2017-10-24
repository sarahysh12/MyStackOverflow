package me.arminb.sara;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
public class JettyServer {

    @Autowired
    WebApplicationContext webApplicationContext;

    public void startJetty() throws Exception{

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setErrorHandler(null);

        Server jettyServer = new Server(8080);
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