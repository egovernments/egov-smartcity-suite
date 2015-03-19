package org.egov.infra.security.auditing;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component("springSecurityAwareAuditor")
public class SpringSecurityAwareAuditor implements AuditorAware<User> {

    @Autowired
    private SecurityUtils securityUtils;
    
    @Override
    public User getCurrentAuditor() {
        return securityUtils.getCurrentUser();
    }

}