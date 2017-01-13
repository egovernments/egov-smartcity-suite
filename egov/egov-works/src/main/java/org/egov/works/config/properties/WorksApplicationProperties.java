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
    
    public String[] getContractorMasterMandatoryFields(){
        return environment.getProperty("contractormaster.fields.mandatory").split(",");
    }
    
    public String[] getContractorMasterHideFields(){
        return environment.getProperty("contractormaster.fields.hide").split(",");
    }
}
