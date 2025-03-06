package com.Project.core.schedulers;


import org.apache.sling.api.resource.*;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Project.core.config.PagePublisherSchedulerConfig;
import com.adobe.cq.wcm.core.components.models.Page;
import com.day.cq.replication.*;

import java.util.Iterator;
import java.util.Map;

import javax.jcr.Session;

@Component(service = Runnable.class, immediate = true, 
    configurationPid = "com.example.core.config.PagePublisherSchedulerConfig", 
    property = {
        "scheduler.expression=0 0/5 * * * ?", // Default: Every 5 minutes
        "scheduler.concurrent=false"
})
public class PagePublisherScheduler implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(PagePublisherScheduler.class);

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private Replicator replicator;

    @Reference
    private SlingSettingsService slingSettingsService;

    private String cronExpression;
    private String pagePath;

    @Activate
    @Modified
    protected void activate(PagePublisherSchedulerConfig config) {
        this.cronExpression = config.cronExpression();
        this.pagePath = config.pagePath();
        LOG.info("Scheduler activated with cron: {} and path: {}", cronExpression, pagePath);
    }

    @Override
    public void run() {
        LOG.info("Executing Page Publisher Scheduler...");

        if (pagePath == null || pagePath.isEmpty()) {
            LOG.warn("Page path is not configured.");
            return;
        }

        try (ResourceResolver resolver = getServiceResourceResolver()) {
            if (resolver == null) {
                LOG.error("Failed to get ResourceResolver.");
                return;
            }

            Resource rootPage = resolver.getResource(pagePath);
            if (rootPage == null) {
                LOG.warn("Page path {} does not exist.", pagePath);
                return;
            }

            publishPages(rootPage, resolver);
        } catch (Exception e) {
            LOG.error("Error executing scheduler", e);
        }
    }

    private void publishPages(Resource rootPage, ResourceResolver resolver) {
        Iterator<Resource> children = rootPage.listChildren();
        while (children.hasNext()) {
            Resource child = children.next();
            Page page = child.adaptTo(Page.class);
            if (page != null) {
                String pagePath = ((Resource) page).getPath();
                try {
                    replicator.replicate(resolver.adaptTo(Session.class), ReplicationActionType.ACTIVATE, pagePath);
                    LOG.info("Published page: {}", pagePath);
                } catch (ReplicationException e) {
                    LOG.error("Failed to publish page: {}", pagePath, e);
                }
            }
        }
    }

    private ResourceResolver getServiceResourceResolver() {
        try {
            return resourceResolverFactory.getServiceResourceResolver(
                Map.of(ResourceResolverFactory.SUBSERVICE, "hello")); // Use your subservice
        } catch (LoginException e) {
            LOG.error("Failed to obtain service resource resolver", e);
            return null;
        }
    }
}
