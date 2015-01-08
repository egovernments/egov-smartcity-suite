/*
 * @(#)HierarchyAction.java 3.0, 18 Jun, 2013 2:21:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.adminBoundry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.commons.utils.EgovInfrastrUtil;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.lib.admbndry.HeirarchyType;
import org.egov.lib.admbndry.HeirarchyTypeImpl;
import org.egov.lib.admbndry.ejb.api.HeirarchyTypeService;
import org.egov.lib.admbndry.ejb.server.HeirarchyTypeServiceImpl;

public class HierarchyAction extends EgovAction {
	private static final Logger LOG = LoggerFactory.getLogger(HierarchyAction.class);
	private HeirarchyTypeService heirarchyTypeService = new HeirarchyTypeServiceImpl();
	/**
	 * This method is used to get all the top boundries and set the list in session 
	 * Calls the setup method in EgovAction class that sets a list of 
	 * all the departments in the session
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest req
	 * @param HttpServletResponse res
	 * @return ActionForward
	 **/
	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {

		final HierarchyForm heirarchyForm = (HierarchyForm) form;
		String target = "";
		
		if (req.getParameter("bool").equals("CREATE")) {
			try {
				final HeirarchyType hierarchy = new HeirarchyTypeImpl();
				hierarchy.setName(heirarchyForm.getName());
				hierarchy.setCode(heirarchyForm.getCode());
				heirarchyTypeService.create(hierarchy);
				new EgovInfrastrUtil().resetCache();
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
				final HeirarchyType hierarchy = heirarchyTypeService.getHeirarchyTypeByID(heirarchyTypeId);
				hierarchy.setName(heirarchyForm.getName());
				hierarchy.setCode(heirarchyForm.getCode());
				heirarchyTypeService.update(hierarchy);
				new EgovInfrastrUtil().resetCache();
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
				new EgovInfrastrUtil().resetCache();
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

}
