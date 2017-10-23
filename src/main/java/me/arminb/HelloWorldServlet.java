package me.arminb;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;


public class HelloWorldServlet extends HttpServlet{

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().println("This is my first servlet");
    }


}