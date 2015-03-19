/*
 * @(#)ImageRendererAction.java 3.0, 14 Jun, 2013 1:41:52 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.actions.common;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.UserImpl;
import org.egov.infstr.security.utils.CryptoHelper;
import org.egov.web.actions.BaseFormAction;

@ParentPackage("egov")
public class ImageRendererAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	private transient Long id;

	@SkipValidation
	public void getUserSignature() throws IOException {
		final HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("image/jpeg");
		this.persistenceService.setType(UserImpl.class);
		final UserImpl user = (UserImpl) this.persistenceService.findById(this.id.intValue(), false);
		if (user.getUserSignature() != null) {
			response.getOutputStream().write(CryptoHelper.decrypt(user.getUserSignature().getSignature(), CryptoHelper.decrypt(user.getPwd())));
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	public void setId(final Long id) {
		this.id = id;
	}

	@Override
	public Object getModel() {
		return null;
	}
}
