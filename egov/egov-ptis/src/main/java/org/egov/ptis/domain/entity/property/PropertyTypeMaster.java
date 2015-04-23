/*
 * PropertyType Created on Dec 15, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.entity.property;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.models.BaseModel;

/**
 * This class defines Property Type i.e A Property is linked to a PropertyType
 * indicating its current Type. Property Type can be Flat, Apartment, Duplex
 * etc.
 * 
 * @author Neetu
 * @version 2.00
 */
public class PropertyTypeMaster extends BaseModel {

	private String type;
	private Float factor;
	private String code;
	private Integer orderNo;

	/**
	 * @return Returns if the given Object is equal to PropertyUsage
	 */
	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (this == that)
			return true;

		if (that.getClass() != this.getClass())
			return false;
		final PropertyTypeMaster propertyTypeMaster = (PropertyTypeMaster) that;

		if (this.getId() != null && propertyTypeMaster.getId() != null) {
			if (getId().equals(propertyTypeMaster.getId())) {
				return true;
			} else
				return false;
		} else if (this.getType() != null && propertyTypeMaster.getType() != null) {
			if (getType().equals(propertyTypeMaster.getType())) {
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
			hashCode += this.getId().hashCode();
		}

		if (this.getType() != null) {
			hashCode += this.getType().hashCode();
		}

		return hashCode;
	}

	/**
	 * @return Returns the boolean after validating the current object
	 */
	public boolean validatePropTypeMstr() {
		if (getType() == null)
			throw new EGOVRuntimeException(
					"In PropertyTypeMaster Validate :Attribute 'Type' is not set, Please Check !!");

		if (getFactor() == null)
			throw new EGOVRuntimeException(
					"In PropertyTypeMaster Validate :Attribute 'Factor' is not set, Please Check !!");

		if (getFactor() == 0)
			throw new EGOVRuntimeException("In PropertyTypeMaster Validate :Attribute 'Factor' = 0, Please Check !!");
		if (getType() == null)
			throw new EGOVRuntimeException(
					"In PropertyTypeMaster Validate :Attribute 'Type' is not set, Please Check !!");

		return true;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Float getFactor() {
		return factor;
	}

	public void setFactor(Float factor) {
		this.factor = factor;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getId()).append("|Type: ").append(getType()).append("|Factor: ").append(
				getFactor());

		return objStr.toString();
	}

}
