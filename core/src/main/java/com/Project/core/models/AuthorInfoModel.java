package com.Project.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AuthorInfoModel {

    @ValueMapValue
    private String listFrom;

    @ChildResource(name = "authors")
    private Resource authorsResource;

    public String getListFrom() {
        return listFrom;
    }

    public List<AuthorDetails> getAuthors() {
        List<AuthorDetails> authorsList = new ArrayList<>();
        if (authorsResource != null) {
            for (Resource child : authorsResource.getChildren()) {
                AuthorDetails author = Optional.ofNullable(child.adaptTo(AuthorDetails.class)).orElse(null);
                if (author != null) {
                    authorsList.add(author);
                }
            }
        }
        return authorsList;
    }

    @Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class AuthorDetails {

        @ValueMapValue
        private String fname;

        @ValueMapValue
        private String lname;

        @ValueMapValue
        private String address;

        @ValueMapValue
        private String pnum;

        @ValueMapValue
        private String path;

        public String getFname() {
            return fname;
        }

        public String getLname() {
            return lname;
        }

        public String getAddress() {
            return address;
        }

        public String getPnum() {
            return pnum;
        }

        public String getPath() {
            return path;
        }
    }
}
