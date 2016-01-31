package org.egov.mrs.application;

import java.util.List;
import java.util.stream.Collectors;

import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Utils {
    
    @Autowired
    private SecurityUtils securityUtils;
    
    public boolean isLoggedInUserApprover() {
        List<Role> approvers = securityUtils.getCurrentUser().getRoles().stream()
                .filter(role -> role.getName().equalsIgnoreCase(Constants.APPROVER_ROLE_NAME)).collect(Collectors.toList());

        if (approvers.isEmpty())
            return false;

        return true;
    }
}
