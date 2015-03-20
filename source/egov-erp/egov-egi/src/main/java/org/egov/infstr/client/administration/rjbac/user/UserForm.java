/*
 * @(#)UserForm.java 3.0, 18 Jun, 2013 2:38:13 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.administration.rjbac.user;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.egov.infstr.client.EgovActionForm;

public class UserForm extends EgovActionForm implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id = null;
	private String firstName = "";
	private String middleName = "";
	private String lastName = "";
	private String salutation = "";
	private String dob = "";
	// private String empId = "";
	private String userName = "";
	private String password = "";
	private String pwdReminder = "";
	private String bndryType = "";
	private String bndryType0 = "";
	private String bndryType1 = "";
	private String bndryType2 = "";
	private String bndryType3 = "";
	private Integer roleId[] = null;
	private Integer departmentId = null;
	private String topBoundaryID = null;
	private String bndries = "";
	private String level = "";
	private String fromDate = "";
	private String toDate = "";
	private Integer userDetail = null;
	private String fromDate1[] = null;
	private String toDate1[] = null;
	private boolean isActive;
	private String modifyRole[] = null;
	private String selCheck[] = null;
	private String selRoleID[] = null;
	private String exisFromDate[] = null;
	private String exisToDate[] = null;
	private String userRoleId[] = null;
	private String bndryTypeID[] = null;
	private String bndryID[] = null;
	private String bndryValue[] = null;
	private FormFile file;

	public FormFile getFile() {
		return this.file;
	}

	public void setFile(final FormFile file) {
		this.file = file;
	}

	/**
	 * @return Returns the level.
	 */
	public String getLevel() {
		return this.level;
	}

	/**
	 * @param level The level to set.
	 */
	public void setLevel(final String level) {
		this.level = level;
	}

	/**
	 * @return Returns the bndries.
	 */
	public String getBndries() {
		return this.bndries;
	}

	/**
	 * @param bndries The bndries to set.
	 */
	public void setBndries(final String bndries) {
		this.bndries = bndries;
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
	 * @return Returns the roleId.
	 */
	public Integer[] getRoleId() {
		return this.roleId;
	}

	/**
	 * @param roleId The roleId to set.
	 */
	public void setRoleId(final Integer[] roleId) {
		this.roleId = roleId;
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
	 * @return Returns the password.
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @param password The password to set.
	 */
	public void setPassword(final String pwd) {
		this.password = pwd;
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
		this.fromDate = "";
		this.toDate = "";
		this.dob = "";
		this.firstName = "";
		this.lastName = "";
		this.middleName = "";
		this.password = "";
		this.pwdReminder = "";
		this.salutation = "";
		this.userName = "";
		this.departmentId = null;
		this.roleId = null;
	}

	/**
	 * @return Returns the userDetail.
	 */
	public Integer getUserDetail() {
		return this.userDetail;
	}

	/**
	 * @param userDetail The userDetail to set.
	 */
	public void setUserDetail(final Integer userDetail) {
		this.userDetail = userDetail;
	}

	/**
	 * @return Returns the fromDate.
	 */
	public String getFromDate() {
		return this.fromDate;
	}

	/**
	 * @param fromDate The fromDate to set.
	 */
	public void setFromDate(final String fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return Returns the toDate.
	 */
	public String getToDate() {
		return this.toDate;
	}

	/**
	 * @param toDate The toDate to set.
	 */
	public void setToDate(final String toDate) {
		this.toDate = toDate;
	}

	/**
	 * @return Returns the fromDate1.
	 */
	public String[] getFromDate1() {
		return this.fromDate1;
	}

	/**
	 * @param fromDate1 The fromDate1 to set.
	 */
	public void setFromDate1(final String[] fromDate1) {
		this.fromDate1 = fromDate1;
	}

	/**
	 * @return Returns the toDate1.
	 */
	public String[] getToDate1() {
		return this.toDate1;
	}

	/**
	 * @param toDate1 The toDate1 to set.
	 */
	public void setToDate1(final String[] toDate1) {
		this.toDate1 = toDate1;
	}

	/**
	 * @return Returns the isActive.
	 */
	public boolean getIsActive() {
		return this.isActive;
	}

	/**
	 * @param isActive The isActive to set.
	 */
	public void setIsActive(final boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return Returns the modifyRole.
	 */
	public String[] getModifyRole() {
		return this.modifyRole;
	}

	/**
	 * @param modifyRole The modifyRole to set.
	 */
	public void setModifyRole(final String[] modifyRole) {
		this.modifyRole = modifyRole;
	}

	/**
	 * @return Returns the selCheck.
	 */
	public String[] getSelCheck() {
		return this.selCheck;
	}

	/**
	 * @param selCheck The selCheck to set.
	 */
	public void setSelCheck(final String[] selCheck) {
		this.selCheck = selCheck;
	}

	/**
	 * @return Returns the roleID.
	 */
	/**
	 * @return Returns the exisFromDate.
	 */
	public String[] getExisFromDate() {
		return this.exisFromDate;
	}

	/**
	 * @param exisFromDate The exisFromDate to set.
	 */
	public void setExisFromDate(final String[] exisFromDate) {
		this.exisFromDate = exisFromDate;
	}

	/**
	 * @return Returns the exisToDate.
	 */
	public String[] getExisToDate() {
		return this.exisToDate;
	}

	/**
	 * @param exisToDate The exisToDate to set.
	 */
	public void setExisToDate(final String[] exisToDate) {
		this.exisToDate = exisToDate;
	}

	/**
	 * @return Returns the existingRoleID.
	 */
	public String[] getSelRoleID() {
		return this.selRoleID;
	}

	/**
	 * @param existingRoleID The existingRoleID to set.
	 */
	public void setSelRoleID(final String[] existingRoleID) {
		this.selRoleID = existingRoleID;
	}

	/**
	 * @return Returns the userRoleId.
	 */
	public String[] getUserRoleId() {
		return this.userRoleId;
	}

	/**
	 * @param userRoleId The userRoleId to set.
	 */
	public void setUserRoleId(final String[] userRoleId) {
		this.userRoleId = userRoleId;
	}

	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @return the bndryID
	 */
	public String[] getBndryID() {
		return this.bndryID;
	}

	/**
	 * @param bndryID the bndryID to set
	 */
	public void setBndryID(final String[] bndryID) {
		this.bndryID = bndryID;
	}

	/**
	 * @return the bndryTypeID
	 */
	public String[] getBndryTypeID() {
		return this.bndryTypeID;
	}

	/**
	 * @param bndryTypeID the bndryTypeID to set
	 */
	public void setBndryTypeID(final String[] bndryTypeID) {
		this.bndryTypeID = bndryTypeID;
	}

	/**
	 * @return the bndryValue
	 */
	public String[] getBndryValue() {
		return this.bndryValue;
	}

	/**
	 * @param bndryValue the bndryValue to set
	 */
	public void setBndryValue(final String[] bndryValue) {
		this.bndryValue = bndryValue;
	}
}