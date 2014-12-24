/*
 * @(#)Rules.java 3.0, 14 Jun, 2013 4:43:49 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.model;

import java.util.Date;

import org.egov.exceptions.RBACException;

public interface Rules {

	/**
	 * @return Returns the id
	 */
	Integer getId();

	/**
	 * @param id The id to set
	 */
	void setId(Integer id);

	/**
	 * @return Returns the name
	 */
	String getName();

	/**
	 * @param name The name to set
	 */
	void setName(String name);

	/**
	 * @return Returns the date
	 */
	Date getUpdatedTime();

	/**
	 * @param updatedtime the date to set
	 */
	void setUpdatedTime(Date updatedtime);

	/**
	 * @return Returns the default
	 */
	String getDefaultValue();

	/**
	 * @param defaultValue The default value to set
	 */
	void setDefaultValue(String defaultValue);

	/**
	 * @param acti To specify whether rule is active or not
	 */
	void setActive(Integer acti);

	/**
	 * @return Returns 1 if active or 0 if inactive
	 */
	Integer getActive();

	/**
	 * @param type To set type of Rule(AmountRule/FundRule/AccountCodeRule)
	 */
	void setType(String type);

	/**
	 * @return Returns type of rule
	 */
	String getType();

	/**
	 * This method returns true if Rules are validated. implemenation of method is different for subclasses. An entity class implements an interface(which implements interface RuleData) The rule can have either include or exclude condition not both
	 * @param rd
	 * @return boolean
	 */
	boolean isValid(RuleData rd) throws RBACException;
}