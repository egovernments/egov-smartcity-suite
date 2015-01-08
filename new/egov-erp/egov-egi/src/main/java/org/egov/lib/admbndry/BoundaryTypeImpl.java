/*
 * @(#)BoundaryTypeImpl.java 3.0, 16 Jun, 2013 3:29:26 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry;

import java.util.Date;
import java.util.Set;

public class BoundaryTypeImpl implements BoundaryType {

	private static final long serialVersionUID = 1L;
	private Date updatedTime;
	private Integer id;
	private BoundaryType parentBndryType;
	private Set childBndryTypes;
	private String name;
	private String parentName;
	private String bndryTypeLocal;
	private short hierarchy;
	private HeirarchyType heirarchyType;

	public BoundaryTypeImpl() {
	}

	public BoundaryTypeImpl(BoundaryType parent) {
		parent.addChildBoundaryType(this);
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

	public void setHierarchy(short hierarchy) {

		this.hierarchy = hierarchy;
	}

	public short getHierarchy() {
		return hierarchy;

	}

	/**
	 * * @see org.egov.lib.admbndry.BoundaryType#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * * @see org.egov.lib.admbndry.BoundaryType#setName()
	 */
	public void setName(String name) {
		this.name = name;

	}

	/**
	 * * @see org.egov.lib.admbndry.BoundaryType#getParent()
	 */
	public BoundaryType getParent() {
		return parentBndryType;
	}

	/**
	 * * @see org.egov.lib.admbndry.BoundaryType#getHeirarchy()
	 */
	public short getHeirarchy() {
		return hierarchy;
	}

	/**
	 * * @see org.egov.lib.admbndry.BoundaryType#setHeirarchy(short)
	 */
	public void setHeirarchy(short arg1) {
		hierarchy = arg1;
	}

	/**
	 * * @see org.egov.lib.admbndry.BoundaryType#setParent(org.egov.lib.admbndry. BoundaryType)
	 */
	public void setParent(BoundaryType bndryType) {
		this.parentBndryType = bndryType;
	}

	/**
	 * * @see org.egov.lib.admbndry.BoundaryType#getChildBoundaryType()
	 */
	public Set getChildBoundaryTypes() {
		return childBndryTypes;
	}

	/**
	 * * @see org.egov.lib.admbndry.BoundaryType#setChildBoundaryType()
	 */
	public void setChildBoundaryTypes(Set childBndryTypes) {
		this.childBndryTypes = childBndryTypes;

	}

	public void addChildBoundaryType(BoundaryType bndryType) {
		bndryType.setParent(this);
		this.childBndryTypes.add(bndryType);
	}

	/**
	 * * @see org.egov.lib.admbndry.BoundaryType#setHeirarchyType(org.egov.lib. admbndry.HeirarchyType)
	 */
	public void setHeirarchyType(HeirarchyType heirarchyType) {
		this.heirarchyType = heirarchyType;
	}

	/***
	 * @see org.egov.lib.admbndry.BoundaryType#getHeirarchyType()
	 */
	public HeirarchyType getHeirarchyType() {
		return heirarchyType;
	}

	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (!(obj instanceof BoundaryTypeImpl))
			return false;

		final BoundaryType other = (BoundaryType) obj;

		if (!(this.getHeirarchyType().equals(other.getHeirarchyType()))) {
			return false;
		} else if (!(this.getHeirarchy() == other.getHeirarchy())) {
			return false;
		} else if (!(this.getName().equals(other.getName()))) {
			return false;
		} else {
			return true;
		}
	}

	public int hashCode() {
		int hashCode = 0;

		if (getHeirarchyType() != null && getHeirarchyType().getName() != null)
			hashCode = hashCode + getHeirarchyType().getName().hashCode();

		// assumes boundary name is never null.
		hashCode = hashCode + Integer.valueOf(getHeirarchy()).hashCode();

		// assumes boundary type name is never null.
		if (getName() != null)
			hashCode = hashCode + getName().hashCode();

		// assumes top boundary id is never null.
		if (getParent() != null)
			hashCode = hashCode + getParent().hashCode();

		return hashCode;

	}

	/**
	 * @return Returns the parentName.
	 */
	public String getParentName() {
		return parentName;
	}

	/**
	 * @param parentName The parentName to set.
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	/**
	 * @return the bndryTypeLocal
	 */
	public String getBndryTypeLocal() {
		return bndryTypeLocal;
	}

	/**
	 * @param bndryTypeLocal the bndryTypeLocal to set
	 */
	public void setBndryTypeLocal(String bndryTypeLocal) {
		this.bndryTypeLocal = bndryTypeLocal;
	}

	public BoundaryTypeImpl getFirstChild() {
		for (Object child : this.childBndryTypes) {
			return (BoundaryTypeImpl) child;
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();
		objStr = (getId() != null) ? objStr.append("Id: ").append(getId()).append("|") : objStr.append("");
		objStr.append("BoundaryType: ").append(getParent()).append("|Name: ").append(getName()).append("|ParentName: ").append(getParentName());

		return objStr.toString();
	}
}