package org.egov.infra.security.utils;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.security.authentication.SecureUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtils {

    @Autowired
    private UserService userService;

    public User getCurrentUser() {
        if (isCurrentUserAuthenticated()) {
            if (getCurrentAuthentication().getPrincipal() instanceof String)
                return userService.getUserById(1l);//TODO should be replaced with anonymous user
            else 
                return userService.getUserById(((SecureUser) getCurrentAuthentication().getPrincipal()).getUserId());
        } else
            return null;

    }

    public boolean isCurrentUserAuthenticated() {
        return getCurrentAuthentication().isAuthenticated();
    }

    public Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public boolean hasRole(final String role) {
        return getCurrentAuthentication()
                .getAuthorities()
                .parallelStream()
                .map(grantedAuthority -> grantedAuthority.getAuthority().equals(role))
                .findFirst().get();
    }
}
