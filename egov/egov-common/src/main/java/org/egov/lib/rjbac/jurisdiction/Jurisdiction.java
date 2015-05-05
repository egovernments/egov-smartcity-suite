/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.lib.rjbac.jurisdiction;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.User;

public class Jurisdiction implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id = null;
	private BoundaryType jurisdictionLevel = null;
	private Set<JurisdictionValues> jurisdictionValues = new HashSet();
	private User user = null;
	private Date updateTime;

	/**
	 * @return Returns the jurisdictionLevel.
	 */
	public BoundaryType getJurisdictionLevel() {
		return jurisdictionLevel;
	}

	/**
	 * @param jurisdictionLevel The jurisdictionLevel to set.
	 */
	public void setJurisdictionLevel(BoundaryType jurisdictionLevel) {
		this.jurisdictionLevel = jurisdictionLevel;
	}

	/**
	 * @return Returns the jurisdictionValues.
	 */
	public Set getJurisdictionValues() {
		return jurisdictionValues;
	}

	/**
	 * @param jurisdictionValues The jurisdictionValues to set.
	 */
	public void setJurisdictionValues(Set jurisdictionValues) {
		this.jurisdictionValues = jurisdictionValues;
	}

	public void addJurisdictionValue(JurisdictionValues bndry) {
		getJurisdictionValues().add(bndry);
	}

	public void removeJurisdictionValue(JurisdictionValues bndry) {
		if (this.jurisdictionValues.contains(bndry))
			this.jurisdictionValues.remove(bndry);
	}

	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return Returns the user.
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user The user to set.
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return Returns the updateTime.
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime The updateTime to set.
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Jurisdiction)) {
			return false;
		}

		final Jurisdiction other = (Jurisdiction) obj;
		if (other.getUser().equals(getUser())) {
			if (other.getJurisdictionLevel().equals(getJurisdictionLevel())) {
				return true;

			} else {
				return false;
			}

		} else {
			return false;
		}

	}

	public int hashCode() {
		int hashCode = 0;
		if (this.getUser() != null) {
			hashCode = hashCode + this.getUser().hashCode();
		}

		if (this.getJurisdictionLevel() != null) {
			hashCode = hashCode + this.getJurisdictionLevel().hashCode();
		}

		return hashCode;

	}

	public boolean validate() {
		BoundaryType bt = this.jurisdictionLevel;
		Set bndries = this.jurisdictionValues;
		for (Iterator iter = bndries.iterator(); iter.hasNext();) {
			JurisdictionValues element = (JurisdictionValues) iter.next();
			// System.out.println("element.getBoundaryType()"+element.getBoundaryType());
			if (!element.getBoundary().getBoundaryType().equals(bt))
				throw new EGOVRuntimeException("Invalid Boundary " + element.getBoundary().getName() + " for Boundary Type " + bt.getName() + ".");
		}

		return true;
	}
}
