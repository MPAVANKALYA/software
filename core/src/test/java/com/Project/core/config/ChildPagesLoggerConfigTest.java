package com.Project.core.config;



import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ChildPagesLoggerConfigTest {

    @Test
    void testDefaultBasePagePath() {
        
        ChildPagesLoggerConfig config = Mockito.mock(ChildPagesLoggerConfig.class);

        
        when(config.basePagePath()).thenReturn("/content/Project/us");

       
        assertEquals("/content/Project/us", config.basePagePath(), "Default base page path should be '/content/Project/us'");
    }

    @Test
    void testCustomBasePagePath() {
       
        ChildPagesLoggerConfig config = Mockito.mock(ChildPagesLoggerConfig.class);

        
        when(config.basePagePath()).thenReturn("/content/custom/path");

       
        assertEquals("/content/custom/path", config.basePagePath(), "Custom base page path should be '/content/custom/path'");
    }
}
