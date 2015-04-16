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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.client.delegate.BoundaryTypeDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoundryTypeAction extends EgovAction {
	/*private static final Logger LOG = LoggerFactory.getLogger(BoundryTypeAction.class);
	private HeirarchyTypeService heirarchyTypeService = new HeirarchyTypeServiceImpl();
	*//**
	 * This method does Create, Update and Delete BoundryType based on the client's call
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest req
	 * @param HttpServletResponse res
	 * @return ActionForward
	 **//*

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws IOException, ServletException, EGOVException {

		try {
			String message = "";
			final javax.servlet.http.HttpSession session = req.getSession();
			final String operation = req.getParameter("operation");
			final BoundaryType boundaryType = new BoundaryType();
			final BoundryTypeForm boundryTypeForm = (BoundryTypeForm) form;

			final BoundaryTypeDelegate boundaryTypeDelegate = BoundaryTypeDelegate.getInstance();
			final HierarchyType heirarchyType = heirarchyTypeService.getHeirarchyTypeByID(Integer.valueOf(boundryTypeForm.getHeirarchyType().trim()));
			//boundaryType.setHeirarchyType(heirarchyType);
			boundaryType.setName(boundryTypeForm.getName());
			boundaryType.setParentName(boundryTypeForm.getParentName());
			//boundaryType.setBndryTypeLocal(boundryTypeForm.getBndryTypeLocal());

			if ("create".equals(operation)) {
				try {
					boundaryTypeDelegate.createBoundaryType(boundaryType);
					message = "Boundry Type successfully created.";
				} catch (final Exception e) {
					LOG.error("Error occurred while creating Boundary Type.",e);
					req.setAttribute("MESSAGE", "Error occurred while creating Boundary Type.");
					throw new EGOVRuntimeException("Error occurred while creating Boundary Type.");
				}

			} else if ("edit".equals(operation)) {
				try {
					boundaryTypeDelegate.updateBoundaryType(boundaryType);
					message = "Boundry Type successfully modified.";
				} catch (final Exception e) {
					LOG.error("Error occurred while updating Boundary Type.",e);
					req.setAttribute("MESSAGE", "Error occurred while updating Boundary Type");
					throw new EGOVRuntimeException("Error occurred while updating Boundary Type.");

				}

			} else if ("delete".equals(operation)) {
				try {
					boundaryTypeDelegate.deleteBoundaryType(boundaryType);
					message = "Boundry Type successfully deleted.";
				} catch (final Exception e) {
					LOG.error("Error occurred while deleting Boundary Type",e);
					req.setAttribute("MESSAGE", "Error occurred while deleting Boundary Type");
					throw new EGOVRuntimeException("Error occurred while deleting Boundary Type");

				}

			} else {
				throw new EGOVRuntimeException("Boundary Type operation not found.");
			}
			
			session.setAttribute("MESSAGE", message);
			return mapping.findForward("success");
		} catch (final Exception e) {
			LOG.error("Error occurred while loading Boundary Type screen",e);
			req.setAttribute("MESSAGE", "Request cannot be processed due to an internal server error.");
			throw new EGOVRuntimeException("Request cannot be processed due to an internal server error.");
		}

	}*/
}
