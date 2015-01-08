/*
 * @(#)AccountCodeRuleData.java 3.0, 14 Jun, 2013 4:30:01 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.model;

import java.util.Set;

public interface AccountCodeRuleData extends RuleData {

	/**
	 * @return Returns collection of objects of type IEList, which gives 
	 * include and exclude list for the Rule
	 */
	Set getIeList();

	/**
	 * To set include and exclude list for the Rule
	 * @param ieList
	 */
	void setIeList(Set ieList);

}
