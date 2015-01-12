/*
 * @(#)BoundaryTypeServiceImpl.java 3.0, 11 Jun, 2013 11:50:24 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry.ejb.server;

import java.util.List;

import org.egov.exceptions.NoSuchObjectException;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.BoundaryTypeDAO;
import org.egov.lib.admbndry.HeirarchyType;
import org.egov.lib.admbndry.ejb.api.BoundaryTypeService;

public class BoundaryTypeServiceImpl implements BoundaryTypeService {

	public BoundaryType createBoundaryType(final String name, final short heirarchyLevel, final BoundaryType parent, final boolean isLeaf) {
		return null;
	}

	@Override
	public void createBoundaryType(final BoundaryType bndryType) {
		new BoundaryTypeDAO().createBoundaryType(bndryType);
	}

	@Override
	public void removeBoundaryType(final BoundaryType bndryType) {
		new BoundaryTypeDAO().removeBoundaryType(bndryType);
	}

	@Override
	public void updateBoundaryType(final BoundaryType bndryType) {
		new BoundaryTypeDAO().updateBoundaryType(bndryType);
	}

	@Override
	public BoundaryType getBoundaryType(final short heirarchylevel, final HeirarchyType heirarchyType) {
		return new BoundaryTypeDAO().getBoundaryType(heirarchylevel, heirarchyType);
	}

	@Override
	public BoundaryType getTopBoundaryType(final HeirarchyType heirarchyType) {
		final short topHrchy = 1;
		return new BoundaryTypeDAO().getBoundaryType(topHrchy, heirarchyType);
	}

	@Override
	public BoundaryType getBoundaryType(final Integer id) {
		return new BoundaryTypeDAO().getBoundaryType(id);
	}

	@Override
	public BoundaryType getBoundaryType(final String bndryTypeName, final HeirarchyType heirarchyType) {
		return new BoundaryTypeDAO().getBoundaryType(bndryTypeName, heirarchyType);
	}

	@Override
	public List getParentBoundaryList(final Integer boundaryId) throws NoSuchObjectException {
		return new BoundaryTypeDAO().getParentBoundaryList(boundaryId);
	}
}
