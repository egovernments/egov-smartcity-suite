/*
 * @(#)ViewRuleGroupAction.java 3.0, 18 Jun, 2013 11:43:32 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.rrbac.client;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.lib.rrbac.model.Action;
import org.egov.lib.rrbac.model.RuleGroup;
import org.egov.lib.rrbac.services.RbacService;
import org.egov.lib.rrbac.services.RbacServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ViewRuleGroupAction extends org.apache.struts.action.Action {

	private static final Logger LOG = LoggerFactory.getLogger(ViewRuleGroupAction.class);
	private final RbacService rbacService = new RbacServiceImpl();
	@Autowired
	private RoleService roleService ;

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		String target = "";
		Set<RuleGroup> rgList = null;
		Set<Action> rList = null;
		try {
			final org.apache.struts.action.DynaActionForm mfb = (org.apache.struts.action.DynaActionForm) form;
			final String submitType = req.getParameter("submitType");
			if (submitType.equalsIgnoreCase("getRG")) {
				final Integer actId = (Integer) mfb.get("actionId1");
				if (!(actId == 0)) {
					final org.egov.lib.rrbac.model.Action action = this.rbacService.getActionById(actId);
					rgList = action.getRuleGroup();
				}

				req.setAttribute("ruleGroupList", rgList);
				target = "success";
			} else if (submitType.equalsIgnoreCase("getActions")) {
				final Integer rId = (Integer) mfb.get("roleId1");
				if (!(rId == 0)) {
					final Role r = this.roleService.getRoleById(rId.longValue());
					// This is commented while rewriting role master screen
	                                // code must be corrected while rewriting this screen
					//rList = r.getActions();
				}

				req.setAttribute("actionsList", rList);
				req.setAttribute("searchRoleId", rId);
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
