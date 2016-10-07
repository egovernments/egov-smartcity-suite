package org.egov.infra.config.process.auth;

import org.activiti.engine.cfg.AbstractProcessEngineConfigurator;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.process.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessAuthConfigurator extends AbstractProcessEngineConfigurator{

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService userGroupService;

    @Override
    public void configure(ProcessEngineConfigurationImpl processEngineConfiguration) {
        processEngineConfiguration.setUserEntityManager(new ProcessUserManager(processEngineConfiguration, userService));
        processEngineConfiguration.setGroupEntityManager(new ProcessUserGroupManager(processEngineConfiguration, userGroupService));
    }
}
