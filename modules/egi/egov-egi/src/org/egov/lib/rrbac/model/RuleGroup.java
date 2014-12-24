/*
 * @(#)RuleGroup.java 3.0, 14 Jun, 2013 4:43:57 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.lib.rjbac.role.Role;

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