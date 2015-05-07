package org.egov.infstr.security.spring;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.entity.enums.UserType;
import org.egov.infra.admin.master.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component("activationPreAuthenticationChecker")
public class ActivationPreAuthenticationChecker extends AccountStatusUserDetailsChecker {

	@Autowired
	private UserService userService;
	
	public void check(UserDetails userDetail) {
		User user = userService.getUserByUsername(userDetail.getUsername());
	
		if (user.getType().equals(UserType.CITIZEN) && !user.isActive()) {
			throw new DisabledException("OTP not activated");

		} 

		super.check(userDetail);
	}
}
