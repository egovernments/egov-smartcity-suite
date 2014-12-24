/*
 * @(#)ViewRuleAction.java 3.0, 18 Jun, 2013 11:41:13 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.rrbac.client;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.lib.rrbac.model.RuleGroup;
import org.egov.lib.rrbac.model.Rules;
import org.egov.lib.rrbac.services.RbacService;
import org.egov.lib.rrbac.services.RbacServiceImpl;

public class ViewRuleAction extends org.apache.struts.action.Action {

	private static final Logger LOG = LoggerFactory.getLogger(ViewRuleGroupAction.class);
	private final RbacService rbacService = new RbacServiceImpl();

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		String target = "";
		Set<Rules> ruList = null;
		try {
			final org.apache.struts.action.DynaActionForm mfb = (org.apache.struts.action.DynaActionForm) form;
			final String submitType = req.getParameter("submitType");

			final Integer rgID = (Integer) mfb.get("ruleGroupId");

			if (!(rgID == 0)) {
				final RuleGroup ruleGroup = this.rbacService.getRuleGroupById(rgID);
				ruList = ruleGroup.getRules();
			}

			if (submitType.equalsIgnoreCase("getRules")) {
				req.setAttribute("rulesList", ruList);
				target = "success";
			} else if (submitType.equalsIgnoreCase("getRulesToUpdate")) {
				req.setAttribute("updateRulesList", ruList);
				target = "success";

			} else {
				target = "error";

			}

		} catch (final Exception ex) {
			target = "error";
			LOG.error("Exception Encountered!!!" + ex.getMessage());
			throw new EGOVRuntimeException("Exception occured -----> " + ex.getMessage());

		}
		return mapping.findForward(target);

	}
}
