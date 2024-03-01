package com.mysite.core.models;

import java.util.List;

import org.osgi.annotation.versioning.ConsumerType;



@ConsumerType
public interface AemPractice {
    String getImagePath();

    boolean isBackgroundImage();

    String getTitle();

    List<NavigationItem> getNavigationItems();
}
