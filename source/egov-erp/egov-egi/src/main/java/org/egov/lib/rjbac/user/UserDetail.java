/*
 * @(#)UserDetail.java 3.0, 15 Jun, 2013 11:37:37 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.user;

import java.util.Date;

public interface UserDetail {
	/**
	 * @return Returns the extrafield1.
	 */
	String getExtrafield1();

	/**
	 * @param extrafield1 The extrafield1 to set.
	 */
	void setExtrafield1(String extrafield1);

	/**
	 * @return Returns the extrafield2.
	 */
	String getExtrafield2();

	/**
	 * @param extrafield2 The extrafield2 to set.
	 */
	void setExtrafield2(String extrafield2);

	/**
	 * @return Returns the extrafield3.
	 */
	String getExtrafield3();

	/**
	 * @param extrafield3 The extrafield3 to set.
	 */
	void setExtrafield3(String extrafield3);

	/**
	 * @return Returns the id.
	 */
	Integer getId();

	/**
	 * @param id The id to set.
	 */
	void setId(Integer id);

	/**
	 * @return Returns the id_bankbranch.
	 */
	Integer getBankBranchId();

	/**
	 * @param id_bankbranch The id_bankbranch to set.
	 */
	void setBankBranchId(Integer id_bankbranch);

	/**
	 * @return Returns the dob.
	 */
	Date getDob();

	/**
	 * @param dob The dob to set.
	 */
	void setDob(Date dob);

	/**
	 * @return Returns the id_emp.
	 */
	Integer getEmpId();

	/**
	 * @param id_emp The id_emp to set.
	 */
	void setEmpId(Integer id_emp);

	/**
	 * @return Returns the locale.
	 */
	String getLocale();

	/**
	 * @param locale The locale to set.
	 */
	void setLocale(String locale);

}