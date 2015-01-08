/*
 * @(#)BeforeUpdateModuleAction.java 3.0, 18 Jun, 2013 3:13:41 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.administration.rjbac.module;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.commons.service.GenericCommonsService;

public class BeforeUpdateModuleAction extends EgovAction {
	private static final Logger LOG = LoggerFactory.getLogger(BeforeUpdateModuleAction.class);
	private GenericCommonsService genericCommonsService;

	public void setGenericCommonsService(final GenericCommonsService genericCommonsService) {
		this.genericCommonsService = genericCommonsService;
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		String target = "failure";
		try {
			if (org.apache.commons.lang.StringUtils.isBlank(req.getParameter("moduleId"))) {
				throw new EGOVRuntimeException("Module Id not found.");
			}
			req.setAttribute("module", genericCommonsService.getModuleByID(Integer.valueOf(req.getParameter("moduleId"))));
			target = "success";
			this.saveToken(req);
		} catch (final Exception c) {
			LOG.error("Error occurred while try to process Module update.",c);
			req.setAttribute("MESSAGE", "Could not process this request due to an internal server error.");
			throw new EGOVRuntimeException("Error occurred while try to process Module update.");

		}
		return mapping.findForward(target);
	}

}
