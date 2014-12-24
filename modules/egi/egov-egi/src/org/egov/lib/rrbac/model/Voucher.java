/*
 * @(#)Voucher.java 3.0, 14 Jun, 2013 5:05:15 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.model;

import java.util.HashSet;
import java.util.Set;

public class Voucher implements AmountRuleData, FundRuleData {
	private Integer minRange = 100;
	private Integer maxRange = 20000;
	private HashSet hs = new HashSet();
	private String str = "General";

	public static void main(String a[]) {

	}

	public Integer getMinRange() {
		return minRange;
	}

	public void setMinRange(Integer minRange) {
		this.minRange = minRange;
	}

	public Integer getMaxRange() {
		return maxRange;
	}

	public void setMaxRange(Integer maxRange) {
		this.maxRange = maxRange;
	}

	public void setIeList(Set hs) {
		this.hs = (HashSet) hs;
	}

	public Set getIeList() {
		hs.add(str);
		return hs;

	}

}
