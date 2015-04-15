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