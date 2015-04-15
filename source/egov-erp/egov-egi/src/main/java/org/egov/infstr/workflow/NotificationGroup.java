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
package org.egov.infstr.workflow;

import java.util.Date;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.infstr.models.BaseModel;
import org.egov.pims.commons.Position;
import org.hibernate.validator.constraints.Length;

public class NotificationGroup extends BaseModel {

	private static final long serialVersionUID = 1L;

	@Length(min = 1, max = 100, message = "err.grpname")
	private String groupName;
	@Length(max = 250, message = "err.grpdesc")
	private String groupDesc;
	@NotNull(message = "err.effdt")
	private Date effectiveDate;
	private char active;
	@Valid
	private Set<Position> members;

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the groupDesc
	 */
	public String getGroupDesc() {
		return groupDesc;
	}

	/**
	 * @param groupDesc the groupDesc to set
	 */
	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	/**
	 * @return the effectiveDate
	 */
	public Date getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * @return the members
	 */
	public Set<Position> getMembers() {
		return members;
	}

	/**
	 * @param members the members to set
	 */
	public void setMembers(Set<Position> members) {
		this.members = members;
	}

	/**
	 * @return the active
	 */
	public char getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(char active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return this.groupName;
	}
}
