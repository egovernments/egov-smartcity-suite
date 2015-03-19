/*
 * @(#)ChangePasswordAction.java 3.0, 18 Jun, 2013 2:29:37 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.administration.rjbac.user;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.security.utils.CryptoHelper;
import org.egov.infstr.security.utils.ValidatorUtils;
import org.egov.lib.rjbac.user.dao.UserDAO;

public class ChangePasswordAction extends Action {

	private UserDAO userDao;

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		// Check its an Ajax request
		if (Boolean.valueOf(req.getParameter("AJAX"))) {
			res.setContentType("text/plain");
			final PrintWriter out = res.getWriter();
			out.println(String.valueOf(ValidatorUtils.isValidPassword(req.getParameter("pwd"), true)));
			out.flush();
			return null;
		} else {
			final ChangePasswordForm changePasswordForm = (ChangePasswordForm) form;
			final User user = this.getUserDao().getUserByUserName((String)req.getSession().getAttribute("LAST_USER_NAME_PWD_CHANGE"));
			if (user.getUserSignature() != null) {
				final byte [] currentSignature = CryptoHelper.decrypt(user.getUserSignature().getSignature(),CryptoHelper.decrypt(user.getPwd()));
				user.getUserSignature().setSignature(CryptoHelper.encrypt(currentSignature,changePasswordForm.getPwd().trim()));
			}
			user.setPwd(changePasswordForm.getPwd().trim());
			user.setPwdReminder(changePasswordForm.getPwdReminder().trim());
			user.setPwdModifiedDate(new Date());
			this.getUserDao().createOrUpdateUserWithPwdEncryption(user);
			req.setAttribute("MESSAGE", "Password successfully modified.");				
			return mapping.findForward("success");
		}

	}

	public UserDAO getUserDao() {
		return this.userDao;
	}

	public void setUserDao(final UserDAO userDao) {
		this.userDao = userDao;
	}

}
