/*
 * @(#)BoundaryType.java 3.0, 16 Jun, 2013 3:16:40 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry;

import java.io.Serializable;
import java.util.Set;

public interface BoundaryType extends Serializable {

	/**
	 * This method sets the pk of the boundary type.
	 * @param int as boundary type pk.
	 */

	void setId(Integer id);

	/**
	 * This method returns the PK of the boundary type.
	 * @return int as boundary type pk.
	 */

	Integer getId();

	/**
	 * This method returns the name of the boundary type. 
	 * For example it might be 'WARD' or 'COUNTRY'!! ETC.
	 * @return String as boundary type name.
	 */
	String getName();

	/**
	 * This method sets the name of the boundary type. For example it might be 'WARD' 
	 * or 'COUNTRY'!!.
	 * @param String as boundary type name.
	 */
	void setName(String name);

	/**
	 * This method returns its parent boundary type, if the return value is null, it means this is the top boundary type represented in the hierarchy.
	 * @return Parent Boundary type.
	 */
	BoundaryType getParent();

	/**
	 * This method sets its parent boundary type.
	 * @param Parent Boundary type.
	 */
	void setParent(BoundaryType bndryType);

	/**
	 * This method gets the hierarchy of the boundary type.
	 * @return short as boundary hierarchy level.
	 */
	short getHeirarchy();

	/**
	 * This method sets the hierarchy of the boundary type.
	 * @param short as boundary hierarchy level.
	 */
	void setHeirarchy(short arg1);

	/**
	 * This method gets the child boundary type.
	 * @return BoundaryType.
	 */

	Set getChildBoundaryTypes();

	/**
	 * This method sets the child boundary type.
	 * @param BoundaryType.
	 */
	void setChildBoundaryTypes(Set childBndryTypes);

	/**
	 * This method sets the child boundary type.
	 * @param BoundaryType.
	 */
	void addChildBoundaryType(BoundaryType bndryType);

	void setHeirarchyType(HeirarchyType heirarchyType);

	HeirarchyType getHeirarchyType();

	/**
	 * @return Returns the parentName.
	 */
	String getParentName();

	/**
	 * @param parentName The parentName to set.
	 */
	void setParentName(String parentName);

	/**
	 * @return Returns the BndryNameLocal.
	 */
	String getBndryTypeLocal();

	/**
	 * @param bndryNameLocal The bndryNameLocal to set.
	 */
	void setBndryTypeLocal(String bndryTypeLocal);

}