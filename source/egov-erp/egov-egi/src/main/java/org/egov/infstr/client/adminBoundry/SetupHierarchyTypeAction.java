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
package org.egov.infstr.client.adminBoundry;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infstr.client.EgovAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetupHierarchyTypeAction extends EgovAction {

	/*private static final Logger LOG = LoggerFactory.getLogger(SetupHierarchyTypeAction.class);
	private final HeirarchyTypeService heirarchyTypeService = new HeirarchyTypeServiceImpl();

	*//**
	 * This method is used to get all the top boundries and set the list in session Calls the setup method 
	 * in EgovAction class that sets a list of all the departments in the session
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest req
	 * @param HttpServletResponse res
	 * @return ActionForward
	 **//*
	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		final HierarchyForm hierarchyForm = (HierarchyForm) form;
		String target = "";
		Set hierarchySet = new HashSet();
		this.setup(req);
		this.saveToken(req);
		if ("VIEW".equals(req.getParameter("bool"))) {
			try {
				if (hierarchyForm.getHierarchyTypeid() != null) {
					req.getSession().setAttribute("heirarchyTypeId", (int) hierarchyForm.getHierarchyTypeid());
					final HierarchyType hierarchy = this.heirarchyTypeService.getHeirarchyTypeByID(hierarchyForm.getHierarchyTypeid());
					hierarchyForm.setName(hierarchy.getName());
					hierarchyForm.setCode(hierarchy.getCode());
					target = "viewHierarchy";
				} else {
					hierarchySet = this.heirarchyTypeService.getAllHeirarchyTypes();
					target = "success";
				}
			} catch (final Exception e) {
				LOG.error("Request cannot be processed due an internal server error.",e);
				req.setAttribute("MESSAGE", "Request cannot be processed due an internal server error.");
				throw new EGOVRuntimeException("Request cannot be processed due an internal server error.");

			}
		}
		if ("UPDATE".equals(req.getParameter("bool"))) {
			try {
				req.getSession().setAttribute("heirarchyTypeId", (int) hierarchyForm.getHierarchyTypeid());
				final HierarchyType hierarchy = this.heirarchyTypeService.getHeirarchyTypeByID(hierarchyForm.getHierarchyTypeid());
				hierarchyForm.setName(hierarchy.getName());
				hierarchyForm.setCode(hierarchy.getCode());
				target = "updateHierarchy";

			} catch (final Exception e) {
				LOG.error("Request cannot be processed due an internal server error.",e);
				req.setAttribute("MESSAGE", "Request cannot be processed due an internal server error.");
				throw new EGOVRuntimeException("Request cannot be processed due an internal server error.");

			}
		}

		req.getSession().setAttribute("hierarchySet", hierarchySet);
		return mapping.findForward(target);
	}*/
}
