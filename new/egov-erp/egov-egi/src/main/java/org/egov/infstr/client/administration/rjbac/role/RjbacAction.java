/*
 * @(#)RjbacAction.java 3.0, 18 Jun, 2013 3:03:43 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.administration.rjbac.role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.infstr.client.EgovAction;

public class RjbacAction extends EgovAction {

	

	/**
	 * This method forwards the control to different JSPS depending on the client's request 
	 * Calls the setup method in EgovAction class that sets a list of all the departments in the session
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest req
	 * @param HttpServletResponse res
	 * @return ActionForward
	 **/

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		this.setup(req);
		String target = "";
		if (req.getParameter("param").equals("createRole")) {
			target = "success";
		} else if (req.getParameter("param").equals("deleteUser")) {
			target = "todeleteUser";
		} else if (req.getParameter("param").equals("deleteDept")) {
			target = "todeleteDept";
		} else if (req.getParameter("param").equals("deleteRole")) {
			target = "todeleteRole";
		}
		return mapping.findForward(target);
	}
}
