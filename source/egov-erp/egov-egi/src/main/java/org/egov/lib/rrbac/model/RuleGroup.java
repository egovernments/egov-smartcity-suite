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
import java.util.HashSet;
import java.util.Set;

import org.egov.infra.admin.master.entity.Role;

public class RuleGroup implements Comparable<RuleGroup> {

	private Integer id;
	private String name;
	private Date updatedTime;
	private Set rules = new HashSet();
	private Role roleId;

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
	 * @return Returns the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the date
	 */
	public Date getUpdatedTime() {
		return updatedTime;
	}

	/**
	 * @param date The date and time to set
	 */
	public void setUpdatedTime(Date updatedtime) {
		this.updatedTime = updatedtime;
	}

	/**
	 * @param roleId To set role for a rulegroup
	 */
	public void setRoleId(Role roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return Return role
	 */
	public Role getRoleId() {
		return this.roleId;
	}

	/**
	 * @return set of rules for RuleGroup
	 */
	public Set getRules() {
		return rules;
	}

	/**
	 * @param rules To set rules for a rulegroup
	 */

	public void setRules(Set rules) {
		this.rules = rules;
	}

	/**
	 * @param rs To a rule to rules collection
	 */
	public void addRules(Rules rs) {
		getRules().add(rs);
	}

	/**
	 * @return Returns if the given Object is equal to RuleGroup
	 */
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (!(obj instanceof RuleGroup))
			return false;

		final RuleGroup other = (RuleGroup) obj;

		if (getName().equals(other.getName())) {
			return true;
		} else
			return false;
	}

	/**
	 * @return Returns the hashCode
	 */

	public int hashCode() {
		int hashCode = 0;
		hashCode = hashCode + this.getName().hashCode();

		return hashCode;
	}

	public int compareTo(RuleGroup ruleGrp) throws ClassCastException {
		
		return (ruleGrp == null || ruleGrp.getName() == null ) ? -1 : this.name.compareTo(ruleGrp.getName());
	}

}