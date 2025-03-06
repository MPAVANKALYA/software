package com.Project.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/getBoolean")
public class GetBooleanPropertyServlet extends SlingSafeMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(GetBooleanPropertyServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        // Read component path from request parameter
        String resourcePath = request.getParameter("path");

        if (resourcePath == null || resourcePath.isEmpty()) {
            response.getWriter().write("Error: Missing 'path' parameter.");
            return;
        }

        // Get the subNode resource
        Resource resource = request.getResourceResolver().getResource(resourcePath);

        if (resource == null) {
            response.getWriter().write("Error: Component not found at: " + resourcePath);
            return;
        }

        ValueMap properties = resource.getValueMap();
        boolean booleanValue = properties.get("value", false); // Default to false if property is missing

        LOG.info("Boolean Property Value: {}", booleanValue);

        response.getWriter().write("Boolean Property Value: " + booleanValue);
    }
}
