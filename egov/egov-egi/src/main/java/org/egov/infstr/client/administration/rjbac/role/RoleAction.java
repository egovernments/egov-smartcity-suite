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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.commons.utils.EgovInfrastrUtil;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infstr.client.EgovAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoleAction extends EgovAction {
	private static final Logger LOG = LoggerFactory.getLogger(RoleAction.class);
	private final RoleService roleService;
	private final EgovInfrastrUtil egovInfrastrUtil;

	public RoleAction(RoleService roleService, EgovInfrastrUtil egovInfrastrUtil) {
		this.roleService = roleService;
		this.egovInfrastrUtil = egovInfrastrUtil;
	}

	/**
	 * This method is used to get all the top boundaries and set the list in session Calls the setup method in 
	 * EgovAction class that sets a list of all the departments in the session
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest req
	 * @param HttpServletResponse res
	 * @return ActionForward
	 **/
	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		
		if ( !isTokenValid(req) ) {
		    return mapping.findForward("error");
		}
		
		final RoleForm roleForm = (RoleForm) form;
		String target = "";
		final HttpSession session = req.getSession();
		if (req.getParameter("bool").equals("CREATE")) {
			final Role role = new Role();
			role.setName(roleForm.getRoleName());
			role.setDescription(roleForm.getRoleDesc());
			/*role.setLocalName(roleForm.getRoleNameLocal());
			role.setLocalDescription(roleForm.getRoleDescLocal());*/

			try {
				roleService.createRole(role);
				target = "success";
				roleForm.reset(mapping, req);
				req.setAttribute("MESSAGE", "Role successfully created");
				egovInfrastrUtil.resetCache();
			} catch (final Exception c) {
				LOG.error("Error occurred while creating Role",c);
				req.setAttribute("MESSAGE", "Role could not be created due to an internal error!");
				throw new EGOVRuntimeException("Error occurred while creating Role");
			}
		} else if (req.getParameter("bool").equals("UPDATE")) {

			final Role role  = roleService.getRoleById((Long)session.getAttribute("roleIdValue"));
			role.setName(roleForm.getRoleName());
			role.setDescription(roleForm.getRoleDesc());
			/*role.setLocalName(roleForm.getRoleNameLocal());
			role.setLocalDescription(roleForm.getRoleDescLocal());*/

			try {
				roleService.update(role);
				target = "success";
				roleForm.reset(mapping, req);
				req.setAttribute("MESSAGE", "Role successfully modified");
				egovInfrastrUtil.resetCache();
			} catch (final Exception c) {
				LOG.error("Error occurred while updating Role",c);
				req.setAttribute("MESSAGE", "Role could not be modified due to an internal error!");
				throw new EGOVRuntimeException("Error occurred while updating Role");
			}
		} else if (req.getParameter("bool").equals("DELETE")) {
			
			final Role role  = roleService.getRoleById(roleForm.getRoleId().longValue());

			try {
				roleService.remove(role);
				target = "success";
				roleForm.reset(mapping, req);
				req.setAttribute("MESSAGE", "Role successfully deleted");
				egovInfrastrUtil.resetCache();
			} catch (final Exception c) {
				LOG.error("Error occurred while deleting Role", c);
				req.setAttribute("MESSAGE", "Role could not be deleted due to an internal error!");
				throw new EGOVRuntimeException("Error occurred while deleting Role");
			}
		}
		if (target.equals("success")) {
			 resetToken(req);
		}
		return mapping.findForward(target);
	}

}
