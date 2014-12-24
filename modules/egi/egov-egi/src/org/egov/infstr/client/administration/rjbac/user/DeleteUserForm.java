/*
 * @(#)DeleteUserForm.java 3.0, 18 Jun, 2013 2:30:35 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.administration.rjbac.user;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.egov.infstr.client.EgovActionForm;

public class DeleteUserForm extends EgovActionForm {

	private static final long serialVersionUID = 1L;
	private Integer userid = null;
	private Integer departmentId = null;
	private String firstName = "";
	private String middleName = "";
	private String lastName = "";
	private String salutation = "";
	private String dob = "";
	private String userName = "";
	private String pwd = "";
	private String pwdReminder = "";
	private String bndryType = "";
	private String bndryType0 = "";
	private String bndryType1 = "";
	private String bndryType2 = "";
	private String bndryType3 = "";
	private String bndryType4 = "";
	private String bndryType5 = "";
	private Integer roleId = null;
	private String topBoundaryID = null;
	private String removedJurIds = null;

	/**
	 * @return Returns the removedJurIds.
	 */
	public String getRemovedJurIds() {
		return this.removedJurIds;
	}

	/**
	 * @param removedJurIds The removedJurIds to set.
	 */
	public void setRemovedJurIds(final String removedJurIds) {
		this.removedJurIds = removedJurIds;
	}

	/**
	 * @return Returns the departmentId.
	 */
	public Integer getDepartmentId() {
		return this.departmentId;
	}

	/**
	 * @param departmentId The departmentId to set.
	 */
	public void setDepartmentId(final Integer departmentId) {
		this.departmentId = departmentId;
	}

	/**
	 * @return Returns the userid.
	 */
	public Integer getUserid() {
		return this.userid;
	}

	/**
	 * @param userid The userid to set.
	 */
	public void setUserid(final Integer userid) {
		this.userid = userid;
	}

	/**
	 * @return Returns the bndryType.
	 */
	public String getBndryType() {
		return this.bndryType;
	}

	/**
	 * @param bndryType The bndryType to set.
	 */
	public void setBndryType(final String bndryType) {
		this.bndryType = bndryType;
	}

	/**
	 * @return Returns the bndryType0.
	 */
	public String getBndryType0() {
		return this.bndryType0;
	}

	/**
	 * @param bndryType0 The bndryType0 to set.
	 */
	public void setBndryType0(final String bndryType0) {
		this.bndryType0 = bndryType0;
	}

	/**
	 * @return Returns the bndryType1.
	 */
	public String getBndryType1() {
		return this.bndryType1;
	}

	/**
	 * @param bndryType1 The bndryType1 to set.
	 */
	public void setBndryType1(final String bndryType1) {
		this.bndryType1 = bndryType1;
	}

	/**
	 * @return Returns the bndryType2.
	 */
	public String getBndryType2() {
		return this.bndryType2;
	}

	/**
	 * @param bndryType2 The bndryType2 to set.
	 */
	public void setBndryType2(final String bndryType2) {
		this.bndryType2 = bndryType2;
	}

	/**
	 * @return Returns the bndryType3.
	 */
	public String getBndryType3() {
		return this.bndryType3;
	}

	/**
	 * @param bndryType3 The bndryType3 to set.
	 */
	public void setBndryType3(final String bndryType3) {
		this.bndryType3 = bndryType3;
	}

	/**
	 * @return Returns the bndryType4.
	 */
	public String getBndryType4() {
		return this.bndryType4;
	}

	/**
	 * @param bndryType4 The bndryType4 to set.
	 */
	public void setBndryType4(final String bndryType4) {
		this.bndryType4 = bndryType4;
	}

	/**
	 * @return Returns the bndryType5.
	 */
	public String getBndryType5() {
		return this.bndryType5;
	}

	/**
	 * @param bndryType5 The bndryType5 to set.
	 */
	public void setBndryType5(final String bndryType5) {
		this.bndryType5 = bndryType5;
	}

	/**
	 * @return Returns the dob.
	 */
	public String getDob() {
		return this.dob;
	}

	/**
	 * @param dob The dob to set.
	 */
	public void setDob(final String dob) {
		this.dob = dob;
	}

	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * @param firstName The firstName to set.
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * @param lastName The lastName to set.
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return Returns the middleName.
	 */
	public String getMiddleName() {
		return this.middleName;
	}

	/**
	 * @param middleName The middleName to set.
	 */
	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return Returns the pwd.
	 */
	public String getPwd() {
		return this.pwd;
	}

	/**
	 * @param pwd The pwd to set.
	 */
	public void setPwd(final String pwd) {
		this.pwd = pwd;
	}

	/**
	 * @return Returns the pwdReminder.
	 */
	public String getPwdReminder() {
		return this.pwdReminder;
	}

	/**
	 * @param pwdReminder The pwdReminder to set.
	 */
	public void setPwdReminder(final String pwdReminder) {
		this.pwdReminder = pwdReminder;
	}

	/**
	 * @return Returns the roleId.
	 */
	public Integer getRoleId() {
		return this.roleId;
	}

	/**
	 * @param roleId The roleId to set.
	 */
	public void setRoleId(final Integer roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return Returns the salutation.
	 */
	public String getSalutation() {
		return this.salutation;
	}

	/**
	 * @param salutation The salutation to set.
	 */
	public void setSalutation(final String salutation) {
		this.salutation = salutation;
	}

	/**
	 * @return Returns the topBoundaryID.
	 */
	public String getTopBoundaryID() {
		return this.topBoundaryID;
	}

	/**
	 * @param topBoundaryID The topBoundaryID to set.
	 */
	public void setTopBoundaryID(final String topBoundaryID) {
		this.topBoundaryID = topBoundaryID;
	}

	/**
	 * @return Returns the userName.
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * @param userName The userName to set.
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}

	/**
	 * resets properties of this form
	 */
	@Override
	public void reset(final ActionMapping mapping, final HttpServletRequest req) {
		this.dob = "";
		this.firstName = "";
		this.lastName = "";
		this.middleName = "";
		this.pwd = "";
		this.pwdReminder = "";
		this.salutation = "";
		this.userName = "";
		this.bndryType = "";
		this.departmentId = null;
		this.roleId = null;
		this.userid = null;
		this.topBoundaryID = null;
	}
}
