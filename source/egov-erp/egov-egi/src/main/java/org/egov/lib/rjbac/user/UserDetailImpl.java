/*
 * @(#)UserDetailImpl.java 3.0, 15 Jun, 2013 11:39:09 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.user;

import java.util.Date;

public class UserDetailImpl extends UserImpl implements UserDetail {
	private Integer id;
	private Integer bankBranchId;
	private String extrafield1;
	private String extrafield2;
	private String extrafield3;
	private Date dob;
	private String locale;
	private Integer empId;

	/**
	 * @return Returns the dob.
	 */
	public Date getDob() {
		return dob;
	}

	/**
	 * @param dob The dob to set.
	 */
	public void setDob(Date dob) {
		this.dob = dob;
	}

	/**
	 * @return Returns the empId.
	 */
	public Integer getEmpId() {
		return empId;
	}

	/**
	 * @param empId The empId to set.
	 */
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	/**
	 * @return Returns the locale.
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale The locale to set.
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * @return Returns the extrafield1.
	 */
	public String getExtrafield1() {
		return extrafield1;
	}

	/**
	 * @param extrafield1 The extrafield1 to set.
	 */
	public void setExtrafield1(String extrafield1) {
		this.extrafield1 = extrafield1;
	}

	/**
	 * @return Returns the extrafield2.
	 */
	public String getExtrafield2() {
		return extrafield2;
	}

	/**
	 * @param extrafield2 The extrafield2 to set.
	 */
	public void setExtrafield2(String extrafield2) {
		this.extrafield2 = extrafield2;
	}

	/**
	 * @return Returns the extrafield3.
	 */
	public String getExtrafield3() {
		return extrafield3;
	}

	/**
	 * @param extrafield3 The extrafield3 to set.
	 */
	public void setExtrafield3(String extrafield3) {
		this.extrafield3 = extrafield3;
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
	 * @return Returns the id_bankbranch.
	 */
	public Integer getBankBranchId() {
		return bankBranchId;
	}

	/**
	 * @param id_bankbranch The id_bankbranch to set.
	 */
	public void setBankBranchId(Integer id_bankbranch) {
		this.bankBranchId = id_bankbranch;
	}

	public boolean equals(Object obj) {
		// equals will only compare superclass fields
		return super.equals(obj);
	}
}