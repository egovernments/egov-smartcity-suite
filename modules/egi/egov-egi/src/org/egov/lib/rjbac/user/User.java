/*
 * @(#)User.java 3.0, 15 Jun, 2013 11:33:55 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.user;

import java.util.Date;
import java.util.Set;

import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.jurisdiction.Jurisdiction;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.security.JurisdictionData;

public interface User extends JurisdictionData {

	/**
	 * Gets the department.
	 * @return Returns the department.
	 */
	Department getDepartment();

	/**
	 * Sets the department.
	 * @param department The department to set.
	 */
	void setDepartment(Department department);

	/**
	 * Gets the extra field1.
	 * @return Returns the extrafield1.
	 */
	String getExtraField1();

	/**
	 * Sets the extra field1.
	 * @param extrafield1 The extrafield1 to set.
	 */
	void setExtraField1(String extrafield1);

	/**
	 * Gets the extra field2.
	 * @return Returns the extrafield2.
	 */
	String getExtraField2();

	/**
	 * Sets the extra field2.
	 * @param extrafield2 The extrafield2 to set.
	 */
	void setExtraField2(String extrafield2);

	/**
	 * Gets the extra field3.
	 * @return Returns the extrafield3.
	 */
	String getExtraField3();

	/**
	 * Sets the extra field3.
	 * @param extrafield3 The extrafield3 to set.
	 */
	void setExtraField3(String extrafield3);

	/**
	 * Gets the extra field4.
	 * @return Returns the extrafield4.
	 */
	String getExtraField4();

	/**
	 * Sets the extra field4.
	 * @param extrafield4 The extrafield4 to set.
	 */
	void setExtraField4(String extrafield4);

	/**
	 * Gets the first name.
	 * @return Returns the first_name.
	 */
	String getFirstName();

	/**
	 * Sets the first name.
	 * @param first_name The first_name to set.
	 */
	void setFirstName(String first_name);

	/**
	 * Gets the checks if is active.
	 * @return Returns the isActive.
	 */
	Integer getIsActive();

	/**
	 * Sets the checks if is active.
	 * @param isActive The isActive to set.
	 */
	void setIsActive(Integer isActive);

	/**
	 * Gets the id.
	 * @return Returns the id.
	 */
	Integer getId();

	/**
	 * Sets the id.
	 * @param id The id to set.
	 */
	void setId(Integer id);

	/**
	 * Gets the checks if is suspended.
	 * @return Returns the is_suspended.
	 */
	char getIsSuspended();

	/**
	 * Sets the checks if is suspended.
	 * @param is_suspended The is_suspended to set.
	 */
	void setIsSuspended(char is_suspended);

	/**
	 * Gets the last name.
	 * @return Returns the last_name.
	 */
	String getLastName();

	/**
	 * Sets the last name.
	 * @param last_name The last_name to set.
	 */
	void setLastName(String last_name);

	/**
	 * Gets the middle name.
	 * @return Returns the middle_name.
	 */
	String getMiddleName();

	/**
	 * Sets the middle name.
	 * @param middle_name The middle_name to set.
	 */
	void setMiddleName(String middle_name);

	/**
	 * Gets the pwd.
	 * @return Returns the pwd.
	 */
	String getPwd();

	/**
	 * Sets the pwd.
	 * @param pwd The pwd to set.
	 */
	void setPwd(String pwd);

	/**
	 * Gets the pwd reminder.
	 * @return Returns the pwd_reminder.
	 */
	String getPwdReminder();

	/**
	 * Sets the pwd reminder.
	 * @param pwd_reminder The pwd_reminder to set.
	 */
	void setPwdReminder(String pwd_reminder);

	/**
	 * Gets the dob.
	 * @return Returns the dob.
	 */
	Date getDob();

	/**
	 * Sets the dob.
	 * @param dob The dob to set.
	 */
	void setDob(Date dob);

	/**
	 * Gets the roles.
	 * @return Returns the roles.
	 */
	Set<Role> getRoles();

	/**
	 * Gets the user roles.
	 * @return the user roles
	 */
	Set<UserRole> getUserRoles();

	/**
	 * Adds the role.
	 * @param role the role
	 */
	void addRole(Role role);

	/**
	 * Removes the role.
	 * @param role the role
	 */
	void removeRole(Role role);

	/**
	 * Sets the roles.
	 * @param roles The roles to set.
	 */
	void setRoles(Set<Role> roles);

	/**
	 * Sets the user roles.
	 * @param userRoles the new user roles
	 */
	void setUserRoles(Set<UserRole> userRoles);

	/**
	 * Gets the salutation.
	 * @return Returns the salutation.
	 */
	String getSalutation();

	/**
	 * Sets the salutation.
	 * @param salutation The salutation to set.
	 */
	void setSalutation(String salutation);

	/**
	 * Gets the title.
	 * @return Returns the title.
	 */
	String getTitle();

	/**
	 * Sets the title.
	 * @param title The title to set.
	 */
	void setTitle(String title);

	/**
	 * Gets the update time.
	 * @return Returns the updatetime.
	 */
	Date getUpdateTime();

	/**
	 * Sets the update time.
	 * @param updatetime The updatetime to set.
	 */
	void setUpdateTime(Date updatetime);

	/**
	 * Gets the from date.
	 * @return Returns the fromdate.
	 */
	Date getFromDate();

	/**
	 * Sets the from date.
	 * @param fromDate the new from date
	 */
	void setFromDate(Date fromDate);

	/**
	 * Gets the to date.
	 * @return Returns the todate.
	 */
	Date getToDate();

	/**
	 * Sets the to date.
	 * @param toDate the new to date
	 */
	void setToDate(Date toDate);

	/**
	 * Gets the last password modified date.
	 * @return Returns the Pwd Modified Date.
	 */
	Date getPwdModifiedDate();

	/**
	 * Sets last password modified date
	 * @param PwdModifiedDate the date given
	 */
	void setPwdModifiedDate(Date pwdModifiedDate);

	/**
	 * Gets the update user id.
	 * @return Returns the updateuserid.
	 */
	Integer getUpdateUserId();

	/**
	 * Sets the update user id.
	 * @param updateuserid The updateuserid to set.
	 */
	void setUpdateUserId(Integer updateuserid);

	/**
	 * Gets the user name.
	 * @return Returns the user_name.
	 */
	String getUserName();

	/**
	 * Sets the user name.
	 * @param user_name The user_name to set.
	 */
	void setUserName(String user_name);

	/**
	 * Sets the login terminal.
	 * @param user_name the new login terminal
	 */
	void setLoginTerminal(String user_name);

	/**
	 * Gets the login terminal.
	 * @return String The terminal name from which the user logged in.
	 */
	String getLoginTerminal();

	/**
	 * Gets the all jurisdictions.
	 * @return the all jurisdictions
	 */
	Set<Jurisdiction> getAllJurisdictions();

	/**
	 * Gets the all jurisdictions for level.
	 * @param bt the bt
	 * @return the all jurisdictions for level
	 */
	Set<Jurisdiction> getAllJurisdictionsForLevel(BoundaryType bt);

	/**
	 * Sets the all jurisdictions.
	 * @param allJurisdictions the new all jurisdictions
	 */
	void setAllJurisdictions(Set<Jurisdiction> allJurisdictions);

	/**
	 * Adds the jurisdiction.
	 * @param jur the jur
	 */
	void addJurisdiction(Jurisdiction jur);

	/**
	 * Removes the jurisdiction.
	 * @param jur the jur
	 */
	void removeJurisdiction(Jurisdiction jur);

	/**
	 * Sets the top boundary id.
	 * @param topBoundaryID the new top boundary id
	 */
	void setTopBoundaryID(Integer topBoundaryID);

	/**
	 * Gets the top boundary id.
	 * @return the top boundary id
	 */
	Integer getTopBoundaryID();

	/**
	 * Gets the all jurisdictions for level full reslove.
	 * @param bt the bt
	 * @return the all jurisdictions for level full reslove
	 */
	Set<Jurisdiction> getAllJurisdictionsForLevelFullReslove(BoundaryType bt);

	/**
	 * Gets the parent.
	 * @return Returns the user to which this user reports to.
	 */
	User getParent();

	/**
	 * Sets the parent.
	 * @param reportsTo The reportsTo to set.
	 */
	void setParent(User reportsTo);

	/**
	 * Sets the reportees.
	 * @param reportees the new reportees
	 */
	void setReportees(Set reportees);

	/**
	 * Gets the reportees.
	 * @return the reportees
	 */
	Set getReportees();

	/**
	 * Gets the all jurisdictions full reslove.
	 * @return the all jurisdictions full reslove
	 */
	Set<Jurisdiction> getAllJurisdictionsFullReslove();

	/**
	 * Gets the user signature byte stream
	 * @return byte array of signature data
	 */
	UserSignature getUserSignature();

	/**
	 * Sets the user signature byte stream.
	 * @param byte array of signature data
	 */
	void setUserSignature(UserSignature userSignature);
}