/*
 * @(#)BeforeCreateOrUpdateRoleAction.java 3.0, 18 Jun, 2013 3:38:56 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.administration.rjbac.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.commons.service.GenericCommonsService;
import org.egov.infstr.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BeforeCreateOrUpdateRoleAction extends EgovAction {
	private static final Logger logger = LoggerFactory.getLogger(BeforeCreateOrUpdateRoleAction.class);
	private GenericCommonsService genericCommonsService;
	@Autowired
	private RoleService roleService;

	public void setGenericCommonsService(final GenericCommonsService genericCommonsService) {
		this.genericCommonsService = genericCommonsService;
	}

	public void setRoleService (final RoleService roleService) {
		this.roleService = roleService;
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) {
		String target = "failure";
		try {
			final String moduleId = req.getParameter("moduleId");
			final String roleId = req.getParameter("roleId");

			if (StringUtils.isBlank(moduleId)) {
				throw new EGOVRuntimeException("Could not get Module Id");
			} else if (StringUtils.isBlank(roleId)) {
				throw new EGOVRuntimeException("Could not get Role Id");
			}
			this.saveToken(req);			
			req.setAttribute("module", this.genericCommonsService.getModuleByID(Integer.valueOf(moduleId)));
			req.setAttribute("role", this.roleService.getRoleById(Long.valueOf(roleId)));
			req.getSession().setAttribute("moduleId", Integer.valueOf(moduleId));
			target = "success";
		} catch (final Exception c) {
			logger.error("Error occurred while trying to initiate Role operation.",c);
			req.setAttribute("MESSAGE", "Could not process the request, due to an internal server error");
			throw new EGOVRuntimeException("Error occurred while trying to initiate Role operation.");

		}
		return mapping.findForward(target);
	}

}
