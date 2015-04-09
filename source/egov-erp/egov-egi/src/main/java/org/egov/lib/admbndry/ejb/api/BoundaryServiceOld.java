/*
 * @(#)BoundaryService.java 3.0, 11 Jun, 2013 11:03:14 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry.ejb.api;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.lib.admbndry.Boundary;

public interface BoundaryServiceOld {

	/**
	 * This method creates the boundary and also recursively creates the subsequent child boundaries.
	 * @param Boundary to be created.
	 */

	Boundary createBoundary(Boundary boundary);

	/**
	 * This method updates the boundary and also recursively updates the subsequent child boundaries if there are any changes made in that.
	 * @param Boundary to be updated.
	 */

	void updateBoundary(Boundary boundary);

	/**
	 * This method removes the boundary and also recursively removes the subsequent child boundaries if there are any.
	 * @param Boundary to be remove.
	 */

	void removeBoundary(Boundary boundary);

	/**
	 * This method gets all the boundaries of the particular boundary type and its top level boundary's pk being the given topLevelBoundaryID.
	 * @param BoundaryType, topLevelBoundaryID PK of the top level boundary.
	 * @return Set of Boundaries.
	 */

	List<Boundary> getAllBoundaries(BoundaryType arg1, int topLevelBoundaryID);

	/**
	 * This method gets all the boundaries of the particular boundary type of the given hierarchy level and its top level boundary's pk being the given topLevelBoundaryID.
	 * @param BoundaryType, topLevelBoundaryID PK of the top level boundary.
	 * @return Set of Boundaries.
	 */

	List<Boundary> getAllBoundaries(short heirarchyLevel, HierarchyType heirarchyType, short topLevelBoundaryID);

	/**
	 * This method gets a single boundary of the particular boundary type and of the given boundary number and its top level boundary's pk being the given topLevelBoundaryID.
	 * @param BoundaryType, topLevelBoundaryID PK of the top level boundary.
	 * @return Set of Boundaries.
	 */

	Boundary getBoundary(short bndryNum, BoundaryType bndryType, int topLevelBoundaryID);

	Boundary getBoundary(BigInteger bndryNum, BoundaryType bndryType, int topLevelBoundaryID);

	/**
	 * This method gets a single boundary of the particular boundary type of a particular hierarchy level and of the given boundary number and its top level boundary's pk being the given topLevelBoundaryID.
	 * @param BoundaryType, topLevelBoundaryID PK of the top level boundary.
	 * @return Set of Boundaries.
	 */

	Boundary getBoundary(short bndryNum, short bndryTypeHeirarchyLevel, HierarchyType heirarchyType, int topLevelBoundaryID);

	Boundary getBoundary(BigInteger bndryNum, short bndryTypeHeirarchyLevel, HierarchyType heirarchyType, int topLevelBoundaryID);

	/**
	 * This method gets a single boundary for a given boundary number of the particular boundary type and its parent boundaryId being the parentBoundaryID.
	 * @param bndryNum, parentBoundaryID and BoundaryType.
	 * @return Boundary.
	 */
	Boundary getBoundary(short bndryNum, Integer parentBoundaryID, BoundaryType boundaryType);

	Boundary getBoundary(BigInteger bndryNum, Integer parentBoundaryID, BoundaryType boundaryType);

	/**
	 * This method gets a single boundary which is at the top, (i.e this boundary instance has a boundary type of hierarchy level 1) and which has the given boundary name. This program assumes that with the given boundary name there should be only one instance of the top boundary type.
	 * @param Boundary name of the top level boundary.
	 * @return Boundary representing the top level Boundary type.
	 */

	Boundary getTopBoundary(String boundaryName, HierarchyType heirarchyType);

	/**
	 * Gets all the top boundaries as a set of boundary objects.
	 * @return Set
	 */
	List<Boundary> getTopBoundaries(HierarchyType heirarchyType);

	String getBoundaryNameForID(Integer id);

	Boundary getBoundary(int bndryID);

	Boundary getAllBoundaryById(int bndryID);

	Boundary getBoundaryById(int bndryID);

	List getAllLeafBoundariesForParentBndryIdAndChBType(Integer parentBoundaryId, String leafBTypeName);

	List<Integer> getBoundaryIdList(List<Boundary> boundaryObjList);

	Set<Boundary> getCrossHeirarchyParent(Boundary childBoundary);

	Set<Boundary> getCrossHeirarchyChildren(Boundary parentBoundary, BoundaryType childBoundaryType);
	
	List<Boundary> getBoundaryByNameLike(String name);

}
