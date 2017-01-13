package org.egov.works.config.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(name = "worksApplicationProperties", value = { "classpath:config/application-config-works.properties",
        "classpath:config/egov-erp-${user.name}.properties",
        "classpath:config/application-config-${client.id}.properties",
        "classpath:config/egov-erp-override.properties" }, ignoreResourceNotFound = true)
public class WorksApplicationProperties {

    @Autowired
    private Environment environment;

    public String assetRequired() {
        return environment.getProperty("asset.required", String.class);
    }

    public String locationDetailsRequired() {
        return environment.getProperty("location.required", String.class);
    }

    public Boolean lineEstimateRequired() {
        if (!environment.containsProperty("works.lineestimate.required"))
            return Boolean.TRUE;
        return environment.getProperty("works.lineestimate.required", Boolean.class);
    }
}
