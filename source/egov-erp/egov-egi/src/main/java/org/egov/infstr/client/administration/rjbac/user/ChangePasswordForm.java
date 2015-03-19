/*
 * @(#)ChangePasswordForm.java 3.0, 18 Jun, 2013 2:29:49 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.administration.rjbac.user;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorActionForm;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.security.utils.CryptoHelper;
import org.egov.infstr.security.utils.ValidatorUtils;
import org.egov.lib.rjbac.user.dao.UserDAO;

public class ChangePasswordForm extends ValidatorActionForm {

	private static final long serialVersionUID = 1L;
	private String oldPwd;
	private String pwd;
	private String pwdReminder;

	public String getOldPwd() {
		return this.oldPwd;
	}

	public void setOldPwd(final String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public String getPwd() {
		return this.pwd;
	}

	public void setPwd(final String pwd) {
		this.pwd = pwd;
	}

	public String getPwdReminder() {
		return this.pwdReminder;
	}

	public void setPwdReminder(final String pwdReminder) {
		this.pwdReminder = pwdReminder;
	}

	/**
	 * checks for null values in ActionForm fields and also business validations like old password incorrect,password mismatch etc.,
	 */
	@Override
	public ActionErrors validate(final ActionMapping mapping, final HttpServletRequest request) {
		final ActionErrors errors = new ActionErrors();
		if (StringUtils.isBlank(this.getOldPwd())) {
			errors.add("message", new ActionMessage("oldPwd.null"));
		}
		if (StringUtils.isBlank(this.getPwd())) {
			errors.add("message", new ActionMessage("pwd.null"));
		}
		if (StringUtils.isBlank(this.getPwdReminder())) {
			errors.add("message", new ActionMessage("pwdReminder.null"));
		}

		if (StringUtils.isNotBlank(this.getPwd()) && StringUtils.isNotBlank(this.getPwdReminder())) {
			if (!this.getPwd().trim().equals(this.getPwdReminder().trim())) {
				errors.add("message", new ActionMessage("pwd.mismatch"));
			}
		}
		if (errors.isEmpty()) {
			if (!ValidatorUtils.isValidPassword(this.getPwd(), true)) {
				errors.add("message", new ActionMessage("new.pwd.invalid"));
			} else if (StringUtils.isNotBlank(this.getOldPwd())) {
				final UserDAO userDao = new UserDAO();
				final User user = userDao.getUserByUserName((String) request.getSession().getAttribute("LAST_USER_NAME_PWD_CHANGE"));
				final String decryptPwd = CryptoHelper.decrypt(user.getPwd().trim());
				if (!this.getOldPwd().trim().equals(decryptPwd)) {
					errors.add("message", new ActionMessage("old.pwd.Incorrect"));
				} else if (this.getPwd().trim().equals(decryptPwd)) {
					errors.add("message", new ActionMessage("pwd.old.new.same"));
				}
			}
		}
		return errors;
	}
}
