/*
 * @(#)CreateActionRoleMapAction.java 3.0, 17 Jun, 2013 5:02:21 PM
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

public class CreateActionRoleMapAction extends org.apache.struts.action.Action {

	private static final Logger LOG = LoggerFactory.getLogger(CreateActionRoleMapAction.class);
	private final RbacService rbacService = new RbacServiceImpl();
	private final RoleService roleService = new RoleServiceImpl();

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		String target = "";
		String alertMessage = null;

		try {
			final org.apache.struts.action.DynaActionForm mfb = (org.apache.struts.action.DynaActionForm) form;

			final Integer acID = (Integer) mfb.get("actionId");

			final Integer[] unMappedRoleId = (Integer[]) mfb.get("unMappedRoleId");
			final Integer[] mappedRoleId = (Integer[]) mfb.get("mappedRoleId");

			final org.egov.lib.rrbac.model.Action action = this.rbacService.getActionById(acID);
			for (final Integer roleID1 : unMappedRoleId) {
				final Role role = this.roleService.getRole(roleID1);
				action.addRole(role);

			}

			for (final Integer roleID2 : mappedRoleId) {
				final Role role = this.roleService.getRole(roleID2);
				action.removeRole(role);
				final Set rgList = action.getRuleGroup();
				RuleGroup ruleGroup = null;
				for (final Iterator itr1 = rgList.iterator(); itr1.hasNext();) {
					ruleGroup = (RuleGroup) itr1.next();
					final Role rgRole = ruleGroup.getRoleId();
					if (role.equals(rgRole)) {
						action.removeRuleGroup(ruleGroup);
					}
				}

			}

			HibernateUtil.getCurrentSession().flush();
			alertMessage = "Executed successfully";
			req.setAttribute("alertMessage", alertMessage);

			target = "success";

		} catch (final Exception ex) {
			target = "error";
			LOG.info("Exception Encountered!!!" + ex.getMessage());
			throw new EGOVRuntimeException("Exception occured -----> " + ex.getMessage());

		}
		return mapping.findForward(target);

	}

}
