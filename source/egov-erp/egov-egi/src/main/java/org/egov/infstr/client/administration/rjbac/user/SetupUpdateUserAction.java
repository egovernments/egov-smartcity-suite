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
package org.egov.infstr.client.administration.rjbac.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.security.utils.CryptoHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SetupUpdateUserAction extends EgovAction {

	private static final Logger logger = LoggerFactory.getLogger(SetupUpdateUserAction.class);
	private final UserService userService = new UserService();
	@Autowired    
	private RoleService roleService;

	/**
	 * This method is used to get all the top boundries and set the list in session Calls the setup method in EgovAction class that sets a list of all the departments in the session
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest req
	 * @param HttpServletResponse res
	 * @return ActionForward
	 */
	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		List roleList = new ArrayList();
		this.setup(req);
		final UserForm userForm = (UserForm) form;
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		final SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
		String target = "";
		User usr = null;
		try {
			final String username = userForm.getUserName();
			usr = this.userService.getUserByUsername(username);
			userForm.setId(usr.getId());
			userForm.setFirstName(usr.getName());
			/*if (usr.getMiddleName() != "") {
				userForm.setMiddleName(usr.getMiddleName());
			}
			if (usr.getLastName() != "") {
				userForm.setLastName(usr.getLastName());
			}*/
			if (usr.getSalutation() != "") {
				userForm.setSalutation(usr.getSalutation());
			}
			userForm.setPassword(CryptoHelper.decrypt(usr.getPassword()));
			//userForm.setPwdReminder(usr.getPwdReminder());
			//final Date fdate = usr.getFromDate();
			//final Date tdate = usr.getToDate();
			final Date dob = usr.getDob();
			/*final String fromdate = fdate.toString();
			final String fromDate = formatter1.format(formatter.parse(fromdate));
			userForm.setFromDate(fromDate);
			if (tdate != null) {
				final String todate = tdate.toString();
				final String toDate = formatter1.format(formatter.parse(todate));
				userForm.setToDate(toDate);
			}*/
			if (dob != null) {
				final String dob1 = dob.toString();
				final String Dob = formatter1.format(formatter.parse(dob1));
				userForm.setDob(Dob);
			}
			final boolean isactive = usr.isActive();
			req.getSession().setAttribute("isactive", isactive);
			//final Set roleObj1 = this.userService.getAllRolesForUser(username);
			/*if (roleObj1 != null && !roleObj1.isEmpty()) {
				req.setAttribute("roleObj1", roleObj1);
			}*/
			roleList = this.roleService.getAllRoles();
			target = "success";
		} catch (final EGOVRuntimeException e) {
			logger.error("Error occurred while getting user data", e);
			target = "error";
			req.setAttribute("MESSAGE", "Error before UserAction !!");
			throw new EGOVRuntimeException("Error occurred while getting user data", e);
		} catch (final Exception c) {
			logger.error("Error occurred while getting user data", c);
			target = "error";
			req.setAttribute("MESSAGE", "Error before UserAction !!");
			throw new EGOVRuntimeException("Error occurred while getting user data", c);
		}
		req.getSession().setAttribute("RoleList", roleList);
		req.getSession().setAttribute("userDetail", usr);
		return mapping.findForward(target);
	}
}
