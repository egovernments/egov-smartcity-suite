/*
 * @(#)AmountRule.java 3.0, 14 Jun, 2013 4:36:24 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.model;

import java.util.Date;

import org.egov.exceptions.AmountException;

public class AmountRule implements Rules, AmountRuleData {

	private Integer id;
	private String name;
	private Date updatedTime;
	private String defaultValue;
	private String type;
	private Integer minRange;
	private Integer maxRange;
	private Integer active;

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
	 * @param updatedtime the date to set
	 */
	public void setUpdatedTime(Date updatedtime) {
		this.updatedTime = updatedtime;
	}

	/**
	 * @return Returns the default
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue The default value to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return Returns minimum amount for the rule
	 */
	public Integer getMinRange() {
		return minRange;
	}

	/**
	 * @param minRange To set minimum amount
	 */
	public void setMinRange(Integer minRange) {
		this.minRange = minRange;
	}

	/**
	 * @return Returns maximum amount for the rule
	 */
	public Integer getMaxRange() {
		return maxRange;
	}

	/**
	 * @param maxRange To set maximum amount
	 */
	public void setMaxRange(Integer maxRange) {
		this.maxRange = maxRange;
	}

	/**
	 * @return Returns 1 if active or 0 if inactive
	 */
	public Integer getActive() {
		return active;
	}

	/**
	 * @param type To set type of Rule(AmountRule/FundRule/AccountCodeRule)
	 */
	public void setActive(Integer acti) {
		this.active = acti;
	}

	/**
	 * @return Returns type of rule
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type To set type of Rule(AmountRule/FundRule/AccountCodeRule)
	 */
	public void setType(String type) {
		this.type = type;
	}

	/*
	 * public RuleGroup getRuleGroup() { return ruleGroup; } public void setRuleGroup(RuleGroup rg) { this.ruleGroup=rg; }
	 */
	/**
	 * @return Returns if the given Object is equal to Amountrule
	 */
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (!(obj instanceof AmountRule))
			return false;

		final AmountRule other = (AmountRule) obj;

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
		if (this.getName() != null)
			hashCode = hashCode + this.getName().hashCode();

		return hashCode;
	}

	/**
	 * This method returns true if Rules are validated. implemenation of method is different for subclasses. An entity class implements an interface(which implements interface RuleData)
	 * @param rd
	 * @return boolean
	 */
	public boolean isValid(RuleData rd) throws AmountException {
		boolean retVal = true;
		int val = (this.getActive()).intValue();
		if (val != 0) {
			retVal = false;
			AmountRuleData amd = (AmountRuleData) rd;
			int amount = (amd.getMaxRange()).intValue();
			int minRuleAmount = (getMinRange()).intValue();
			int maxRuleAmount = (getMaxRange()).intValue();
			if (amount >= minRuleAmount && amount <= maxRuleAmount)
				retVal = true;
			if (retVal == false) {
				throw new AmountException("Passed Amount " + amount + " is out of specified range ");

			}

		}
		return retVal;
	}

}