/*
 * @(#)UserDetailsImpl.java 3.0, 18 Jun, 2013 4:11:10 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security.spring;

import java.util.ArrayList;
import java.util.List;

import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Providing User details to security framework.
 * 
 * @author sahina bose
 */
public class UserDetailsImpl implements UserDetailsService {

	private UserService userService;

	public void setUserService(final UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final User usr = this.userService.getUserByUserName(username);
		if (usr == null) {
			return null;
		}
		final List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		for (final Role role : usr.getRoles()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));

		}
		final UserDetails userDetails = new org.springframework.security.core.userdetails.User(usr.getUsername(), usr.getPassword(), usr.isActive(), // enabled
		                usr.isActive(), // accountNonExpired
				usr.isActive(), // credentialsNonExpired
				usr.isActive(), // accountNonLocked
				grantedAuthorities);

		return userDetails;
	}

}
