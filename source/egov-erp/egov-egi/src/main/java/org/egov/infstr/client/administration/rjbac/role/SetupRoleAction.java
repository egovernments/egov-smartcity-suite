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
package org.egov.infstr.client.administration.rjbac.role;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infstr.client.EgovAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SetupRoleAction extends EgovAction {
	private static final Logger LOG = LoggerFactory.getLogger(SetupRoleAction.class);
	@Autowired
	private RoleService roleService;
	/**
	 * This method is used to get all the top boundries and set the list in session Calls the setup 
	 * method in EgovAction class that sets a list of all the departments in the session
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest req
	 * @param HttpServletResponse res
	 * @return ActionForward
	 **/
	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		final RoleForm roleForm = (RoleForm) form;
		String target = "";
		this.setup(req);
		this.saveToken(req);
		List<Role> roleList = new ArrayList<Role>();
		if (req.getParameter("bool").equals("VIEW")) {

			try {
				if (roleForm.getRoleId() != null) {
					final Long roleid = roleForm.getRoleId().longValue();
					final Role role = roleService.getRoleById(roleid);
					roleForm.setRoleName(role.getName());
					roleForm.setRoleDesc(role.getDescription());
					/*roleForm.setRoleNameLocal(role.getLocalName());
					roleForm.setRoleDescLocal(role.getLocalDescription());*/
					target = "viewRole";
				} else {
					roleList = roleService.getAllRoles();
					target = "success";
				}
			} catch (final Exception c) {
				LOG.error("Error occurred while try to view Role.",c);
				req.setAttribute("MESSAGE", "Could not process Role view due to an internal server error.");
				throw new EGOVRuntimeException("Error occurred while try to view Role." );

			}

		} else if (req.getParameter("bool").equals("UPDATE")) {

			try {
				final HttpSession session = req.getSession();
				if (roleForm.getRoleId() != null) {
					final Long roleid = roleForm.getRoleId().longValue();
					session.setAttribute("roleIdValue", roleid);
					final Role role = roleService.getRoleById(roleid);
					roleForm.setRoleName(role.getName());
					roleForm.setRoleDesc(role.getDescription());
					/*roleForm.setRoleNameLocal(role.getLocalName());
					roleForm.setRoleDescLocal(role.getLocalDescription());*/
					target = "updateRole";
				} else {
					roleList = roleService.getAllRoles();
					target = "success";
				}
			} catch (final Exception c) {
				LOG.error("Error occurred while try to update Role.",c);
				req.setAttribute("MESSAGE", "Could not process Role update due to an internal server error.");
				throw new EGOVRuntimeException("Error occurred while try to update Role.");

			}

		}

		req.getSession().setAttribute("roleList", roleList);
		return mapping.findForward(target);
	}
}
