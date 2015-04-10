/*
 * @(#)BeforeBoundryTypeAction.java 3.0, 18 Jun, 2013 1:47:21 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.adminBoundry;

import org.egov.infstr.client.EgovAction;

public class BeforeBoundryTypeAction extends EgovAction {
	/*private static final Logger LOG = LoggerFactory.getLogger(BeforeBoundryTypeAction.class);
	private BoundaryTypeService boundaryTypeService = new BoundaryTypeServiceImpl(null);
	private HeirarchyTypeService heirarchyTypeService = new HeirarchyTypeServiceImpl();
	
	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		try {
			final List<HierarchyType> heirarchyList = new ArrayList<HierarchyType>();
			final Set<HierarchyType> heirarchyTypes = heirarchyTypeService.getAllHeirarchyTypes();
			for (final HierarchyType heirarchyType : heirarchyTypes) {
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

	}*/
}
