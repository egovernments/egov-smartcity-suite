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
package org.egov.lib.security.terminal.client;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.security.terminal.dao.UserValidateHibernateDAO;
import org.egov.lib.security.terminal.model.Location;
import org.egov.lib.security.terminal.model.UserValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserValidateAction extends DispatchAction {

	private UserService userService;
	private  UserValidateHibernateDAO userValidateHibernateDAO;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserValidateAction.class);

	public UserValidateAction(UserService userService, UserValidateHibernateDAO userValidateHibernateDAO) {
		this.userService = userService;
		this.userValidateHibernateDAO = userValidateHibernateDAO;
	}

	public ActionForward validateUser(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws ServletException {
		String target = "";
		boolean result = false;
		
		try {
			UserValidateForm userform = (UserValidateForm) form;
			
			String username = userform.getUsername();
			String password = userform.getPassword();
			String ipAddress = userform.getIpAddress();
			String locationId = userform.getLocationId();
			String counterId = userform.getCounterId();
			String loginType = userform.getLoginType();
			
			if (username == null || password == null || loginType == null)
				throw new EGOVRuntimeException("ActionForm value cannot be null");
			
			UserValidate obj = new UserValidate();
			obj.setUsername(username);
			obj.setPassword(password);
			obj.setIpAddress(ipAddress);
			
			if (loginType.equalsIgnoreCase("Location")) {
				if (locationId == null || counterId == null)
					throw new EGOVRuntimeException("LocationId or CounterId is null");
				
				obj.setLocationId(Integer.parseInt(locationId));
				obj.setCounterId(Integer.parseInt(counterId));
				
				Location location = userValidateHibernateDAO.getLocationByIP(ipAddress);
				if (location != null) {
					result = userValidateHibernateDAO.validateUserLocation(obj);
				} else {
					location = userValidateHibernateDAO.getTerminalByIP(ipAddress);
					if (location != null)
						result = userValidateHibernateDAO.validateUserTerminal(obj);
				}
				
				req.getSession().setAttribute("com.egov.user.locationId", locationId);
				req.getSession().setAttribute("com.egov.user.counterId", counterId);
				
			} else {
				result = userValidateHibernateDAO.validateUser(obj);
				// isActive = userValidateHibernateDAO.validateActiveUserForPeriod(obj.getUsername());
			}
			
			if (result == true) {
				target = "success";
				req.getSession().setAttribute("com.egov.user.LoginUserName", username);
				User user = userService.getUserByUsername(username);
				req.getSession().setAttribute("com.egov.user.LoginUserId", user.getId());
				req.getSession().setAttribute("org.egov.user.UserFirstName",user.getName());
			} else {
				target = "failure";
			}
			
			req.setAttribute("target", target);
		} catch (Exception ex) {
			target = "error";
			LOGGER.error("Exception occurred in UserValidateAction ", ex);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception occurred in UserValidateAction ", ex);
		}
		
		return mapping.findForward(target);
		
	}
	
}
