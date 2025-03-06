package com.Project.core.models;

import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.Container;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Model(adaptables = SlingHttpServletRequest.class, 
       adapters = {ContentComponentModel.class, Container.class}, 
       defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ContentComponentModel {

    @ValueMapValue
    private int columnCount;

    /**
     * Returns a list of integers representing the number of columns
     * e.g., if columnCount = 3, it returns [1, 2, 3]
     */
    public List<Integer> getColumns() {
        return IntStream.rangeClosed(1, columnCount)
                .boxed()
                .collect(Collectors.toList());
    }

    /**
     * Returns the selected column count
     */
    public int getColumnCount() {
        return columnCount;
    }
}

