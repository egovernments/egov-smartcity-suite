/*
 * @(#)CitizenIndexAction.java 3.0, 29 Jul, 2013 1:24:25 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.web.actions.citizen;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.license.utils.Constants;

import com.opensymphony.xwork2.Action;

public class CitizenIndexAction implements Action, ServletRequestAware {

	private HttpSession session = null;
	private HttpServletRequest request;
	private Integer userId;

	@Override
	public String execute() {
		this.session = this.request.getSession();
		final User user = new UserDAO().getUserByUserName(Constants.CITIZENUSER);
		this.session.setAttribute("com.egov.user.LoginUserName", user.getUserName());
		this.userId = user.getId();
		EGOVThreadLocals.setUserId(this.userId.toString());
		return SUCCESS;
	}

	@Override
	public void setServletRequest(final HttpServletRequest arg0) {
		this.request = arg0;
	}

}
