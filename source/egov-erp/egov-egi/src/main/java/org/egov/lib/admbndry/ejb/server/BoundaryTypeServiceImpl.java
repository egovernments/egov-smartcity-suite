/*
 * @(#)BoundaryTypeServiceImpl.java 3.0, 11 Jun, 2013 11:50:24 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry.ejb.server;

import org.egov.exceptions.NoSuchObjectException;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.lib.admbndry.BoundaryTypeDAO;
import org.egov.lib.admbndry.ejb.api.BoundaryTypeService;
import org.hibernate.SessionFactory;

import java.util.List;

public class BoundaryTypeServiceImpl implements BoundaryTypeService {

	private SessionFactory sessionFactory;

	public BoundaryTypeServiceImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public BoundaryType createBoundaryType(final String name, final short heirarchyLevel, final BoundaryType parent, final boolean isLeaf) {
		return null;
	}

	@Override
	public void createBoundaryType(final BoundaryType bndryType) {
		new BoundaryTypeDAO(sessionFactory).createBoundaryType(bndryType);
	}

	@Override
	public void removeBoundaryType(final BoundaryType bndryType) {
		new BoundaryTypeDAO(sessionFactory).removeBoundaryType(bndryType);
	}

	@Override
	public void updateBoundaryType(final BoundaryType bndryType) {
		new BoundaryTypeDAO(sessionFactory).updateBoundaryType(bndryType);
	}

	@Override
	public BoundaryType getBoundaryType(final short heirarchylevel, final HierarchyType heirarchyType) {
		return new BoundaryTypeDAO(sessionFactory).getBoundaryType(heirarchylevel, heirarchyType);
	}

	@Override
	public BoundaryType getTopBoundaryType(final HierarchyType heirarchyType) {
		final short topHrchy = 1;
		return new BoundaryTypeDAO(sessionFactory).getBoundaryType(topHrchy, heirarchyType);
	}

	@Override
	public BoundaryType getBoundaryType(final Integer id) {
		return new BoundaryTypeDAO(sessionFactory).getBoundaryType(id);
	}

	@Override
	public BoundaryType getBoundaryType(final String bndryTypeName, final HierarchyType heirarchyType) {
		return new BoundaryTypeDAO(sessionFactory).getBoundaryType(bndryTypeName, heirarchyType);
	}

	@Override
	public List getParentBoundaryList(final Integer boundaryId) throws NoSuchObjectException {
		return new BoundaryTypeDAO(sessionFactory).getParentBoundaryList(boundaryId);
	}
}
