package com.Project.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import org.osgi.service.component.annotations.Component;
import org.osgi.framework.Constants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import com.google.common.collect.ImmutableMap;

@Component(service = Servlet.class,
           property = {
              Constants.SERVICE_DESCRIPTION + "=GET Request Servlet",
              "sling.servlet.methods=" + "GET",
              "sling.servlet.paths=" + "/bin/getWeather"
           })
public class GetRequestServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        // OpenWeather API URL (GET request) - Updated to New York
        String url = "http://api.openweathermap.org/data/2.5/weather?q=New%20York&appid=d67f82e1d07ae6d59db2a647226b1d79";
        
        // Send GET request to the API
        HttpClient client = HttpClients.createDefault();
        HttpGet getRequest = new HttpGet(url);
        HttpResponse httpResponse = client.execute(getRequest);
        
        // Read the response
        String responseString = EntityUtils.toString(httpResponse.getEntity());
        
        // Store the response in the JCR (CRXDE Lite)
        ResourceResolver resolver = request.getResourceResolver();
        try {
            // Check if the resource path exists, if not create it
            Resource contentResource = resolver.getResource("/content");
            if (contentResource == null) {
                // Create content path if it doesn't exist
                resolver.create(resolver.getResource("/"), "content", ImmutableMap.of());
            }
            // Create a new node under /content with the name "weatherData"
            resolver.create(resolver.getResource("/content"), "weatherData", ImmutableMap.of("response", responseString));
            
            // Commit the changes
            resolver.commit();
        } catch (Exception e) {
            // Log the error (you can enhance this for better logging)
            e.printStackTrace();
        }
        
        // Send the response back to the browser
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseString);
        out.flush();
    }
}
