/*
 * @(#)SetupRoleAction.java 3.0, 18 Jun, 2013 3:10:06 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.administration.rjbac.role;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infstr.client.EgovAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SetupRoleAction extends EgovAction {
	private static final Logger LOG = LoggerFactory.getLogger(SetupRoleAction.class);
	@Autowired
	private RoleService roleService;
	/**
	 * This method is used to get all the top boundries and set the list in session Calls the setup 
	 * method in EgovAction class that sets a list of all the departments in the session
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest req
	 * @param HttpServletResponse res
	 * @return ActionForward
	 **/
	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		final RoleForm roleForm = (RoleForm) form;
		String target = "";
		this.setup(req);
		this.saveToken(req);
		List<Role> roleList = new ArrayList<Role>();
		if (req.getParameter("bool").equals("VIEW")) {

			try {
				if (roleForm.getRoleId() != null) {
					final Long roleid = roleForm.getRoleId().longValue();
					final Role role = roleService.getRoleById(roleid);
					roleForm.setRoleName(role.getName());
					roleForm.setRoleDesc(role.getDescription());
					roleForm.setRoleNameLocal(role.getLocalName());
					roleForm.setRoleDescLocal(role.getLocalDescription());
					target = "viewRole";
				} else {
					roleList = roleService.getAllRoles();
					target = "success";
				}
			} catch (final Exception c) {
				LOG.error("Error occurred while try to view Role.",c);
				req.setAttribute("MESSAGE", "Could not process Role view due to an internal server error.");
				throw new EGOVRuntimeException("Error occurred while try to view Role." );

			}

		} else if (req.getParameter("bool").equals("UPDATE")) {

			try {
				final HttpSession session = req.getSession();
				if (roleForm.getRoleId() != null) {
					final Long roleid = roleForm.getRoleId().longValue();
					session.setAttribute("roleIdValue", roleid);
					final Role role = roleService.getRoleById(roleid);
					roleForm.setRoleName(role.getName());
					roleForm.setRoleDesc(role.getDescription());
					roleForm.setRoleNameLocal(role.getLocalName());
					roleForm.setRoleDescLocal(role.getLocalDescription());
					target = "updateRole";
				} else {
					roleList = roleService.getAllRoles();
					target = "success";
				}
			} catch (final Exception c) {
				LOG.error("Error occurred while try to update Role.",c);
				req.setAttribute("MESSAGE", "Could not process Role update due to an internal server error.");
				throw new EGOVRuntimeException("Error occurred while try to update Role.");

			}

		}

		req.getSession().setAttribute("roleList", roleList);
		return mapping.findForward(target);
	}
}
