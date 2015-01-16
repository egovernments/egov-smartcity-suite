package org.egov.pgr.utils;

import org.egov.lib.rjbac.user.UserImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

	
	public UserImpl getCurrentUser() {
		/*Authentication authentication = getCurrentAuthentication();
        return (authentication == null || !authentication.isAuthenticated() || (authentication instanceof AnonymousAuthenticationToken)) 
        		? null : ((UserDetailsAdapter) authentication.getPrincipal()).getUser();*/
		return null;
	}
		
	public boolean isCurrentUserAuthenticated() {
		return getCurrentAuthentication().isAuthenticated();
	}
	
	public Authentication getCurrentAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public boolean hasRole(final String role) {
		for (GrantedAuthority grantedAuthority : this.getCurrentAuthentication().getAuthorities()) {
			if (grantedAuthority.getAuthority().equals(role)) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
}
