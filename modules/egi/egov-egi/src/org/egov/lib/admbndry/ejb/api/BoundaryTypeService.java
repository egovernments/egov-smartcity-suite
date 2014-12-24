/*
 * @(#)BoundaryTypeService.java 3.0, 11 Jun, 2013 11:11:38 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry.ejb.api;

import java.util.List;

import org.egov.exceptions.NoSuchObjectException;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.HeirarchyType;

public interface BoundaryTypeService {

	/**
	 * This method creates the boundary type and also recursively creates the subsequent child boundary types.
	 * @param Boundary Type to be created.
	 */

	void createBoundaryType(BoundaryType bndryType);

	/**
	 * This method removes the boundary type and removes the coressponding instances of the boundaries. And also recursively removes the subsequent child boundary types and its boundary instances if there are any.
	 * @param Boundary type to be removed.
	 */
	void removeBoundaryType(BoundaryType bndryType);

	/**
	 * This method updates the boundary type and also recursively updates the subsequent child boundary types if there are any changes made in that.
	 * @param Boundary type to be updated.
	 */
	void updateBoundaryType(BoundaryType bndryType);

	/**
	 * This returns the boundaryType of the given hierarchy level.
	 * @param heirarchylevel
	 * @return BoundaryType
	 */
	BoundaryType getBoundaryType(short heirarchylevel, HeirarchyType heirarchyType);

	/**
	 * This returns the top boundaryType i.e of hierarchy level 1.
	 * @return BoundaryType
	 */

	BoundaryType getTopBoundaryType(HeirarchyType heirarchyType);

	/**
	 * This returns the boundaryType of the given boundary type name.
	 * @param name of the boundary type
	 * @return BoundaryType
	 */
	BoundaryType getBoundaryType(String bndryTypeName, HeirarchyType heirarchyType);

	/**
	 * @param id
	 * @return
	 */
	BoundaryType getBoundaryType(Integer id);

	List<Boundary> getParentBoundaryList(Integer boundaryId) throws NoSuchObjectException;
	/**
	 * This returns list of parent boundary for the given boundary id.
	 * @param name of the boundaryId
	 * @return list of parent boundary.
	 */

}