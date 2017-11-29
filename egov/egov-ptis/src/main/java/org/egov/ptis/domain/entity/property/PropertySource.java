/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */


package org.egov.ptis.domain.entity.property;

import org.egov.infra.exception.ApplicationRuntimeException;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * This class defines Property Source i.e Source from which Property got entered
 * in to the system. PropertySource can be SAS, PRF etc.
 * </p>
 * 
 * @author Gayathri Joshi
 * @version 2.00
 * @since 2.00
 */
public class PropertySource implements Serializable {

	private Integer ID = null;

	private String name = null;

	private Date lastUpdatedTimeStamp = null;

	private String propSrcCode = null;

	/**
	 * @return Returns the iD.
	 */
	public Integer getID() {
		return ID;
	}

	/**
	 * @param id
	 *            The iD to set.
	 */
	public void setID(Integer id) {
		ID = id;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the lastUpdatedTimeStamp.
	 */
	public Date getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	/**
	 * @param lastUpdatedTimeStamp
	 *            The lastUpdatedTimeStamp to set.
	 */
	public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}

	/**
	 * @return the propSrcCode
	 */
	public String getPropSrcCode() {
		return propSrcCode;
	}

	/**
	 * @param propSrcCode
	 *            the propSrcCode to set
	 */
	public void setPropSrcCode(String propSrcCode) {
		this.propSrcCode = propSrcCode;
	}

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
		final PropertySource thatPropSrc = (PropertySource) that;

		if (this.getID() != null && thatPropSrc.getID() != null) {
			if (getID().equals(thatPropSrc.getID())) {
				return true;
			} else
				return false;
		} else if (this.getName() != null && thatPropSrc.getName() != null) {
			if (getName().equals(thatPropSrc.getName())) {
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
		if (this.getID() != null) {
			hashCode += this.getID().hashCode();
		}
		if (this.getName() != null) {
			hashCode += this.getName().hashCode();
		}

		return hashCode;
	}

	/**
	 * @return Returns the boolean after validating the current object
	 */
	public boolean validate() {
		if (getName() == null)
			throw new ApplicationRuntimeException(
					"In PropertySource Validate :'Source Name' Attribute is no set, Please Check !!");

		return true;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getID()).append("|Name: ").append(getName());

		return objStr.toString();
	}
}
