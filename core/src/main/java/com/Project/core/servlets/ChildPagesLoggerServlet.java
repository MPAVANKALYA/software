package com.Project.core.servlets;

import com.Project.core.config.ChildPagesLoggerConfig;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Iterator;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/logChildPages",
                "sling.servlet.methods=GET"
        }
)
@Designate(ocd = ChildPagesLoggerConfig.class)
public class ChildPagesLoggerServlet extends SlingAllMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(ChildPagesLoggerServlet.class);

    private volatile String basePagePath; // Holds the configured path

    @Activate
    @Modified
    protected void activate(ChildPagesLoggerConfig config) {
        this.basePagePath = config.basePagePath();
        LOG.info("Base page path set to: {}", basePagePath);
    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        
        if (basePagePath == null || basePagePath.isEmpty()) {
            response.getWriter().write("Configured page path is missing.");
            LOG.error("OSGi Configuration page path is not set.");
            return;
        }

        ResourceResolver resolver = request.getResourceResolver();
        if (resolver == null) {
            LOG.error("Could not obtain ResourceResolver");
            response.getWriter().write("ResourceResolver is null.");
            return;
        }

        PageManager pageManager = resolver.adaptTo(PageManager.class);
        if (pageManager == null) {
            LOG.error("PageManager could not be adapted.");
            response.getWriter().write("PageManager not available.");
            return;
        }

        Page parentPage = pageManager.getPage(basePagePath);
        if (parentPage == null) {
            LOG.error("Page not found for path: {}", basePagePath);
            response.getWriter().write("Configured page not found.");
            return;
        }

        LOG.info("Parent Page: {}", parentPage.getPath());
        response.getWriter().write("Child pages of " + basePagePath + ":\n");

        Iterator<Page> childPages = parentPage.listChildren();
        while (childPages.hasNext()) {
            Page childPage = childPages.next();
            LOG.info("Child Page: {}", childPage.getPath());
            response.getWriter().write(childPage.getPath() + "\n");
        }

        response.getWriter().write("Child pages logged successfully.");
    }
}
