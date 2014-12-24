/*
 * @(#)Accountdetailkey.java 3.0, 6 Jun, 2013 2:34:35 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

public class Accountdetailkey implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private CChartOfAccounts chartofaccounts;
	private Integer groupid;
	private String detailname;
	private Integer detailkey;
	private Accountdetailtype accountdetailtype;

	/**
	 * @return Returns the chartofaccounts.
	 */
	public CChartOfAccounts getChartofaccounts() {
		return chartofaccounts;
	}

	/**
	 * @param chartofaccounts The chartofaccounts to set.
	 */
	public void setChartofaccounts(CChartOfAccounts chartofaccounts) {
		this.chartofaccounts = chartofaccounts;
	}

	/**
	 * @return Returns the detailkey.
	 */
	public Integer getDetailkey() {
		return detailkey;
	}

	/**
	 * @param detailkey The detailkey to set.
	 */
	public void setDetailkey(Integer detailkey) {
		this.detailkey = detailkey;
	}

	/**
	 * @return Returns the detailname.
	 */
	public String getDetailname() {
		return detailname;
	}

	/**
	 * @param detailname The detailname to set.
	 */
	public void setDetailname(String detailname) {
		this.detailname = detailname;
	}

	/**
	 * @return Returns the groupid.
	 */
	public Integer getGroupid() {
		return groupid;
	}

	/**
	 * @param groupid The groupid to set.
	 */
	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}

	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return Returns the accountdetailtype.
	 */
	public Accountdetailtype getAccountdetailtype() {
		return accountdetailtype;
	}

	/**
	 * @param accountdetailtype The accountdetailtype to set.
	 */
	public void setAccountdetailtype(Accountdetailtype accountdetailtype) {
		this.accountdetailtype = accountdetailtype;
	}
}
