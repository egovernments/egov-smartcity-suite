/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infstr.rrbac.client;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rrbac.model.RuleGroup;
import org.egov.lib.rrbac.services.RbacService;
import org.egov.lib.rrbac.services.RbacServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateRuleGroupAction extends org.apache.struts.action.Action {

	private static final Logger LOG = LoggerFactory.getLogger(CreateRuleGroupAction.class);
	private final RbacService rbacService = new RbacServiceImpl();
	@Autowired
	private RoleService roleService;;


	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		String target = "";
		String alertMessage = null;

		try {
			final org.apache.struts.action.DynaActionForm mfb = (org.apache.struts.action.DynaActionForm) form;

			final Long rlID = (Long) mfb.get("roleId");
			final Integer acID = (Integer) mfb.get("actionId");
			final String rgName = (String) mfb.get("ruleGroupName");
			final Integer ruleGroupId = (Integer) mfb.get("ruleGroupId");
			final String submitType = req.getParameter("submitType");
			final Role role = this.roleService.getRoleById(rlID);
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
