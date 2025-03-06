package com.Project.core.config;


import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Page Publisher Scheduler Configuration")
public @interface PagePublisherSchedulerConfig {

    @AttributeDefinition(name = "Cron Expression", description = "Set the cron expression for scheduling")
    String cronExpression() default "0 0/5 * * * ?"; // Every 5 minutes

    @AttributeDefinition(name = "Page Path", description = "Path of the page under which all pages will be published")
    String pagePath() default "/content/Project/us/en";
}
