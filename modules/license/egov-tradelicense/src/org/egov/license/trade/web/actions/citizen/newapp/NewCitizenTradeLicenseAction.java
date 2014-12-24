/*
 * @(#)NewCitizenTradeLicenseAction.java 3.0, 25 Jul, 2013 4:57:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.trade.web.actions.citizen.newapp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.license.trade.web.actions.newtradelicense.NewTradeLicenseAction;
import org.egov.license.utils.Constants;
import org.egov.web.annotation.ValidationErrorPage;

@ParentPackage("egov")
public class NewCitizenTradeLicenseAction extends NewTradeLicenseAction implements ServletRequestAware {

	private static final long serialVersionUID = 1L;
	private HttpSession session = null;
	private HttpServletRequest request;
	private Integer userId;

	@Override
	@SkipValidation
	public void prepareNewForm() {
		super.prepareNewForm();
		setUserDetails();
	}

	@Override
	@ValidationErrorPage(Constants.NEW)
	public String create() {
		setUserDetails();
		return super.create();
	}

	@Override
	public void setServletRequest(final HttpServletRequest arg0) {
		this.request = arg0;
	}

	private void setUserDetails() {
		this.session = this.request.getSession();
		final String userName = (String) this.session.getAttribute("com.egov.user.LoginUserName");
		final User user;
		if (userName != null) {
			user = new UserDAO().getUserByUserName(userName);
		} else {
			user = new UserDAO().getUserByUserName(Constants.CITIZENUSER);
			this.session.setAttribute("com.egov.user.LoginUserName", user.getUserName());
		}
		this.userId = user.getId();
		EGOVThreadLocals.setUserId(this.userId.toString());
	}
}
