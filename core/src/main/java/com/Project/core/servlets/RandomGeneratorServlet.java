package com.Project.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Random;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/randomGenerator")
public class RandomGeneratorServlet extends SlingAllMethodsServlet {

    private static final Random RANDOM = new Random();
    private static final String NUMBERS = "0123456789";
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        String type = request.getParameter("type");
        if (type == null || type.isEmpty()) {
            response.getWriter().write("Error: Missing 'type' parameter. Use 'Number', 'Letters', or 'Random'.");
            return;
        }

        String result;
        switch (type) {
            case "Number":
                result = generateRandomString(NUMBERS, 6);
                break;
            case "Letters":
                result = generateRandomString(LETTERS, 6);
                break;
            case "Random":
                result = generateMixedRandom(3, 3);
                break;
            default:
                response.getWriter().write("Error: Invalid parameter. Use 'Number', 'Letters', or 'Random'.");
                return;
        }

        response.getWriter().write(result);
    }

    private String generateRandomString(String characters, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(RANDOM.nextInt(characters.length())));
        }
        return sb.toString();
    }

    private String generateMixedRandom(int numCount, int letterCount) {
        return generateRandomString(NUMBERS, numCount) + generateRandomString(LETTERS, letterCount);
    }
}
