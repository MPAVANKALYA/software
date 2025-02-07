package com.Project.core.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class YourComponentModelTest {

    private YourComponentModel model;

    @BeforeEach
    void setUp() throws Exception {
        model = new YourComponentModel();

        // Setting private fields using reflection
        setPrivateField(model, "path", "/content/dam/sample.jpg");
        setPrivateField(model, "bordewidth", "50");
        setPrivateField(model, "bordermarginleft", "30");
        setPrivateField(model, "borderRadius", 15);
        setPrivateField(model, "allEdges", false);
        setPrivateField(model, "leftEdge", true);
        setPrivateField(model, "topRightEdge", true);
        setPrivateField(model, "rightEdge", false);
        setPrivateField(model, "bottomleftEdge", false);
    }

    /**
     * Helper method to set private fields using reflection.
     */
    private void setPrivateField(Object object, String fieldName, Object value) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    @Test
    void testGetBorderRadius_AllEdgesSelected() throws Exception {
        setPrivateField(model, "borderRadius", 10);
        setPrivateField(model, "allEdges", true);

        assertEquals("10px", model.getBorderRadius());
    }

    @Test
    void testGetBorderRadius_CustomEdges() {
        assertEquals("15px 15px 0 0", model.getBorderRadius());
    }

    @Test
    void testGetBorderRadius_NoEdgesSelected() throws Exception {
        setPrivateField(model, "borderRadius", 20);
        setPrivateField(model, "leftEdge", false);
        setPrivateField(model, "topRightEdge", false);
        setPrivateField(model, "rightEdge", false);
        setPrivateField(model, "bottomleftEdge", false);
        setPrivateField(model, "allEdges", false);

        assertEquals("0 0 0 0", model.getBorderRadius());
    }

    @Test
    void testGetPath() {
        assertEquals("/content/dam/sample.jpg", model.getPath());
    }

    @Test
    void testGetBordewidth() {
        assertEquals("50%", model.getBordewidth());
    }

    @Test
    void testGetBordermarginleft() {
        assertEquals("30px", model.getBordermarginleft());
    }
}
