/*
 * @(#)BeforeRuleGroupMapAction.java 3.0, 17 Jun, 2013 4:58:41 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.rrbac.client;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.lib.rrbac.model.RuleGroup;
import org.egov.lib.rrbac.model.RuleType;
import org.egov.lib.rrbac.services.RbacService;
import org.egov.lib.rrbac.services.RbacServiceImpl;

public class BeforeRuleGroupMapAction extends Action {

	private static final Logger LOG = LoggerFactory.getLogger(BeforeRuleGroupMapAction.class);
	private final RbacService rbacService = new RbacServiceImpl();

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		String target = "";
		try {
			final List<RuleGroup> rgList = this.rbacService.getRuleGroupList();
			final List<RuleType> rtList = this.rbacService.getRuleTypeList();
			Collections.sort(rgList);
			Collections.sort(rtList);
			req.getSession().setAttribute("ruleGroup", rgList);
			req.getSession().setAttribute("ruleType", rtList);
			target = "success";
		} catch (final Exception ex) {
			target = "error";
			LOG.error("Exception Encountered!!!" + ex.getMessage());
			throw new EGOVRuntimeException("Exception occured -----> " + ex.getMessage());

		}

		return mapping.findForward(target);

	}
}
