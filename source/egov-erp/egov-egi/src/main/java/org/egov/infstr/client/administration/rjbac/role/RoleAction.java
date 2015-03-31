/*
 * @(#)RoleAction.java 3.0, 18 Jun, 2013 3:04:40 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.administration.rjbac.role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.commons.utils.EgovInfrastrUtil;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infstr.client.EgovAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoleAction extends EgovAction {
	private static final Logger LOG = LoggerFactory.getLogger(RoleAction.class);
	private final RoleService roleService;
	private final EgovInfrastrUtil egovInfrastrUtil;

	public RoleAction(RoleService roleService, EgovInfrastrUtil egovInfrastrUtil) {
		this.roleService = roleService;
		this.egovInfrastrUtil = egovInfrastrUtil;
	}

	/**
	 * This method is used to get all the top boundaries and set the list in session Calls the setup method in 
	 * EgovAction class that sets a list of all the departments in the session
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest req
	 * @param HttpServletResponse res
	 * @return ActionForward
	 **/
	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		
		if ( !isTokenValid(req) ) {
		    return mapping.findForward("error");
		}
		
		final RoleForm roleForm = (RoleForm) form;
		String target = "";
		final HttpSession session = req.getSession();
		if (req.getParameter("bool").equals("CREATE")) {
			final Role role = new Role();
			role.setName(roleForm.getRoleName());
			role.setDescription(roleForm.getRoleDesc());
			/*role.setLocalName(roleForm.getRoleNameLocal());
			role.setLocalDescription(roleForm.getRoleDescLocal());*/

			try {
				roleService.createRole(role);
				target = "success";
				roleForm.reset(mapping, req);
				req.setAttribute("MESSAGE", "Role successfully created");
				egovInfrastrUtil.resetCache();
			} catch (final Exception c) {
				LOG.error("Error occurred while creating Role",c);
				req.setAttribute("MESSAGE", "Role could not be created due to an internal error!");
				throw new EGOVRuntimeException("Error occurred while creating Role");
			}
		} else if (req.getParameter("bool").equals("UPDATE")) {

			final Role role  = roleService.getRoleById((Long)session.getAttribute("roleIdValue"));
			role.setName(roleForm.getRoleName());
			role.setDescription(roleForm.getRoleDesc());
			/*role.setLocalName(roleForm.getRoleNameLocal());
			role.setLocalDescription(roleForm.getRoleDescLocal());*/

			try {
				roleService.update(role);
				target = "success";
				roleForm.reset(mapping, req);
				req.setAttribute("MESSAGE", "Role successfully modified");
				egovInfrastrUtil.resetCache();
			} catch (final Exception c) {
				LOG.error("Error occurred while updating Role",c);
				req.setAttribute("MESSAGE", "Role could not be modified due to an internal error!");
				throw new EGOVRuntimeException("Error occurred while updating Role");
			}
		} else if (req.getParameter("bool").equals("DELETE")) {
			
			final Role role  = roleService.getRoleById(roleForm.getRoleId().longValue());

			try {
				roleService.remove(role);
				target = "success";
				roleForm.reset(mapping, req);
				req.setAttribute("MESSAGE", "Role successfully deleted");
				egovInfrastrUtil.resetCache();
			} catch (final Exception c) {
				LOG.error("Error occurred while deleting Role", c);
				req.setAttribute("MESSAGE", "Role could not be deleted due to an internal error!");
				throw new EGOVRuntimeException("Error occurred while deleting Role");
			}
		}
		if (target.equals("success")) {
			 resetToken(req);
		}
		return mapping.findForward(target);
	}

}
