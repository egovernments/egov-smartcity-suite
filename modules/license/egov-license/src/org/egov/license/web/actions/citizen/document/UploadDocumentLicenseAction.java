/*
 * @(#)UploadDocumentLicenseAction.java 3.0, 29 Jul, 2013 1:24:26 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.web.actions.citizen.document;

import javax.jcr.RepositoryException;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.license.utils.Constants;
import org.egov.web.actions.docmgmt.BasicDocumentManagerAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.web.annotation.ValidationErrorPageExt;

@ParentPackage("egov")
public class UploadDocumentLicenseAction extends BasicDocumentManagerAction {
	private static final long serialVersionUID = 1L;
	private Integer userId;

	@Override
	public String execute() {
		super.execute();
		setUserDetails();
		return SUCCESS;
	}

	@Override
	@ValidationErrorPage(SUCCESS)
	public String addDocument() throws IllegalAccessException, RepositoryException, RuntimeException {
		setUserDetails();
		return super.addDocument();
	}

	@Override
	public String editDocument() throws RuntimeException {
		setUserDetails();
		return super.editDocument();
	}

	@Override
	@ValidationErrorPage(SUCCESS)
	public String viewDocument() {
		setUserDetails();
		return super.viewDocument();
	}

	@Override
	@ValidationErrorPageExt(action = SUCCESS, makeCall = true, toMethod = "editDocument")
	public String updateDocument() throws IllegalAccessException, RepositoryException, RuntimeException {
		setUserDetails();
		return super.updateDocument();
	}

	private void setUserDetails() {
		String userId = null; 
		try {
			userId= EGOVThreadLocals.getUserId();
		} catch (Exception e) {
			//Expected exception if citizen used, so ignoring
		}
		final User user;
		if (userId != null) {
			user = new UserDAO().getUserByID(Integer.valueOf(userId));
		} else {
			user = new UserDAO().getUserByUserName(Constants.CITIZENUSER);
			ServletActionContext.getRequest().getSession(false).setAttribute("com.egov.user.LoginUserName", user.getFirstName());
		}
		this.userId = user.getId();
		EGOVThreadLocals.setUserId(this.userId.toString());
	}
}
