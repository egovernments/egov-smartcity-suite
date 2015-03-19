/*
 * @(#)SetupUpdateUserAction.java 3.0, 18 Jun, 2013 2:34:15 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.administration.rjbac.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.security.utils.CryptoHelper;
import org.egov.lib.rjbac.role.ejb.api.RoleService;
import org.egov.lib.rjbac.role.ejb.server.RoleServiceImpl;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.lib.rjbac.user.ejb.server.UserServiceImpl;

public class SetupUpdateUserAction extends EgovAction {

	private static final Logger logger = LoggerFactory.getLogger(SetupUpdateUserAction.class);
	private final UserService userService = new UserServiceImpl(null, null);
	private final RoleService roleService = new RoleServiceImpl();

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
			usr = this.userService.getUserByUserName(username);
			userForm.setId(usr.getId());
			userForm.setFirstName(usr.getFirstName());
			if (usr.getMiddleName() != "") {
				userForm.setMiddleName(usr.getMiddleName());
			}
			if (usr.getLastName() != "") {
				userForm.setLastName(usr.getLastName());
			}
			if (usr.getSalutation() != "") {
				userForm.setSalutation(usr.getSalutation());
			}
			userForm.setPassword(CryptoHelper.decrypt(usr.getPassword()));
			//userForm.setPwdReminder(usr.getPwdReminder());
			final Date fdate = usr.getFromDate();
			final Date tdate = usr.getToDate();
			final Date dob = usr.getDob();
			final String fromdate = fdate.toString();
			final String fromDate = formatter1.format(formatter.parse(fromdate));
			userForm.setFromDate(fromDate);
			if (tdate != null) {
				final String todate = tdate.toString();
				final String toDate = formatter1.format(formatter.parse(todate));
				userForm.setToDate(toDate);
			}
			if (dob != null) {
				final String dob1 = dob.toString();
				final String Dob = formatter1.format(formatter.parse(dob1));
				userForm.setDob(Dob);
			}
			final int isactive = usr.getIsActive();
			req.getSession().setAttribute("isactive", isactive);
			final Set roleObj1 = this.userService.getAllRolesForUser(username);
			if (roleObj1 != null && !roleObj1.isEmpty()) {
				req.setAttribute("roleObj1", roleObj1);
			}
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
