/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */


package org.egov.ptis.domain.entity.property;

import org.egov.infra.exception.ApplicationRuntimeException;
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
			throw new ApplicationRuntimeException(
					"In PropertyTypeMaster Validate :Attribute 'Type' is not set, Please Check !!");

		if (getFactor() == null)
			throw new ApplicationRuntimeException(
					"In PropertyTypeMaster Validate :Attribute 'Factor' is not set, Please Check !!");

		if (getFactor() == 0)
			throw new ApplicationRuntimeException("In PropertyTypeMaster Validate :Attribute 'Factor' = 0, Please Check !!");
		if (getType() == null)
			throw new ApplicationRuntimeException(
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
