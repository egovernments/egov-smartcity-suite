/*
 * @(#)IEList.java 3.0, 14 Jun, 2013 4:40:47 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.model;

import java.util.Date;

public class IEList {

	private Integer id;
	private Date updatedTime;
	private Rules ruleId;
	private String value;
	private String type;

	/**
	 * @return Returns the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id The id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return Returns id of rule
	 */
	public Rules getRuleId() {
		return ruleId;
	}

	/**
	 * @param ruleId To set Rule id
	 */
	public void setRuleId(Rules ruleId) {
		this.ruleId = ruleId;
	}

	/**
	 * @return Returns the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value To set the value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return Returns the UpdatedTime.
	 */
	public Date getUpdatedTime() {
		return updatedTime;
	}

	/**
	 * @param UpdatedTime The UpdatedTime to set.
	 */
	public void setUpdatedTime(Date updatedtime) {
		this.updatedTime = updatedtime;
	}

	/**
	 * @return Returns type (include/exclude)
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type To set type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return Returns if the given Object is equal to IEList
	 */
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (!(obj instanceof IEList))
			return false;

		final IEList other = (IEList) obj;

		if (this.getId() != null && other.getId() != null) {
			if (getId().toString().equals(other.getId().toString())) {
				return true;
			} else
				return false;
		} else
			return false;
	}

	/**
	 * @return Returns the hashCode
	 */
	public int hashCode() {
		int hashCode = 0;
		if (this.getId() != null) {
			hashCode = hashCode + this.getId().toString().hashCode();
		}
		return hashCode;

	}

}