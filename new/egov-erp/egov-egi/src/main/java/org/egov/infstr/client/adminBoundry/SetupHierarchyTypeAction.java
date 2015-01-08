/*
 * @(#)SetupHierarchyTypeAction.java 3.0, 18 Jun, 2013 2:27:02 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.adminBoundry;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.EgovAction;
import org.egov.lib.admbndry.HeirarchyType;
import org.egov.lib.admbndry.ejb.api.HeirarchyTypeService;
import org.egov.lib.admbndry.ejb.server.HeirarchyTypeServiceImpl;

public class SetupHierarchyTypeAction extends EgovAction {

	private static final Logger LOG = LoggerFactory.getLogger(SetupHierarchyTypeAction.class);
	private final HeirarchyTypeService heirarchyTypeService = new HeirarchyTypeServiceImpl();

	/**
	 * This method is used to get all the top boundries and set the list in session Calls the setup method 
	 * in EgovAction class that sets a list of all the departments in the session
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest req
	 * @param HttpServletResponse res
	 * @return ActionForward
	 **/
	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		final HierarchyForm hierarchyForm = (HierarchyForm) form;
		String target = "";
		Set hierarchySet = new HashSet();
		this.setup(req);
		this.saveToken(req);
		if ("VIEW".equals(req.getParameter("bool"))) {
			try {
				if (hierarchyForm.getHierarchyTypeid() != null) {
					req.getSession().setAttribute("heirarchyTypeId", (int) hierarchyForm.getHierarchyTypeid());
					final HeirarchyType hierarchy = this.heirarchyTypeService.getHeirarchyTypeByID(hierarchyForm.getHierarchyTypeid());
					hierarchyForm.setName(hierarchy.getName());
					hierarchyForm.setCode(hierarchy.getCode());
					target = "viewHierarchy";
				} else {
					hierarchySet = this.heirarchyTypeService.getAllHeirarchyTypes();
					target = "success";
				}
			} catch (final Exception e) {
				LOG.error("Request cannot be processed due an internal server error.",e);
				req.setAttribute("MESSAGE", "Request cannot be processed due an internal server error.");
				throw new EGOVRuntimeException("Request cannot be processed due an internal server error.");

			}
		}
		if ("UPDATE".equals(req.getParameter("bool"))) {
			try {
				req.getSession().setAttribute("heirarchyTypeId", (int) hierarchyForm.getHierarchyTypeid());
				final HeirarchyType hierarchy = this.heirarchyTypeService.getHeirarchyTypeByID(hierarchyForm.getHierarchyTypeid());
				hierarchyForm.setName(hierarchy.getName());
				hierarchyForm.setCode(hierarchy.getCode());
				target = "updateHierarchy";

			} catch (final Exception e) {
				LOG.error("Request cannot be processed due an internal server error.",e);
				req.setAttribute("MESSAGE", "Request cannot be processed due an internal server error.");
				throw new EGOVRuntimeException("Request cannot be processed due an internal server error.");

			}
		}

		req.getSession().setAttribute("hierarchySet", hierarchySet);
		return mapping.findForward(target);
	}
}
