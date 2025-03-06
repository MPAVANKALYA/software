package com.Project.core.models;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Header {
    @ValueMapValue
    private String textfield;
    @ValueMapValue
    private String pathfield;
    @ChildResource
    private List<Header1> multifield;
    @ValueMapValue
    private String checkbox;

    public String getTextfield() {
        return textfield;
    }

    public String getPathfield() {
        return pathfield;
    }

    public List<Header1> getMultifield() {
        return multifield;
    }

    public String getCheckbox() {
        return checkbox;
    }

}
