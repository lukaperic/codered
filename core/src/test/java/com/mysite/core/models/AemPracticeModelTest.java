package com.mysite.core.models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(AemContextExtension.class)
public class AemPracticeModelTest {

    private static final AemContext context = new AemContext();

    private static final String COMPONENT_JSON_PATH = "/models/aempractice.json";
    private static final String COMPONENT_LOCATION = "/content/mysite/us/en";

    private static final String COMPONENT_PATH = "/jcr:content/root/container/container/aempractice";

    @BeforeAll
    static void init() {
        context.load().json(COMPONENT_JSON_PATH, COMPONENT_LOCATION);
    }

    @Test
    @DisplayName("GIVEN a AEM Practice component with image as background and publish run mode WHEN it's resource type match the implementation THEN image should be able to displayed")
    void testCanDisplayImage() {
        context.currentResource(COMPONENT_LOCATION + COMPONENT_PATH);
        final AemPracticeModel model = context.request().adaptTo(AemPracticeModel.class);
        context.runMode("publish");
        assertAll(
          () -> assertNotNull(model),
          () -> assertTrue(model.canDisplayImage()));
    }

    @Test
    @DisplayName("GIVEN a AEM Practice component on a page WHEN page is loaded THEN component should display expected page info")
    void testGetPageInfo() {
        context.currentResource(COMPONENT_LOCATION + COMPONENT_PATH);
        final AemPracticeModel model = context.request().adaptTo(AemPracticeModel.class);
        assertAll(
          () -> assertNotNull(model),
          () -> assertEquals("en /content/mysite/us/en", model.getPageInfo()));
    }

    @Test
    @DisplayName("GIVEN a AEM Practice component with multiple navigation items WHEN it's properties have been set THEN expected values are returned")
    void testNavigationItems() {
        context.currentResource(COMPONENT_LOCATION + COMPONENT_PATH);
        final AemPracticeModel model = context.request().adaptTo(AemPracticeModel.class);
        assertAll(
          () -> assertNotNull(model), () -> assertNotNull(model.getNavigationItems()),
          () -> assertEquals(3, model.getNavigationItems().size()),
          () -> assertEquals("TEST_Page 1", model.getNavigationItems().get(0).getPageTitle())
        );
    }

}
