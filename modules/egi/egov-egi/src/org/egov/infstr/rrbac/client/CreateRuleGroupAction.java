/*
 * @(#)CreateRuleGroupAction.java 3.0, 17 Jun, 2013 5:11:47 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.rrbac.client;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.role.ejb.api.RoleService;
import org.egov.lib.rjbac.role.ejb.server.RoleServiceImpl;
import org.egov.lib.rrbac.model.RuleGroup;
import org.egov.lib.rrbac.services.RbacService;
import org.egov.lib.rrbac.services.RbacServiceImpl;

public class CreateRuleGroupAction extends org.apache.struts.action.Action {

	private static final Logger LOG = LoggerFactory.getLogger(CreateRuleGroupAction.class);
	private final RbacService rbacService = new RbacServiceImpl();
	private final RoleService roleService = new RoleServiceImpl();

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		String target = "";
		String alertMessage = null;

		try {
			final org.apache.struts.action.DynaActionForm mfb = (org.apache.struts.action.DynaActionForm) form;

			final Integer rlID = (Integer) mfb.get("roleId");
			final Integer acID = (Integer) mfb.get("actionId");
			final String rgName = (String) mfb.get("ruleGroupName");
			final Integer ruleGroupId = (Integer) mfb.get("ruleGroupId");
			final String submitType = req.getParameter("submitType");
			final Role role = this.roleService.getRole(rlID);
			org.egov.lib.rrbac.model.Action action = null;
			if (!(acID == 0)) {
				action = this.rbacService.getActionById(acID);
			}
			if (submitType.equalsIgnoreCase("Create")) {
				final RuleGroup dupRuleGroup = this.rbacService.getRuleGroupByName(rgName);
				if (dupRuleGroup == null) {
					final RuleGroup ruleGroup = new RuleGroup();
					ruleGroup.setRoleId(role);
					ruleGroup.setName(rgName);
					action.addRuleGroup(ruleGroup);
					HibernateUtil.getCurrentSession().flush();
					alertMessage = "Executed successfully";

				} else {
					alertMessage = "Duplicate RuleGroup name.";
				}
				req.setAttribute("alertMessage", alertMessage);
				target = "success";
			} else if (submitType.equalsIgnoreCase("Modify")) {
				final Set rgSet = action.getRuleGroup();
				RuleGroup ruleGroup = null;
				for (final Iterator itr = rgSet.iterator(); itr.hasNext();) {
					ruleGroup = (RuleGroup) itr.next();
					if (ruleGroup.getId().equals(ruleGroupId)) {
						ruleGroup.setName(rgName);
					}
				}
				HibernateUtil.getCurrentSession().flush();
				alertMessage = "Executed successfully";
				req.setAttribute("alertMessage", alertMessage);

				target = "success";
			} else if (submitType.equalsIgnoreCase("Delete")) {

				final Set rgSet = action.getRuleGroup();
				RuleGroup ruleGroup = null;
				for (final Iterator itr = rgSet.iterator(); itr.hasNext();) {
					ruleGroup = (RuleGroup) itr.next();
					if (ruleGroup.getId().equals(ruleGroupId)) {
						action.removeRuleGroup(ruleGroup);
					}
				}
				HibernateUtil.getCurrentSession().flush();
				alertMessage = "Executed successfully";
				req.setAttribute("alertMessage", alertMessage);

				target = "success";

			} else {
				target = "error";
				LOG.error("Submit type is not configured.It should be either 'Create' or 'Modify' or 'Delete'.");
				alertMessage = "Configuration error.plrase report this to administrator.";
				req.setAttribute("alertMessage", alertMessage);
			}

		} catch (final Exception ex) {
			target = "error";
			LOG.error("Exception Encountered!!!" + ex.getMessage());
			throw new EGOVRuntimeException("Exception occured -----> " + ex.getMessage());

		}

		return mapping.findForward(target);

	}
}
