/*
 * @(#)BeforeUserJurisdictionAction.java 3.0, 18 Jun, 2013 2:29:26 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.administration.rjbac.user;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.lib.rjbac.user.ejb.server.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Set;

public class BeforeUserJurisdictionAction extends Action {

	private static final Logger logger = LoggerFactory.getLogger(BeforeUserJurisdictionAction.class);
	private final UserService userService = new UserServiceImpl(null, null);

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		String target = "";
		try {
			String userIdStr = "";
			if (req.getParameter("userid") != null) {
				userIdStr = req.getParameter("userid");
				final User user = this.userService.getUserByID(Long.valueOf(userIdStr));
				final Set jurObj1 = this.userService.getAllJurisdictionsForUser(Long.valueOf(userIdStr));
				if (jurObj1 != null) {
					req.setAttribute("jurObj1", jurObj1);
				}
				req.setAttribute("user", user);
			}
			target = "success";
		} catch (final EGOVRuntimeException rexp) {
			target = "failure";
			logger.error("Error occurred in user jurisdication setup", rexp);
			throw new EGOVRuntimeException("Error occurred in user jurisdication setup", rexp);
		} catch (final Exception c) {
			logger.error("Error occurred in user jurisdication setup", c);
			target = "error";
			req.setAttribute("MESSAGE", "Error Before UserJurisdiction !!");
			throw new EGOVRuntimeException("Error occurred in user jurisdication setup", c);
		}
		return mapping.findForward(target);
	}
}
