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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.commons.service.GenericCommonsService;
import org.egov.infstr.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BeforeCreateOrUpdateRoleAction extends EgovAction {
	private static final Logger logger = LoggerFactory.getLogger(BeforeCreateOrUpdateRoleAction.class);
	private GenericCommonsService genericCommonsService;
	@Autowired
	private RoleService roleService;

	public void setGenericCommonsService(final GenericCommonsService genericCommonsService) {
		this.genericCommonsService = genericCommonsService;
	}

	public void setRoleService (final RoleService roleService) {
		this.roleService = roleService;
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) {
		String target = "failure";
		try {
			final String moduleId = req.getParameter("moduleId");
			final String roleId = req.getParameter("roleId");

			if (StringUtils.isBlank(moduleId)) {
				throw new EGOVRuntimeException("Could not get Module Id");
			} else if (StringUtils.isBlank(roleId)) {
				throw new EGOVRuntimeException("Could not get Role Id");
			}
			this.saveToken(req);			
			req.setAttribute("module", this.genericCommonsService.getModuleByID(Integer.valueOf(moduleId)));
			req.setAttribute("role", this.roleService.getRoleById(Long.valueOf(roleId)));
			req.getSession().setAttribute("moduleId", Integer.valueOf(moduleId));
			target = "success";
		} catch (final Exception c) {
			logger.error("Error occurred while trying to initiate Role operation.",c);
			req.setAttribute("MESSAGE", "Could not process the request, due to an internal server error");
			throw new EGOVRuntimeException("Error occurred while trying to initiate Role operation.");

		}
		return mapping.findForward(target);
	}

}
