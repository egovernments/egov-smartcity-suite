/*
 * @(#)SetupCityAction.java 3.0, 18 Jun, 2013 2:25:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.adminBoundry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.EgovAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetupCityAction extends EgovAction {
	/*private static final Logger LOG = LoggerFactory.getLogger(SetupHierarchyTypeAction.class);
	private final HeirarchyTypeService heirarchyTypeService = new HeirarchyTypeServiceImpl();

	*//**
	 * This method is used to get all the top boundries and set the list in session Calls the setup method 
	 * in EgovAction class that sets a list of all the departments in the session
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest req
	 * @param HttpServletResponse res
	 * @return ActionForward
	 **//*
	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {

		try {
			this.setup(req);
			req.getSession().setAttribute("hierarchySet", this.heirarchyTypeService.getAllHeirarchyTypes());
			return mapping.findForward("success");
		} catch (final Exception e) {
			LOG.error("Request cannot be processed due an internal server error.",e);
			req.setAttribute("MESSAGE", "Request cannot be processed due an internal server error.");
			throw new EGOVRuntimeException("Request cannot be processed due an internal server error.");
		}

	}*/
}
