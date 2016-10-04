package org.egov.infra.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourceFinderUtil extends PathMatchingResourcePatternResolver {

    public List<Resource> getResources(String... locationPattern) {
        Assert.notNull(locationPattern, "Location pattern must not be null");

        List<Resource> resources = new ArrayList<>();
        for (String location : locationPattern) {
            try {
                resources.addAll(Arrays.asList(super.getResources(location)));
            } catch (Exception e) {
                //Ignore this expecting this
            }
        }
        return resources;

    }
}
