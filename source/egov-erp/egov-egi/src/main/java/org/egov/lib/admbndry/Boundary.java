/*
 * @(#)Boundary.java 3.0, 7 Jun, 2013 9:21:02 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

import org.egov.infra.admin.master.entity.BoundaryType;

public interface Boundary extends Serializable {

	/**
	 * @return the fromDate
	 */
	Date getFromDate();

	/**
	 * @param fromDate the fromDate to set
	 */
	void setFromDate(Date fromDate);

	/**
	 * @return the toDate
	 */
	Date getToDate();

	/**
	 * @param toDate the toDate to set
	 */
	void setToDate(Date toDate);

	/**
	 * This method returns its PK,
	 * @return int PK.
	 */
	Integer getId();

	/**
	 * This method sets its PK,
	 * @param int PK.
	 */
	void setId(Integer id);

	/**
	 * This method returns its parent boundary, if the return value is null, it means this is the top boundary 
	 * represented in the hierarchy.
	 * @return Parent Boundary.
	 */
	Boundary getParent();

	/**
	 * This method sets its parent boundary.
	 * @param ParentBoundary.
	 */
	void setParent(Boundary bndry);

	/**
	 * This method returns a boolean value indicating whether it has any parent at all. So any client suppose 
	 * to invoke getParent() in a loop for example might check with this method and then proceed.
	 * @return boolean.
	 */
	boolean isRoot();

	/**
	 * This method returns all its children boundaries as set, if the return set has empty values it means 
	 * this is the last boundary represented in this hierarchy.
	 * @return Parent Boundary.
	 */
	Set<Boundary> getChildren();

	/**
	 * This method returns a boolean value indicating whether it has any childern at all. So any client 
	 * suppose to invoke getChildren() in a loop for example might check with this method and then proceed.
	 * @return boolean.
	 */
	boolean isLeaf();

	/**
	 * This method returns the name of the boundary. For example it might be 'shivajiNgrWard', or 'India'!!.
	 * @return String as boundary name.
	 */
	String getName();

	/**
	 * This method sets the name of the boundary. For example it might be 'shivajiNgrWard', or 'India'!!.
	 * @param String as boundary name.
	 */
	void setName(String name);

	/**
	 * This method returns the boundary type this boundary is representing. For example it might be a 
	 * boundaryType of type 'WARD' OR 'COUNTRY' excetra.
	 * @return BoundaryType interface.
	 */
	BoundaryType getBoundaryType();

	/**
	 * This method sets the boundary type, for this boundary. For example it might be a boundaryType of type 
	 * 'WARD' OR 'COUNTRY' etc.
	 * @param BoundaryType interface.
	 */
	void setBoundaryType(BoundaryType boundaryType);

	/**
	 * This method returns the boundary number this boundary is representing, if any.
	 * @return int boundary number.
	 */
	BigInteger getBoundaryNum();

	/**
	 * This method sets the boundary number this boundary is representing, if any.
	 * @param int boundary number.
	 */
	void setBoundaryNum(BigInteger number);

	/**
	 * This method adds the child boundary to this boundary's set of children.
	 * @param boundary.
	 */
	void addChild(Boundary arg1);

	/**
	 * This method deletes the indicated child boundary from this boundary's set of children.
	 * @param boundary.If the child is not found, the method simply returns.
	 */
	void deleteChild(Boundary arg1);

	/**
	 * This method adds the given set of child boundaries to this boundary's set of children.
	 * @param Set containing boundary objects.
	 */
	void addChildren(Set<Boundary> arg1);

	/**
	 * This method sets the given set of child boundaries.
	 * @param Setcontaining boundary objects.
	 */
	void setChildren(Set<Boundary> arg1);

	/**
	 * This method deletes the indicated set of child boundaries from this boundary's set of children. 
	 * If the child is not found, the method simply returns.
	 * @param Setcontaining boundary objects.
	 */
	void deleteChildren(Set<Boundary> arg1);

	/**
	 * @return Returns the parentBoundaryNum.
	 */
	BigInteger getParentBoundaryNum();

	/**
	 * @param parentBoundaryNum The parentBoundaryNum to set.
	 */
	void setParentBoundaryNum(BigInteger parentBoundaryNum);

	/**
	 * @return Returns the topLevelBoundaryID.
	 */
	Integer getTopLevelBoundaryID();

	/**
	 * @param topLevelBoundaryID The topLevelBoundaryID to set.
	 */
	void setTopLevelBoundaryID(Integer topLevelBoundaryID);

	Integer getBndryTypeHeirarchyLevel();

	/**
	 * @param bndryTypeHeirarchyLevel The bndryTypeHeirarchyLevel to set.
	 */
	void setBndryTypeHeirarchyLevel(Integer bndryTypeHeirarchyLevel);

	Character getIsHistory();

	void setIsHistory(Character isHistory);

	Integer getBndryId();

	void setBndryId(Integer bndryId);

	String getBndryNameLocal();

	void setBndryNameLocal(String bndryNameLocal);

	void setUpdatedTime(Date updatedTime);

	Date getUpdatedTime();

	void setLng(Float lng);

	Float getLng();

	void setLat(Float lat);

	Float getLat();

	/**
	 * Materialized Path to get
	 */
	String getMaterializedPath();

	/**
	 * Materialized Path to set
	 */
	void setMaterializedPath(String materializedPath);
}
