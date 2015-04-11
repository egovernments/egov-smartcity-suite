package org.egov.infra.security.utils;

import java.util.Optional;

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
            if (isCurrentUserAnonymous())
                return userService.getUserById(1l);//TODO should be replaced with anonymous user
            else 
                return userService.getUserById(((SecureUser) getCurrentAuthentication().get().getPrincipal()).getUserId());
        } else
            return userService.getUserById(1l);

    }

    public boolean isCurrentUserAuthenticated() {
    	Optional<Authentication> authentication = getCurrentAuthentication();
        return authentication.isPresent() ? authentication.get().isAuthenticated() : false;
    }
    
    public boolean isCurrentUserAnonymous() {
        return getCurrentAuthentication().get().getPrincipal() instanceof String;
    }
    
    public Optional<Authentication> getCurrentAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }

    public boolean hasRole(final String role) {
        return getCurrentAuthentication().get()
                .getAuthorities()
                .parallelStream()
                .map(grantedAuthority -> grantedAuthority.getAuthority().equals(role))
                .findFirst().get();
    }
}
