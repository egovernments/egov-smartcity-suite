/*
 * ConstructionTypeImpl.java Created on Nov 08, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.entity.property;

import org.egov.infstr.models.BaseModel;

/**
 * <p>
 * This class gives the implementation for ConstructionType. A building is
 * constructed of Floor, Wall, Roof, Wood etc. Each of them represents a
 * ConstructionType.
 * </p>
 * 
 * @author Gayathri Joshi
 * @version 2.00
 * @see org.egov.ptis.domain.entity.property.ConstructionType
 * @since 2.00
 */
public class ConstructionTypeImpl extends BaseModel implements ConstructionType {
	private String type;
	private String name;
	private String code;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return Returns if the given Object is equal to ConstructionTypeImpl
	 */
	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (this == that)
			return true;

		if (that.getClass() != this.getClass())
			return false;
		final ConstructionTypeImpl thatConstrType = (ConstructionTypeImpl) that;

		if (getId() != null && thatConstrType.getId() != null) {
			if (getId().equals(thatConstrType.getId())) {
				return true;
			} else
				return false;
		}
		if (getCode() != null && thatConstrType.getCode() != null) {
			if (getCode().equals(thatConstrType.getCode())) {
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
		if (getId() != null) {
			hashCode += this.getId().hashCode();
		}
		if (getCode() != null) {
			hashCode += this.getCode().hashCode();
		}
		return hashCode;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getId()).append("|Type: ").append(getType()).append("|Name: " + getName());

		return objStr.toString();
	}

}