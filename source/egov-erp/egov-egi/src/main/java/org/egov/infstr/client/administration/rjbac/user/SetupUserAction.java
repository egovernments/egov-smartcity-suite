/*
 * @(#)SetupUserAction.java 3.0, 18 Jun, 2013 2:35:24 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.administration.rjbac.user;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.client.delegate.UserDelegate;
import org.egov.lib.rjbac.role.ejb.api.RoleService;
import org.egov.lib.rjbac.role.ejb.server.RoleServiceImpl;

public class SetupUserAction extends EgovAction {
	
	private static final Logger LOG = LoggerFactory.getLogger(SetupUserAction.class);
	private UserDelegate userDelegate;
	private final RoleService roleService = new RoleServiceImpl();
	/**
	 * This method is used to get all the top boundries and set the list in session Calls the setup
	 * method in EgovAction class that sets a list of all the departments in the session
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest req
	 * @param HttpServletResponse res
	 * @return ActionForward
	 */
	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		List topBndryList = new ArrayList();
		List roleList = new ArrayList();
		String target = "";
		this.setup(req);
		try {
			topBndryList = this.userDelegate.getTopBondaries();
			roleList = roleService.getAllRoles();
			target = "success";
		} catch (final EGOVRuntimeException e) {
			LOG.error("Error occurred User Setup", e);
			target = "error";
			req.setAttribute("MESSAGE", "Error before UserAction !!");
			throw new EGOVRuntimeException("Error occurred User Setup", e);
		} catch (final Exception c) {
			LOG.error("Error occurred User Setup", c);
			target = "error";
			req.setAttribute("MESSAGE", "Error before UserAction !!");
			throw new EGOVRuntimeException("Error occurred in User Setup", c);
		}
		req.setAttribute("TopBndriesList", topBndryList);
		req.getSession().setAttribute("RoleList", roleList);
		return mapping.findForward(target);
	}
	public void setUserDelegate(UserDelegate userDelegate) {
		this.userDelegate = userDelegate;
	}
}
