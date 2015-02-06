package org.egov.infra.utils;

import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    @Autowired
    private UserService userService;

    public User getCurrentUser() {
        if (isCurrentUserAuthenticated()) {
            if (getCurrentAuthentication().getPrincipal() instanceof String)
                return userService.getUserByUserName("anonymous");
            else 
                return userService.getUserByUserName(((UserDetails) getCurrentAuthentication().getPrincipal()).getUsername());
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
