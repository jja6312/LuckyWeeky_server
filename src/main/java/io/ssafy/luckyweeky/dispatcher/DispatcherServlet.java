package io.ssafy.luckyweeky.dispatcher;

import com.google.gson.JsonObject;
import io.ssafy.luckyweeky.application.Controller;
import io.ssafy.luckyweeky.infrastructure.config.bean.XmlBeanFactory;
import io.ssafy.luckyweeky.infrastructure.storage.S3Fileloader;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class DispatcherServlet extends HttpServlet {
    private static String webInfPath;

    public static String getWebInfPath(){
        return webInfPath;
    }


    @Override
    public void init() throws ServletException {
        webInfPath = getServletContext().getRealPath("/WEB-INF");
        try {
            String[] xmlPaths = new String[2];
            xmlPaths[0] = getServletContext().getRealPath("/WEB-INF/beans/model.xml");
            xmlPaths[1] = getServletContext().getRealPath("/WEB-INF/beans/controller.xml");
            new XmlBeanFactory(xmlPaths);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to init DispatcherServlet", e);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        S3Fileloader s3Fileloader = S3Fileloader.getInstance();
        s3Fileloader.close();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String uri = request.getRequestURI();

        String path = uri.substring(8);
        if(path.contains("qweSJqwo")){
            Controller imageController = (Controller) XmlBeanFactory.getBean("qweSJqwo");
            try {
                imageController.service(request, response, null);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
            return;
        }
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        response.setStatus(HttpServletResponse.SC_OK);
        JsonObject respJson = new JsonObject();
        respJson.addProperty("result","true");
        try {
            String uri = request.getRequestURI();
            String contextPath = "/api/v1/";
            if (!uri.contains(contextPath)) {
                respJson.addProperty("errCode","C01");
                throw new IllegalArgumentException("Invalid URI: " + uri);
            }
            String path = uri.substring(8);
            Controller controller = (Controller) XmlBeanFactory.getBean(path);
            if (controller == null) {
                respJson.addProperty("errCode","C02");
                throw new IllegalArgumentException("Controller not found: " + path);
            }
            controller.service(request, response, respJson);
        } catch (Exception e) {
            respJson.addProperty("result","false");
            respJson.addProperty("errCode",e.getMessage());
            e.printStackTrace();
        }finally {
            out.append(respJson.toString());
            out.close();
        }
    }
}
