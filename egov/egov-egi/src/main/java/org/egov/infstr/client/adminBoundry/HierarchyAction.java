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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.commons.utils.EgovInfrastrUtil;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HierarchyAction extends EgovAction {
	/*private static final Logger LOG = LoggerFactory.getLogger(HierarchyAction.class);
	private HeirarchyTypeService heirarchyTypeService;
	private EgovInfrastrUtil egovInfrastrUtil;

	public HierarchyAction(HeirarchyTypeService heirarchyTypeService, EgovInfrastrUtil egovInfrastrUtil) {
        this.heirarchyTypeService = heirarchyTypeService;
        this.egovInfrastrUtil = egovInfrastrUtil;
	}

	*//**
	 * This method is used to get all the top boundries and set the list in session
	 * Calls the setup method in EgovAction class that sets a list of
	 * all the departments in the session
	 **//*
	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {

		final HierarchyForm heirarchyForm = (HierarchyForm) form;
		String target = "";

		if (req.getParameter("bool").equals("CREATE")) {
			try {
				final HierarchyType hierarchy = new HierarchyType();
				hierarchy.setName(heirarchyForm.getName());
				hierarchy.setCode(heirarchyForm.getCode());
				heirarchyTypeService.create(hierarchy);
				egovInfrastrUtil.resetCache();
				target = "success";
				req.setAttribute("TARGET", target);
				req.setAttribute("MESSAGE", "Heirarchy successfully created.");
			} catch (final Exception c) {
				LOG.error("Heirarchy creation cannot be processed due to an internal server error.",c);
				req.setAttribute("MESSAGE", "Heirarchy creation cannot be processed due to an internal server error.");
				throw new EGOVRuntimeException("Heirarchy creation cannot be processed due to an internal server error.");
			}

		} else if (req.getParameter("bool").equals("UPDATE")) {
			try {

				final int heirarchyTypeId = (Integer) req.getSession().getAttribute("heirarchyTypeId");
				req.getSession().removeAttribute("heirarchyTypeId");
				final HierarchyType hierarchy = heirarchyTypeService.getHeirarchyTypeByID(heirarchyTypeId);
				hierarchy.setName(heirarchyForm.getName());
				hierarchy.setCode(heirarchyForm.getCode());
				heirarchyTypeService.update(hierarchy);
				egovInfrastrUtil.resetCache();
				target = "success";
				req.setAttribute("TARGET", target);
				req.setAttribute("MESSAGE", "Heirarchy update successful.");
			} catch (final Exception c) {
				LOG.error("Heirarchy update cannot be processed due to an internal server error.",c);
				req.setAttribute("MESSAGE", "Heirarchy update cannot be processed due to an internal server error.");
				throw new EGOVRuntimeException("Heirarchy update cannot be processed due to an internal server error.");
			}
		} else if (req.getParameter("bool").equals("DELETE")) {
			try {
				heirarchyTypeService.remove(heirarchyTypeService.getHeirarchyTypeByID((int) heirarchyForm.getHierarchyTypeid()));
				egovInfrastrUtil.resetCache();
				target = "success";
				req.setAttribute("MESSAGE", "Heirarchy successfully deleted.");
			} catch (final Exception c) {
				LOG.error("Heirarchy deletion cannot be processed due to an internal server error.",c);
				req.setAttribute("MESSAGE", "Heirarchy deletion cannot be processed due to an internal server error.");
				throw new EGOVRuntimeException("Heirarchy deletion cannot be processed due to an internal server error.");
			}
		}
		EgovMasterDataCaching.getInstance().removeFromCache("egi-hierarchyType");
		return mapping.findForward(target);

	}
*/
}
