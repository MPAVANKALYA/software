package com.Project.core.handlers;

import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

@Component(
    service = EventHandler.class,
    immediate = true,
    property = {
        EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC
    }
)
public class PagePublishEventHandler implements EventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(PagePublishEventHandler.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    public void handleEvent(Event event) {
        ReplicationAction action = ReplicationAction.fromEvent(event);
        
        if (action != null && action.getType() == ReplicationActionType.ACTIVATE) {
            String pagePath = action.getPath();
            LOG.info("Page Published: {}", pagePath);
            
            updateJcrContent(pagePath);
        }
    }

    private void updateJcrContent(String pagePath) {
        try (ResourceResolver resourceResolver = getServiceResourceResolver()) {
            if (resourceResolver != null) {
                Resource contentResource = resourceResolver.getResource(pagePath + "/jcr:content");

                if (contentResource != null) {
                    ModifiableValueMap properties = contentResource.adaptTo(ModifiableValueMap.class);
                    if (properties != null) {
                        properties.put("changed", true);
                        resourceResolver.commit();
                        LOG.info("Updated 'changed' property to true under jcr:content for {}", pagePath);
                    }
                } else {
                    LOG.warn("No jcr:content node found for {}", pagePath);
                }
            }
        } catch (PersistenceException e) {
            LOG.error("Error saving changes to jcr:content", e);
        }
    }

    private ResourceResolver getServiceResourceResolver() {
        try {
            return resolverFactory.getServiceResourceResolver(
                    Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, "hello"));
        } catch (LoginException e) {
            LOG.error("Could not obtain a service resource resolver", e);
        }
        return null;
    }
}
