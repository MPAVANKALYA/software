package com.Project.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Child Pages Logger Configuration")
public @interface ChildPagesLoggerConfig {

    @AttributeDefinition(
        name = "Base Page Path",
        description = "Enter the base page path from where child pages will be logged"
    )
    String basePagePath() default "/content/Project/us";
}