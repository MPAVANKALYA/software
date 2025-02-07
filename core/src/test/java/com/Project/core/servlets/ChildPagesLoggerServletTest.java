package com.Project.core.servlets;

import com.Project.core.config.ChildPagesLoggerConfig;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChildPagesLoggerServletTest {

    @InjectMocks
    private ChildPagesLoggerServlet servlet;

    @Mock
    private ChildPagesLoggerConfig config;

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private SlingHttpServletResponse response;

    @Mock
    private ResourceResolver resourceResolver;

    @Mock
    private PageManager pageManager;

    @Mock
    private Page parentPage;

    @Mock
    private Iterator<Page> childPagesIterator;

    @Mock
    private Page childPage;

    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws IOException {
        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        // ✅ Fix: Properly Mock OSGi Configuration
        when(config.basePagePath()).thenReturn("/content/Project/us");
        servlet.activate(config); // Ensure OSGi config is set before tests
    }

    @Test
    void testDoGet_Success() throws ServletException, IOException {
        when(request.getResourceResolver()).thenReturn(resourceResolver);
        when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
        when(pageManager.getPage("/content/Project/us")).thenReturn(parentPage);
        when(parentPage.getPath()).thenReturn("/content/Project/us");
        when(parentPage.listChildren()).thenReturn(Collections.singletonList(childPage).iterator());
        when(childPage.getPath()).thenReturn("/content/Project/us/child");

        servlet.doGet(request, response);

        String result = responseWriter.toString();
        assertTrue(result.contains("Child pages of /content/Project/us:"));
        assertTrue(result.contains("/content/Project/us/child"));
        assertTrue(result.contains("Child pages logged successfully."));
    }

    @Test
    void testDoGet_PagePathNotSet() throws ServletException, IOException {
        when(config.basePagePath()).thenReturn(null);
        servlet.activate(config);

        servlet.doGet(request, response);

        String result = responseWriter.toString();
        assertTrue(result.contains("Configured page path is missing."));
    }

    @Test
    void testDoGet_ResourceResolverNull() throws ServletException, IOException {
        when(request.getResourceResolver()).thenReturn(null);

        servlet.doGet(request, response);

        String result = responseWriter.toString();
        assertTrue(result.contains("ResourceResolver is null."));
    }

    @Test
    void testDoGet_PageManagerNull() throws ServletException, IOException {
        when(request.getResourceResolver()).thenReturn(resourceResolver);
        when(resourceResolver.adaptTo(PageManager.class)).thenReturn(null);

        servlet.doGet(request, response);

        String result = responseWriter.toString();
        assertTrue(result.contains("PageManager not available."));
    }

    @Test
    void testDoGet_PageNotFound() throws ServletException, IOException {
        when(request.getResourceResolver()).thenReturn(resourceResolver);
        when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
        when(pageManager.getPage("/content/Project/us")).thenReturn(null);

        servlet.doGet(request, response);

        String result = responseWriter.toString();
        assertTrue(result.contains("Configured page not found."));
    }

    @Test
    void testDoGet_HandlesException() throws ServletException, IOException {
        // ✅ Fix: Ensure the Resource Resolver is Not Null
        when(request.getResourceResolver()).thenReturn(resourceResolver);

        // ✅ Fix: Use `doThrow()` Instead of `when().thenThrow()`
        doThrow(new RuntimeException("Unexpected error"))
                .when(resourceResolver).adaptTo(PageManager.class);

        // ✅ Call Servlet Method
        servlet.doGet(request, response);

        // ✅ Verify the Response
        String result = responseWriter.toString();
        assertTrue(result.contains("An error occurred. Check logs."));
    }
}
