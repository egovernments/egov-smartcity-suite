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
import org.egov.lib.rrbac.model.RuleGroup;
import org.egov.lib.rrbac.services.RbacService;
import org.egov.lib.rrbac.services.RbacServiceImpl;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class CreateRoleActionMapAction extends org.apache.struts.action.Action {

	private SessionFactory sessionFactory;

	public CreateRoleActionMapAction(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private static final Logger logger = LoggerFactory.getLogger(CreateRoleActionMapAction.class);
	private final RbacService rbacService = new RbacServiceImpl();
	@Autowired
	private RoleService roleService ;

	@Override
	@Transactional
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		String target = "";
		String alertMessage = null;
		try {
			final org.apache.struts.action.DynaActionForm mfb = (org.apache.struts.action.DynaActionForm) form;

			final Long rlID = (Long) mfb.get("roleId");

			final Integer[] unMappedActionId = (Integer[]) mfb.get("unMappedActionId");
			final Integer[] mappedActionId = (Integer[]) mfb.get("mappedActionId");
			final Role role = this.roleService.getRoleById(rlID);
			for (final Integer actID1 : unMappedActionId) {
				final org.egov.lib.rrbac.model.Action action = this.rbacService.getActionById(actID1);
				// This is commented while rewriting role master screen
                                // code must be corrected while rewriting this screen
				//action.addRole(role);

			}

			for (final Integer actID2 : mappedActionId) {
				final org.egov.lib.rrbac.model.Action action = this.rbacService.getActionById(actID2);
				// This is commented while rewriting role master screen
                                // code must be corrected while rewriting this screen
				//action.removeRole(role);
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
			sessionFactory.getCurrentSession().flush();
			alertMessage = "Executed successfully";
			req.setAttribute("alertMessage", alertMessage);

			target = "success";

		} catch (final Exception ex) {
			target = "error";
			logger.error("Exception Encountered!!!" + ex.getMessage());
			throw new EGOVRuntimeException("Exception occured -----> " + ex.getMessage());

		}
		return mapping.findForward(target);

	}

}
