package com.Project.core.models;



import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = { org.apache.sling.api.SlingHttpServletRequest.class,
        org.apache.sling.api.resource.Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class YourComponentModel {

    @ValueMapValue
    private String path;
    @ValueMapValue
    private String bordewidth;
    @ValueMapValue
    private String bordermarginleft;
    @ValueMapValue
    private boolean rightEdge;
    @ValueMapValue
    private boolean leftEdge;

    @ValueMapValue
    private boolean bottomleftEdge;
    @ValueMapValue
    private boolean topRightEdge;

    @ValueMapValue
    private boolean allEdges;

    @ValueMapValue int borderRadius;
    private String r;

    public String getBorderRadius() {
        String radius = borderRadius + "px";

        // Compute the border-radius values based on selections
        String topLeft = leftEdge ? radius : "0";
        String topRight = topRightEdge ? radius : "0";
        String bottomRight = rightEdge ? radius : "0";
        String bottomLeft = bottomleftEdge ? radius : "0";
        r = topLeft + " " + topRight + " " + bottomRight + " " + bottomLeft;
        // If all edges are selected, apply uniform rounding
        return allEdges ? radius : r;
    }

    public String getPath() {
        return path;
    }

    public String getBordewidth() {
        return bordewidth + "%";
    }

    public String getBordermarginleft() {
        return bordermarginleft + "px";
    }
}