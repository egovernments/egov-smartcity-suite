/*
 * @(#)BoundaryImpl.java 3.0, 16 Jun, 2013 3:15:08 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry;

import org.egov.search.domain.Searchable;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class BoundaryImpl implements Boundary {

	private static final long serialVersionUID = 1L;
	private Date updatedTime;
	private Integer id;
	private BoundaryType myBoundaryType;
	private Boundary parentBndry;
	private Set children = new HashSet();
	private String name;
	private BigInteger number;
	private BigInteger parentBoundaryNum;
	private Integer topLevelBoundaryID;
	private Integer bndryTypeHeirarchyLevel;
	private Date fromDate;
	private Date toDate;
	private Character isHistory;
	private Integer bndryId;
	private String bndryNameLocal;
	private Float lng;
	private Float lat;
	private String materializedPath;

	public BoundaryImpl() {
		//FOr Hibernate
	}
	/**
	 * @return Returns the bndryNameLocal.
	 */
	public String getBndryNameLocal() {
		return bndryNameLocal;
	}

	/**
	 * @param bndryNameLocal The bndryNameLocal to set.
	 */
	public void setBndryNameLocal(String bndryNameLocal) {
		this.bndryNameLocal = bndryNameLocal;
	}

	public BoundaryImpl(BoundaryType boundaryType) {
		myBoundaryType = boundaryType;
	}

	public void setId(Integer id) {
		this.id = id;

	}

	public Integer getId() {
		return id;

	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;

	}

	public Date getUpdatedTime() {
		return updatedTime;

	}

	/*
	 * @see org.egov.lib.admbndry.Boundary#getParent()
	 */
	public Boundary getParent() {

		return parentBndry;
	}

	/*
	 * @see org.egov.lib.admbndry.Boundary#setParent()
	 */
	public void setParent(Boundary bndry) {
		this.parentBndry = bndry;
	}

	/*
	 * @see org.egov.lib.admbndry.Boundary#getChildren()
	 */
	public Set getChildren() {

		return children;
	}

	/*
	 * @see org.egov.lib.admbndry.Boundary#getName()
	 */
	public String getName() {

		return name;
	}

	/*
	 * @see org.egov.lib.admbndry.Boundary#setName()
	 */
	public void setName(String name) {

		this.name = name;
	}

	/*
	 * @see org.egov.lib.admbndry.Boundary#isLeaf()
	 */
	public boolean isLeaf() {
		return (getChildren().isEmpty());
	}

	/*
	 * @see org.egov.lib.admbndry.Boundary#getBoundaryType()
	 */
	public BoundaryType getBoundaryType() {
		return myBoundaryType;
	}

	/*
	 * @see org.egov.lib.admbndry.Boundary#getBoundaryNum()
	 */
	public BigInteger getBoundaryNum() {
		return number;
	}

	/*
	 * @see org.egov.lib.admbndry.Boundary#addChild(org.egov.lib.admbndry.Boundary)
	 */
	public void addChild(Boundary arg1) {
		arg1.setParent(this);
		children.add(arg1);
	}

	/*
	 * @see org.egov.lib.admbndry.Boundary#deleteChild(org.egov.lib.admbndry.Boundary )
	 */
	public void deleteChild(Boundary arg1) {
		children.remove(arg1);
	}

	/*
	 * @see org.egov.lib.admbndry.Boundary#addChildren(java.util.Set)
	 */
	public void addChildren(Set arg1) {
		children.addAll(arg1);

	}

	/*
	 * @see org.egov.lib.admbndry.Boundary#deleteChildren(java.util.Set)
	 */
	public void deleteChildren(Set arg1) {
		children.removeAll(arg1);
	}

	/*
	 * @see org.egov.lib.admbndry.Boundary#isRoot()
	 */
	public boolean isRoot() {
		return (getParent() == null ? true : false);
	}

	/*
	 * @see org.egov.lib.admbndry.Boundary#setBoundaryType(org.egov.lib.admbndry. BoundaryType)
	 */
	public void setBoundaryType(BoundaryType boundaryType) {
		myBoundaryType = boundaryType;
	}

	/*
	 * @see org.egov.lib.admbndry.Boundary#setBoundaryNum(int)
	 */
	public void setBoundaryNum(BigInteger number) {

		this.number = number;
	}

	/*
	 * @see org.egov.lib.admbndry.Boundary#setChildren(java.util.Set)
	 */
	public void setChildren(Set arg1) {
		children = arg1;

	}

	/**
	 * @return Returns the parentBoundaryNum.
	 */
	public BigInteger getParentBoundaryNum() {
		return parentBoundaryNum;
	}

	/**
	 * @param parentBoundaryNum The parentBoundaryNum to set.
	 */
	public void setParentBoundaryNum(BigInteger parentBoundaryNum) {
		this.parentBoundaryNum = parentBoundaryNum;
	}

	/**
	 * @return Returns the topLevelBoundaryID.
	 */
	public Integer getTopLevelBoundaryID() {
		return topLevelBoundaryID;
	}

	/**
	 * @param topLevelBoundaryID The topLevelBoundaryID to set.
	 */
	public void setTopLevelBoundaryID(Integer topLevelBoundaryID) {
		this.topLevelBoundaryID = topLevelBoundaryID;
	}

	/*
	 * * @return Returns the bndryTypeHeirarchyLevel.
	 */
	public Integer getBndryTypeHeirarchyLevel() {
		return bndryTypeHeirarchyLevel;
	}

	/**
	 * @param bndryTypeHeirarchyLevel The bndryTypeHeirarchyLevel to set.
	 */
	public void setBndryTypeHeirarchyLevel(Integer bndryTypeHeirarchyLevel) {
		this.bndryTypeHeirarchyLevel = bndryTypeHeirarchyLevel;
	}

	/**
	 * @return the fromDate
	 */
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the toDate
	 */
	public Date getToDate() {
		return toDate;
	}

	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	/**
	 * @return the isHistory
	 */
	public Character getIsHistory() {
		return isHistory;
	}

	/**
	 * @param isHistory the isHistory to set
	 */
	public void setIsHistory(Character isHistory) {
		this.isHistory = isHistory;
	}

	/**
	 * @return the bndryId
	 */
	public Integer getBndryId() {
		return bndryId;
	}

	/**
	 * @param bndryId the bndryId to set
	 */
	public void setBndryId(Integer bndryId) {
		this.bndryId = bndryId;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof BoundaryImpl))
			return false;
		final Boundary other = (Boundary) obj;

		if (this.getId() != null && this.getId().equals(other.getId()))
			return true;
		// Boundary number can be null
		if (this.getBoundaryNum() != null && !this.getBoundaryNum().equals(other.getBoundaryNum())) {
			return false;
		}
		if (!this.getBoundaryType().equals(other.getBoundaryType())) {
			return false;
		}
		if (this.getName() != null && !this.getName().equals(other.getName())) {
			return false;
		}
		if (this.getBndryNameLocal() != null && !(this.getBndryNameLocal().equals(other.getBndryNameLocal()))) {
			return false;
		}
		if (this.getParent() != null && !this.getParent().equals(other.getParent())) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		int hashCode = 0;

		// assumes boundary number is never null.
		if (getBoundaryNum() != null)
			hashCode = hashCode + getBoundaryNum().hashCode();

		// assumes boundary name is never null.
		if (getName() != null)
			hashCode = hashCode + getName().hashCode();

		// assumes boundary type name is never null.
		if (getBoundaryType() != null && getBoundaryType().getName() != null)
			hashCode = hashCode + getBoundaryType().getName().hashCode();

		// assumes top boundary id is never null.
		Integer i = getTopLevelBndryID(this);
		if (i != null)
			hashCode = hashCode + i.hashCode();

		return hashCode;

	}

	private Integer getTopLevelBndryID(Boundary bn) {
		Boundary localBndry = bn;
		if (localBndry == null)
			return null;

		while (localBndry.getParent() != null) {
			localBndry = localBndry.getParent();
		}

		return localBndry.getId();
	}

	public Float getLat() {
		return lat;
	}

	public Float getLng() {
		return lng;
	}

	public void setLat(Float lat) {
		this.lat = lat;

	}

	public void setLng(Float lng) {
		this.lng = lng;
	}

	/**
	 * Materialized Path to get
	 */
	public String getMaterializedPath() {
		return materializedPath;
	}

	/**
	 * Materialized Path to set
	 */
	public void setMaterializedPath(String materializedPath) {
		this.materializedPath = materializedPath;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr = (getId() != null) ? objStr.append("Id: ").append(getId().toString()).append("|") : objStr.append("");
		objStr.append("BoundaryType: ").append(getBoundaryType()).append("|Name: ").append(getName()).append("|Number: ").append(getBoundaryNum()).append("ParentBoundaryNum: ").append(getParentBoundaryNum()).append("|isHistory: ").append(getIsHistory())
				.append("|MaterializedPath: ").append(getMaterializedPath());

		return objStr.toString();
	}
}
