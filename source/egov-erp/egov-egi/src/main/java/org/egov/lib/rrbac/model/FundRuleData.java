/*
 * @(#)FundRuleData.java 3.0, 14 Jun, 2013 4:39:29 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.model;

import java.util.Set;

public interface FundRuleData extends RuleData {

	/**
	 * @return Returns collection of objects of type IEList, which gives include and exclude list for the Rule
	 */
	Set getIeList();

	/**
	 * To set include and exclude list for the Rule
	 * @param ieList
	 */
	void setIeList(Set ieList);

}
