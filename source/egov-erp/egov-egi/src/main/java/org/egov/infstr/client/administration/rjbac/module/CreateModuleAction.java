/*
 * @(#)CreateModuleAction.java 3.0, 18 Jun, 2013 3:14:39 PM
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
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.service.GenericCommonsService;
import org.egov.infstr.utils.StringUtils;

public class CreateModuleAction extends EgovAction {
	private static final Logger LOG = LoggerFactory.getLogger(CreateModuleAction.class);
	private GenericCommonsService genericCommonsService;

	public void setGenericCommonsService(final GenericCommonsService genericCommonsService) {
		this.genericCommonsService = genericCommonsService;
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		String target = "failure";

		try {

			final ModuleForm moduleForm = (ModuleForm) form;
			final Module module = new Module();
			if (StringUtils.isNotBlank(moduleForm.getModuleName())) {
				module.setModuleName(moduleForm.getModuleName());
			}
			if (StringUtils.isNotBlank(moduleForm.getModuleNameLocal())) {
				module.setModuleNameLocal(moduleForm.getModuleNameLocal());
			}
			if (StringUtils.isNotBlank(moduleForm.getModuleDescription())) {
				module.setModuleDescription(moduleForm.getModuleDescription());
			}
			if (StringUtils.isNotBlank(moduleForm.getParentModuleId())) {
				final Module parentModule = genericCommonsService.getModuleByID(Integer.valueOf(moduleForm.getParentModuleId()));
				if (parentModule != null) {
					module.setParent(parentModule);
				}
			}

			module.setIsEnabled(moduleForm.getIsEnabled());
			if (StringUtils.isNotBlank(moduleForm.getBaseURL())) {
				module.setBaseUrl(moduleForm.getBaseURL());
			}
			if (StringUtils.isNotBlank(moduleForm.getOrderNumber())) {
				module.setOrderNumber(Integer.valueOf(moduleForm.getOrderNumber()));
			}
			genericCommonsService.createModule(module);
			req.setAttribute("module", module);
			target = "success";
			req.setAttribute("MESSAGE", "Module successfully created.");
			this.resetToken(req);

		} catch (final Exception c) {
			LOG.error("Error occurred while trying to create Module",c);
			req.setAttribute("MESSAGE", "Request cannot be processed due to an internal server error.");
			throw new EGOVRuntimeException("Error occurred while trying to create Module");
		}

		return mapping.findForward(target);
	}

}
