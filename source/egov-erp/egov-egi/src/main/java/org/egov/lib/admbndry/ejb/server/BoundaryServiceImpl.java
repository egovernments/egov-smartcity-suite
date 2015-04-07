/*
 * @(#)BoundaryServiceImpl.java 3.0, 11 Jun, 2013 11:46:12 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry.ejb.server;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryFinder;
import org.egov.lib.admbndry.VisitableBoundary;
import org.egov.lib.admbndry.ejb.api.BoundaryService;
import org.egov.lib.admbndry.ejb.api.BoundaryTypeService;
import org.egov.lib.admbndry.ejb.api.HeirarchyTypeService;

public class BoundaryServiceImpl implements BoundaryService {

	private static final Logger LOG = LoggerFactory.getLogger(BoundaryServiceImpl.class);
	private BoundaryDAO boundaryDAO;
	private BoundaryTypeService boundaryTypeService;
	private HeirarchyTypeService heirarchyTypeService;

	public void setBoundaryDAO(final BoundaryDAO boundaryDAO) {
		this.boundaryDAO = boundaryDAO;
	}

	public void setBoundaryTypeService(final BoundaryTypeService boundaryTypeService) {
		this.boundaryTypeService = boundaryTypeService;
	}

	public void setHeirarchyTypeService(final HeirarchyTypeService heirarchyTypeService) {
		this.heirarchyTypeService = heirarchyTypeService;
	}

	@Override
	public Boundary createBoundary(final Boundary boundary) {
		return this.boundaryDAO.createBoundary(boundary);
	}

	@Override
	public void updateBoundary(final Boundary boundary) {
		this.boundaryDAO.updateBoundary(boundary);
	}

	@Override
	public void removeBoundary(final Boundary boundary) {
		this.boundaryDAO.removeBoundary(boundary);
	}

	@Override
	public List<Boundary> getAllBoundaries(final BoundaryType arg1, final int topLevelBoundaryID) {
		return this.boundaryDAO.getAllBoundaries(arg1, topLevelBoundaryID);
	}

	@Override
	public List<Boundary> getAllBoundaries(final short heirarchyLevel, final HierarchyType heirarchyType, final short topLevelBoundaryID) {
		return this.boundaryDAO.getAllBoundaries(heirarchyLevel, heirarchyType, topLevelBoundaryID);
	}

	@Override
	public Boundary getBoundary(final short bndryNum, final BoundaryType bndryType, final int topLevelBoundaryID) {
		return this.boundaryDAO.getBoundary(bndryNum, bndryType, topLevelBoundaryID);
	}

	@Override
	public Boundary getBoundary(final BigInteger bndryNum, final BoundaryType bndryType, final int topLevelBoundaryID) {
		return this.boundaryDAO.getBoundaryByBndryNumAsBigInteger(bndryNum, bndryType, topLevelBoundaryID);
	}

	@Override
	public Boundary getBoundary(final short bndryNum, final short bndryTypeHeirarchyLevel, final HierarchyType heirarchyType, final int topLevelBoundaryID) {
		return this.boundaryDAO.getBoundary(bndryNum, bndryTypeHeirarchyLevel, heirarchyType, topLevelBoundaryID);
	}

	@Override
	public Boundary getBoundary(final BigInteger bndryNum, final short bndryTypeHeirarchyLevel, final HierarchyType heirarchyType, final int topLevelBoundaryID) {
		return this.boundaryDAO.getBoundaryByBndryNumAsBigInteger(bndryNum, bndryTypeHeirarchyLevel, heirarchyType, topLevelBoundaryID);
	}

	@Override
	public Boundary getTopBoundary(final String boundaryName, final HierarchyType heirarchyType) {
		return this.boundaryDAO.getTopBoundary(boundaryName, heirarchyType);
	}

	@Override
	public List<Boundary> getTopBoundaries(final HierarchyType heirarchyType) {
		return this.boundaryDAO.getTopBoundaries(heirarchyType);
	}

	@Override
	public String getBoundaryNameForID(final Integer id) {
		return this.boundaryDAO.getBoundaryNameForID(id);
	}

	@Override
	public Boundary getBoundary(final int bndryID) {
		return this.boundaryDAO.getBoundary(bndryID);

	}

	@Override
	public Boundary getAllBoundaryById(final int bndryID) {
		return this.boundaryDAO.getAllBoundaryById(bndryID);

	}

	@Override
	public Boundary getBoundaryById(final int bndryID) {
		return this.boundaryDAO.getBoundaryById(bndryID);

	}

	@Override
	public Boundary getBoundary(final short bndryNum, final Integer parentBoundaryID, final BoundaryType boundaryType) {
		return this.boundaryDAO.getBoundary(bndryNum, parentBoundaryID, boundaryType);
	}

	@Override
	public Boundary getBoundary(final BigInteger bndryNum, final Integer parentBoundaryID, final BoundaryType boundaryType) {
		return this.boundaryDAO.getBoundaryByBndryNumAsBigInteger(bndryNum, parentBoundaryID, boundaryType);
	}

	@Override
	public List<Integer> getBoundaryIdList(final List<Boundary> boundaryObjList) {

		final List lstOfBndryIds = new ArrayList();
		if (boundaryObjList == null) {
			return lstOfBndryIds;
		}
		for (final Object element : boundaryObjList) {
			final List resultList = (List) element;
			for (final Iterator resItr = resultList.iterator(); resItr.hasNext();) {
				final Boundary bndry = (Boundary) resItr.next();
				final Integer bondryId = bndry.getId();
				if (bondryId != null) {
					if (!lstOfBndryIds.contains(bondryId)) {
						lstOfBndryIds.add(bondryId);
					}
				}
			}
		}
		return lstOfBndryIds;
	}

	/**
	 * Reutrns List of Leaf Boundaries(last level i.e street) for a given parent boundary Id and Leaf BoundaryType
	 * @param parentBoundaryId
	 * @return
	 */
	@Override
	public List getAllLeafBoundariesForParentBndryIdAndChBType(final Integer parentBoundaryId, final String leafBTypeName) {
		final List finalList = new ArrayList();

		if (parentBoundaryId == null || leafBTypeName == null || leafBTypeName.equals("")) {
			throw new IllegalArgumentException("parentBoundaryId or leafBTypeName is null");
		} else {
			try {
				final Boundary parentBoundary = getBoundary(parentBoundaryId);

				Boundary bn = null;
				final VisitableBoundary vBoundary = new VisitableBoundary(parentBoundary);
				final BoundaryFinder visitor = new BoundaryFinder();

				final HierarchyType htype = this.heirarchyTypeService.getHeirarchyTypeByID(1);

				final BoundaryType childBoundaryType = this.boundaryTypeService.getBoundaryType(leafBTypeName, htype);

				visitor.setTargetBoundaryType(childBoundaryType);

				vBoundary.accept(visitor);
				final List result = visitor.getResult();

				final Iterator itr = result.iterator();
				// Iterate through the Parent Boundary List and Get all the child boundaries list.
				while (itr.hasNext()) {
					bn = (Boundary) itr.next();

					final VisitableBoundary vchBoundary = new VisitableBoundary(bn);
					final BoundaryFinder chvisitor = new BoundaryFinder();

					chvisitor.setTargetBoundaryType(childBoundaryType);
					vchBoundary.accept(chvisitor);
					final List chresult = chvisitor.getResult();
					finalList.add(chresult);
				}
			} catch (final Exception e) {
				LOG.error("Error occurred in getAllLeafBoundariesForParentBndryIdAndChBType", e);
				throw new EGOVRuntimeException("Error occurred in getAllLeafBoundariesForParentBndryIdAndChBType", e);
			}
		}
		return finalList;
	}

	@Override
	public Set<Boundary> getCrossHeirarchyParent(final Boundary childBoundary) {
		return this.boundaryDAO.getCrossHeirarchyParent(childBoundary);

	}

	@Override
	public Set<Boundary> getCrossHeirarchyChildren(final Boundary parentBoundary, final BoundaryType childBoundaryType) {
		return this.boundaryDAO.getCrossHeirarchyChildren(parentBoundary, childBoundaryType);

	}

    @Override
    public List<Boundary> getBoundaryByNameLike(String name) {
        return this.boundaryDAO.findByNameLike(name);
    }
}
