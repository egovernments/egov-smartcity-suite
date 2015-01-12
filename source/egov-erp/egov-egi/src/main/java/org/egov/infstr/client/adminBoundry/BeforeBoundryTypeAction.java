/*
 * @(#)BeforeBoundryTypeAction.java 3.0, 18 Jun, 2013 1:47:21 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.adminBoundry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.EgovAction;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.HeirarchyType;
import org.egov.lib.admbndry.ejb.api.BoundaryTypeService;
import org.egov.lib.admbndry.ejb.api.HeirarchyTypeService;
import org.egov.lib.admbndry.ejb.server.BoundaryTypeServiceImpl;
import org.egov.lib.admbndry.ejb.server.HeirarchyTypeServiceImpl;

public class BeforeBoundryTypeAction extends EgovAction {
	private static final Logger LOG = LoggerFactory.getLogger(BeforeBoundryTypeAction.class);
	private BoundaryTypeService boundaryTypeService = new BoundaryTypeServiceImpl();
	private HeirarchyTypeService heirarchyTypeService = new HeirarchyTypeServiceImpl();
	
	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		try {
			final List<HeirarchyType> heirarchyList = new ArrayList<HeirarchyType>();
			final Set<HeirarchyType> heirarchyTypes = heirarchyTypeService.getAllHeirarchyTypes();
			for (final HeirarchyType heirarchyType : heirarchyTypes) {
				final BoundaryType boundaryType = boundaryTypeService.getBoundaryType(Short.valueOf(String.valueOf(1)), heirarchyType);
				if (boundaryType == null) {
					heirarchyList.add(heirarchyType);
				}
			}
			req.setAttribute("hierarchyTypeList", heirarchyList);
			return mapping.findForward("success");
		} catch (final Exception e) {
			LOG.error("Error occurred while setting up Boundary Type.",e);
			throw new EGOVRuntimeException("Error occurred while setting up Boundary Type.");
		}

	}
}
