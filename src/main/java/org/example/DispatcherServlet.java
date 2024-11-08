package org.example;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@WebServlet(urlPatterns = "/*")
public class DispatcherServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getRequestURI();
        System.out.println(path);
        
        if(Objects.requireNonNull(path).equals("/hello")){
            System.out.println("hello");
        }else{
            System.out.println("ho");
        }

    }
}
