/*
 * @(#)HeirarchyTypeImpl.java 3.0, 16 Jun, 2013 3:47:08 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry;

import java.util.Date;

public class HeirarchyTypeImpl implements HeirarchyType {

	private static final long serialVersionUID = 1L;
	private String name;
	private Integer id;
	private Date updatedTime;
	private String code;

	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return Returns the updatedTime.
	 */
	public Date getUpdatedTime() {
		return updatedTime;
	}

	/**
	 * @param updatedTime The updatedTime to set.
	 */
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	/**
	 * 
	 */
	public HeirarchyTypeImpl() {
		super();

	}

	public HeirarchyTypeImpl(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.admbndry.HeirarchyType#getId()
	 */
	public Integer getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.admbndry.HeirarchyType#setId(int)
	 */
	public void setId(Integer id) {
		this.id = id;

	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.admbndry.HeirarchyType#setName()
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.admbndry.HeirarchyType#getName()
	 */
	public String getName() {
		return name;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof HeirarchyTypeImpl))
			return false;

		final HeirarchyType other = (HeirarchyType) obj;

		if (!(this.getName().equals(other.getName()))) {
			return false;
		} else {
			return true;
		}

	}

	public int hashCode() {
		int hashCode = 0;

		if (getName() != null)
			hashCode = getName().hashCode();

		return hashCode;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();
		objStr = (getId() != null) ? objStr.append("Id: ").append(getId()).append("|") : objStr.append("");
		objStr.append("Name: ").append(getName());

		return objStr.toString();
	}

}
