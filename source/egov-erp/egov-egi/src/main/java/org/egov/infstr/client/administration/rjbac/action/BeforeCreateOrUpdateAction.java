/*
 * @(#)BeforeCreateOrUpdateAction.java 3.0, 18 Jun, 2013 3:37:30 PM
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
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.commons.service.GenericCommonsService;
import org.egov.infstr.utils.StringUtils;

public class BeforeCreateOrUpdateAction extends EgovAction {
	private GenericCommonsService genericCommonsService;

	public void setGenericCommonsService(final GenericCommonsService genericCommonsService) {
		this.genericCommonsService = genericCommonsService;
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) {

		if (StringUtils.isBlank(req.getParameter("moduleId"))) {
			throw new EGOVRuntimeException("Could not get Module Id");
		}
		req.setAttribute("module", this.genericCommonsService.getModuleByID(Integer.valueOf(req.getParameter("moduleId"))));
		return mapping.findForward("success");
	}

}
