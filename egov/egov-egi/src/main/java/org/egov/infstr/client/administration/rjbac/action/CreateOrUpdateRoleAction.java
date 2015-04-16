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
package org.egov.infstr.client.administration.rjbac.action;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infstr.client.EgovAction;
import org.egov.lib.rrbac.model.Action;
import org.egov.lib.rrbac.services.RbacService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateOrUpdateRoleAction extends EgovAction {
	private static final Logger LOG = LoggerFactory.getLogger(CreateOrUpdateRoleAction.class);
	private RbacService rbacService;
	private RoleService roleService;

	public void setRbacService(final RbacService rbacService) {
		this.rbacService = rbacService;
	}

	public void setRoleService(final RoleService roleService) {
		this.roleService = roleService;
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) {

		String target = "failure";
		if (!this.isTokenValid(req)) {
			return mapping.findForward(target);
		}
		final RoleActionForm roleActionform = (RoleActionForm) form;

		try {

			if (org.apache.commons.lang.StringUtils.isBlank(roleActionform.getRoleId())) {
				throw new EGOVRuntimeException("Role Id not found");
			}

			final Role role = this.roleService.getRoleById(Long.valueOf(roleActionform.getRoleId()));
			
			// Deleting role action mappings for those actions which are unchecked.
			final Set<Integer> delActionsSet = (Set<Integer>) req.getSession().getAttribute("delActions");

			if ((delActionsSet != null) && !delActionsSet.isEmpty()) {
				for (final Integer actionId : delActionsSet) {
					if (actionId != null) {
						final Action action = this.rbacService.getActionById(actionId);
						if ((action != null) && (role != null)) {
						 // This is commented while rewriting role master screen
						// code must be corrected while rewriting this screen
						//	action.removeRole(role);
						}
					}
				}
			}
			
			// The selected actions are mapped to role.
			if ((role != null) && (roleActionform.getActionId() != null) && (roleActionform.getActionId().length > 0)) {
				for (final String actionId : roleActionform.getActionId()) {
					if (org.apache.commons.lang.StringUtils.isNotBlank(actionId)) {
					 // This is commented while rewriting role master screen
		                        // code must be corrected while rewriting this screen
						//this.rbacService.getActionById(Integer.valueOf(actionId)).addRole(role);
					}
				}
			}

			
			target = "success";
			req.setAttribute("MESSAGE", "Role to Action mapping has successfully completed.");
		} catch (final Exception c) {
			LOG.error("Error occurred while setting Role to Action mapping",c);
			req.setAttribute("MESSAGE", "Could not complete the Role to Action mapping due to some internal server error");
			throw new EGOVRuntimeException("Error occurred while setting Role to Action mapping");
		}
		if (target.equals("success")) {
			this.resetToken(req);
		}
		return mapping.findForward(target);
	}
}
