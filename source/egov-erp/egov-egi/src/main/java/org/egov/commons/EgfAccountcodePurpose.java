/*
 * @(#)EgfAccountcodePurpose.java 3.0, 6 Jun, 2013 3:19:58 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.util.HashSet;
import java.util.Set;

public class EgfAccountcodePurpose implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String name;

	private Set chartofaccountses = new HashSet(0);

	private Set funds = new HashSet(0);

	public EgfAccountcodePurpose() {
		//For hibernate to work
	}

	public EgfAccountcodePurpose(Integer id) {
		this.id = id;
	}

	public EgfAccountcodePurpose(Integer id, String name, Set chartofaccountses, Set funds) {
		this.id = id;
		this.name = name;
		this.chartofaccountses = chartofaccountses;
		this.funds = funds;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set getChartofaccountses() {
		return this.chartofaccountses;
	}

	public void setChartofaccountses(Set chartofaccountses) {
		this.chartofaccountses = chartofaccountses;
	}

	public Set getFunds() {
		return this.funds;
	}

	public void setFunds(Set funds) {
		this.funds = funds;
	}

}
