/*
 * @(#)UpdateUserAction.java 3.0, 18 Jun, 2013 2:35:43 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.administration.rjbac.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.client.EgovActionForm;
import org.egov.infstr.client.delegate.UserDelegate;
import org.egov.infstr.security.utils.CryptoHelper;

public class UpdateUserAction extends EgovAction {
	
	private final static Logger LOG = LoggerFactory.getLogger(UpdateUserAction.class);
	private UserDelegate userDelegate;
	
	/**
	 * This method forwards the control to the jusrsidiction page. If the user is to be given more
	 * levels of jurisdictions then the request is sent to the same jursidiction page
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest req
	 * @param HttpServletResponse res
	 * @return ActionForward
	 */
	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		String target = "";
		String message = "";
		List jurisdcnList = new ArrayList();
		final Map jurisdcnMap = new HashMap();
		final DeleteUserForm updateForm = (DeleteUserForm) form;
		final javax.servlet.http.HttpSession session = req.getSession();
		final String strn = ((req.getParameter("bool") == null) || req.getParameter("bool").equals("null") ? "" : req.getParameter("bool"));
		if (strn.equals("UPDATE")) {
			User usr = (User) session.getAttribute("USER");
			final Map jurMap = (Map) session.getAttribute("jursidcnMap");
			int k = 1;
			for (int i = 0; i < jurMap.size(); i++) {
				final List bndryIdsList1 = new ArrayList();
				final String bndryTypeStr = "bndryType" + k;
				final String bndryTyp = req.getParameter(bndryTypeStr);
				final String hBndValues = "hBndValues" + k;
				final String bndValues = req.getParameter(hBndValues);
				final StringTokenizer bndValuesStrTok = new StringTokenizer(bndValues, ",");
				while (bndValuesStrTok.hasMoreTokens()) {
					final String bndId = bndValuesStrTok.nextToken();
					bndryIdsList1.add(bndId);
				}
				if (!bndryIdsList1.isEmpty()) {
					jurisdcnMap.put(bndryTyp, bndryIdsList1);
				}
				k++;
			}
			jurisdcnList.add(jurisdcnMap);
			updateForm.populate(usr, EgovActionForm.TO_OBJECT);
			final String encpassword = CryptoHelper.encrypt(updateForm.getPwd());
			usr.setPassword(encpassword);
			//usr.setPwdReminder(updateForm.getPwdReminder());
			session.setAttribute("user", usr);
			session.setAttribute("deptid", updateForm.getDepartmentId());
			session.setAttribute("roleid", updateForm.getRoleId());
			if ((jurisdcnList == null) || jurisdcnList.isEmpty()) {
				jurisdcnList = new ArrayList();
			}
			try {
				final String hierarachyTypeName = (String) session.getAttribute("hrchyTypeName");
				//this.userDelegate.updateUser(usr, updateForm.getDepartmentId(), updateForm.getRoleId(), jurisdcnList, hierarachyTypeName);
				target = "success";
				message = "User has been Updated Successfully!!";
				req.setAttribute("MESSAGE", message);
				updateForm.reset(mapping, req);
				session.removeAttribute("jurisdcnList");
				session.removeAttribute("USER");
			} catch (final EGOVRuntimeException ere) {
				target = "error";
				LOG.error( "Error occurred while updating User ", ere);
				throw new EGOVRuntimeException("Error occurred while updating User ", ere);
			}
		}
		return mapping.findForward(target);
	}

	public void setUserDelegate(UserDelegate userDelegate) {
		this.userDelegate = userDelegate;
	}
}
