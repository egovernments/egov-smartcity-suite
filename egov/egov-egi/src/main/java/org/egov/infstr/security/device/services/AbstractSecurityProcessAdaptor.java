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
package org.egov.infstr.security.device.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.security.utils.CryptoHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class AbstractSecurityProcessAdaptor.
 * This is base class for SecurityProcess, Client specific class needs to sub class this<br/>
 * All global logic to the security process has to be done at this place.
 */
public abstract class AbstractSecurityProcessAdaptor implements SecurityProcessAdaptor {
	
	protected static final Logger LOG = LoggerFactory.getLogger(AbstractSecurityProcessAdaptor.class);
	
	protected UserService userService;
	protected String contentType;
	
	/**
	 * Sets the response content type.
	 * @param contentType the new content type
	 */
	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}
	
	/**
	 * Sets the user manager.
	 * @param userManager the new user manager
	 */
	public void setUserService(final UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getContentType() {
		return this.contentType;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doBeforeFilterChain(final HttpServletRequest request, final HttpServletResponse response) {
	}
	
	/**
	 * Authenticate login request, checks user is valid.
	 * @param userName the user name
	 * @param userPwd the user pwd
	 * @return the user
	 */
	protected User authenticateLogin(final String userName, final String userPwd) {
		User user = null;
		if (userName != null && userPwd != null) {
			user = this.userService.getUserByUsername(userName);
			if (user == null || !userPwd.equals(CryptoHelper.decrypt(user.getPassword())) || !user.isActive() ) {
				user = null;
			} else {
				EGOVThreadLocals.setUserId(user.getId());
			}
		}
		return user;
	}	
}
