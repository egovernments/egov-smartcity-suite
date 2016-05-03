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

import java.util.Date;

/**
 * <p>
 * This class defines Property Occupation i.e how is a Property been occupied
 * PropertyOccupation can be Self Occupied, Tenanted etc.
 * </p>
 * 
 * @author Gayathri Joshi
 * @version 2.00
 * @since 2.00
 */
public class PropertyOccupation extends BaseModel {

	private String occupation;
	private Float occupancyFactor;
	private String occupancyCode;
	private Date fromDate;
	private Date toDate;
	private PropertyUsage propertyUsage;

	/**
	 * @return Returns if the given Object is equal to PropertyOccupation
	 */
	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (this == that)
			return true;
		if (that.getClass() != this.getClass())
			return false;
		final PropertyOccupation thatPropOcc = (PropertyOccupation) that;

		if (this.getId() != null && thatPropOcc.getId() != null) {
			if (getId().equals(thatPropOcc.getId())) {
				return true;
			} else
				return false;
		} else if (this.getOccupation() != null && thatPropOcc.getOccupation() != null) {
			if (getOccupation().equals(thatPropOcc.getOccupation())) {
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
		} else if (this.getOccupation() != null) {
			hashCode += this.getOccupation().hashCode();
		}

		return hashCode;
	}

	/**
	 * @return Returns the boolean after validating the current object
	 */
	public boolean validatePropOccupation() {
		if (getOccupation() == null)
			throw new ApplicationRuntimeException(
					"In PropertyOccupation Validate :'Occupation' Attribute is no set, Please Check !!");
		if (getOccupancyFactor() == null)
			throw new ApplicationRuntimeException(
					"In PropertyOccupation Validate :'Occupancy Factor' Attribute is no set, Please Check !!");

		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Id: ").append(getId()).append("Occupation: ").append(occupation).append("|OccupFactor").append(
				occupancyFactor).append("|PropertyUsage").append(propertyUsage);

		return sb.toString();
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public Float getOccupancyFactor() {
		return occupancyFactor;
	}

	public void setOccupancyFactor(Float occupancyFactor) {
		this.occupancyFactor = occupancyFactor;
	}

	public String getOccupancyCode() {
		return occupancyCode;
	}

	public void setOccupancyCode(String occupancyCode) {
		this.occupancyCode = occupancyCode;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public PropertyUsage getPropertyUsage() {
		return propertyUsage;
	}

	public void setPropertyUsage(PropertyUsage propertyUsage) {
		this.propertyUsage = propertyUsage;
	}

}
