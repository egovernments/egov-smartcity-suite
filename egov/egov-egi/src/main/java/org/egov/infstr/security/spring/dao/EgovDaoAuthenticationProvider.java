/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infstr.security.spring.dao;

import java.util.HashMap;

import org.egov.infstr.security.utils.SecurityConstants;
import org.egov.infstr.utils.StringUtils;
import org.egov.lib.security.terminal.dao.UserValidateDAO;
import org.egov.lib.security.terminal.model.Location;
import org.egov.lib.security.terminal.model.UserValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * This class performs the authentication for the user. If location based login is set up, 
 * it reads the locationId and counterId to validate if user is setup to login from terminal 
 * @author: Sahina Bose
 */
public class EgovDaoAuthenticationProvider extends DaoAuthenticationProvider {

	private static final Logger LOG = LoggerFactory.getLogger(EgovDaoAuthenticationProvider.class);
	private UserValidateDAO userValidateDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public void setUserValidateDao(UserValidateDAO userValidateDao) {
		this.userValidateDao = userValidateDao;
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		/*final HashMap<String, String> authenticationCredentials = (HashMap<String, String>) authentication.getCredentials();
		if (authentication.getCredentials() == null) {
			throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}
		final String presentedPassword = authenticationCredentials.get(SecurityConstants.PWD_FIELD);
		if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
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
		}*/
	}
}
