/*
 * @(#)BeforeViewRoleModule.java 3.0, 18 Jun, 2013 3:39:50 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.administration.rjbac.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infstr.client.EgovAction;
import org.springframework.beans.factory.annotation.Autowired;

public class BeforeViewRoleModule extends EgovAction {
        @Autowired
	private RoleService roleService;
	
	public void setRoleService(final RoleService roleManager) {
		this.roleService = roleManager;
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) {
		req.setAttribute("lstRoles", this.roleService.getAllRoles());
		return mapping.findForward("success");
	}
}
