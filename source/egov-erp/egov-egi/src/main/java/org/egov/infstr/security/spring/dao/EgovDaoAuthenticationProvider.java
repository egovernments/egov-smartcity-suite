/*
 * @(#)EgovDaoAuthenticationProvider.java 3.0, 18 Jun, 2013 4:13:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security.spring.dao;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.infstr.security.utils.CryptoHelper;
import org.egov.infstr.security.utils.SecurityConstants;
import org.egov.infstr.utils.StringUtils;
import org.egov.lib.security.terminal.dao.UserValidateDAO;
import org.egov.lib.security.terminal.model.Location;
import org.egov.lib.security.terminal.model.UserValidate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/*
 * This class performs the authentication for the user. If location based login is set up, 
 * it reads the locationId and counterId to validate if user is setup to login from terminal 
 * @author: Sahina Bose
 */
public class EgovDaoAuthenticationProvider extends DaoAuthenticationProvider {

	private static final Logger LOG = LoggerFactory.getLogger(EgovDaoAuthenticationProvider.class);
	private UserValidateDAO userValidateDao;

	public void setUserValidateDao(UserValidateDAO userValidateDao) {
		this.userValidateDao = userValidateDao;
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		final HashMap<String, String> authenticationCredentials = (HashMap<String, String>) authentication.getCredentials();
		if (authentication.getCredentials() == null) {
			throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}
		final String presentedPassword = authenticationCredentials.get(SecurityConstants.PWD_FIELD);
		if (!CryptoHelper.encrypt(presentedPassword).equals(userDetails.getPassword())) {
			throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}
		
		if (!this.userValidateDao.validateActiveUserForPeriod(userDetails.getUsername())) {
			throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "This user is not valid or inactive"));
		}
		final String loginType = authenticationCredentials.get(SecurityConstants.LOGINTYPE);
		if (loginType != null && "Location".equalsIgnoreCase(loginType)) {
			final String locationId = authenticationCredentials.get(SecurityConstants.LOCATION_FIELD);
			final String counterId = authenticationCredentials.get(SecurityConstants.COUNTER_FIELD);
			final String ipAddress = authenticationCredentials.get(SecurityConstants.IPADDR_FIELD);
			if (locationId == null || counterId == null) {
				throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.noLocation", "Location or counter is not specified"));
			}
			final UserValidate obj = new UserValidate();
			obj.setUsername(userDetails.getUsername());
			obj.setPassword(presentedPassword);
			obj.setIpAddress(ipAddress);
			obj.setLocationId(Integer.parseInt(locationId));
			obj.setCounterId(Integer.parseInt(counterId));
			Location location = this.userValidateDao.getLocationByIP(ipAddress);
			if (location != null) {
				if (!this.userValidateDao.validateUserLocation(obj)) {
					LOG.error("No mapping for location: {} from {} for user: {}",StringUtils.toStringArray(location.getName(),ipAddress, userDetails.getUsername()));
					throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "This user cannot login from this terminal"));
				}
			} else {
				location = this.userValidateDao.getTerminalByIP(ipAddress);
				if (location == null) {
					LOG.error("No location or terminal for ip address : {}", ipAddress);
					throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials",
							"There is no Location or terminal mapped for the given IP address"));
				}
				if (location != null && !this.userValidateDao.validateUserTerminal(obj)) {
					LOG.error("No mapping for terminal : {} from {} for user : {}",StringUtils.toStringArray(location.getName(),ipAddress, userDetails.getUsername()));
					throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "This user cannot login from this terminal"));
				}
			}
		}
	}
}
