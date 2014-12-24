/*
 * @(#)AjaxDocumentDownloadAction.java 3.0, 29 Jul, 2013 1:24:27 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.web.actions.citizen.document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.license.utils.Constants;
import org.egov.web.actions.docmgmt.AjaxFileDownloadAction;

public class AjaxDocumentDownloadAction extends AjaxFileDownloadAction implements ServletRequestAware {

	private static final long serialVersionUID = 1L;

	private HttpSession session = null;
	private HttpServletRequest request;
	private Integer userId;

	@Override
	public String execute() {
		super.execute();
		setUserDetails();
		return SUCCESS;
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
