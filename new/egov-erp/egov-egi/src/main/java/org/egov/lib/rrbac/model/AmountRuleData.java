/*
 * @(#)AmountRuleData.java 3.0, 14 Jun, 2013 4:36:50 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.model;

public interface AmountRuleData extends RuleData {

	/**
	 * @return Returns minimum amount for the rule
	 */
	Integer getMinRange();

	/**
	 * @param minRange To set minimum amount
	 */
	void setMinRange(Integer minRange);

	/**
	 * @return Returns maximum amount for the rule
	 */
	Integer getMaxRange();

	/**
	 * @param maxRange To set maximum amount
	 */
	void setMaxRange(Integer maxRange);
}