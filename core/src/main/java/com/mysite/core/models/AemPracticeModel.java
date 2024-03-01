package com.mysite.core.models;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

import com.day.cq.commons.Externalizer;
import com.day.cq.wcm.api.Page;

import lombok.Getter;

@Getter
@Model(adaptables = SlingHttpServletRequest.class, adapters = AemPractice.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = AemPracticeModel.RESOURCE_TYPE)
public class AemPracticeModel implements AemPractice {

    protected static final String RESOURCE_TYPE = "mysite/components/aempractice/v1/aempractice";

    @OSGiService
    private SlingSettingsService slingSettingsService;

    @ScriptVariable
    private Page currentPage;

    @ValueMapValue
    private String imagePath;

    @ValueMapValue
    @Default(booleanValues = true)
    private boolean isBackgroundImage;

    @ValueMapValue
    private String title;

    @ChildResource
    private List<NavigationItem> navigationItems;

    private Set<String> runModes;

    @PostConstruct
    private void init() {
        this.runModes = this.slingSettingsService.getRunModes();
    }

    public boolean canDisplayImage() {
        return this.isBackgroundImage && this.runModes.contains(Externalizer.PUBLISH);
    }

    public boolean isEnvironmentInAuthorMode() {
        return this.runModes.contains(Externalizer.AUTHOR);
    }

    public String getPageInfo() {
        return StringUtils.join(this.currentPage.getTitle(), StringUtils.SPACE, this.currentPage.getPath());
    }

}
