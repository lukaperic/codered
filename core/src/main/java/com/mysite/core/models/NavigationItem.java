package com.mysite.core.models;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import lombok.Getter;

@Getter
@Model(adaptables = Resource.class, adapters = NavigationItem.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavigationItem {

    private static final String TITLE_PREFIX = "TEST_";

    @ValueMapValue
    private String pageTitle;

    @ValueMapValue
    private String pagePath;

    @PostConstruct
    private void init() {
        this.pageTitle = StringUtils.join(TITLE_PREFIX, this.pageTitle);
    }

}
