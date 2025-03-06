package com.Project.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Header1 {

    @ValueMapValue
   private String additionaltext;

    @ValueMapValue
   private String datepicker;

    public String getAdditionaltext() {
        return additionaltext;
    }

    public String getDatepicker() {
        return datepicker;
    }
}
