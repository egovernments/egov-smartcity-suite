/*
 * @(#)UserImpl.java 3.0, 18 Jun, 2013 2:43:41 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryFinder;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.VisitableBoundary;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.jurisdiction.Jurisdiction;
import org.egov.lib.rjbac.jurisdiction.JurisdictionValues;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.user.dao.UserDAO;

public class UserImpl implements User {

	private Integer id;
	private String title;
	private String salutation;
	private String firstName;
	private String middleName;
	private String lastName;
	private Department department;
	private Set<org.egov.lib.rjbac.role.Role> roles = new HashSet<org.egov.lib.rjbac.role.Role>();
	private Set<UserRole> userRoles = new HashSet<UserRole>();
	private String userName;
	private String pwd;
	private String pwdReminder;
	private Integer isActive;
	private Date updateTime;
	private Integer updateUserId;
	private String extrafield1;
	private String extrafield2;
	private String extrafield3;
	private String extrafield4;
	private char isSuspended;
	private String loginTerminal;
	private Integer topBoundaryID;
	private Set<Jurisdiction> allJurisdictions = new HashSet<Jurisdiction>();
	private User parent;
	private Set reportees;
	private Date dob;
	private Date fromDate;
	private Date toDate;
	private Date pwdModifiedDate;
	private UserSignature userSignature;

	@Override
	public String toString() {
		StringBuilder strForm = new StringBuilder();
		strForm = (id != null) ? strForm.append("Id:").append(id) : strForm.append("");
		strForm.append("userName: ").append(userName).append("isActive: ").append(isActive);
		return strForm.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getFromDate() {
		return this.fromDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getToDate() {
		return this.toDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Date getPwdModifiedDate() {
		return pwdModifiedDate;
	}

	public void setPwdModifiedDate(Date pwdModifiedDate) {
		this.pwdModifiedDate = pwdModifiedDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLoginTerminal() {
		return this.loginTerminal;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLoginTerminal(String loginTerminal) {
		this.loginTerminal = loginTerminal;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Department getDepartment() {
		return this.department;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDepartment(Department department) {
		this.department = department;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getExtraField1() {
		return this.extrafield1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setExtraField1(String extrafield1) {
		this.extrafield1 = extrafield1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getExtraField2() {
		return this.extrafield2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setExtraField2(String extrafield2) {
		this.extrafield2 = extrafield2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getExtraField3() {
		return this.extrafield3;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setExtraField3(String extrafield3) {
		this.extrafield3 = extrafield3;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getExtraField4() {
		return this.extrafield4;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setExtraField4(String extrafield4) {
		this.extrafield4 = extrafield4;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getId() {
		return this.id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPwd() {
		return this.pwd;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Role> getRoles() {
		final Date currDate = new Date();
		return this.getValidRolesOnDate(currDate);
	}

	public Set<Role> getValidRolesOnDate(Date onDate) {
		return new UserDAO().getValidRoles(this.id, onDate);		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRoles(Set roles) {
		this.roles = roles;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addRole(Role role) {
		this.roles.add(role);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSalutation() {
		return this.salutation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTitle() {
		return this.title;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getUpdateTime() {
		return this.updateTime;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUpdateTime(Date updatetime) {
		this.updateTime = updatetime;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getUpdateUserId() {
		return this.updateUserId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUpdateUserId(Integer updateuserid) {
		this.updateUserId = updateuserid;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public char getIsSuspended() {
		return this.isSuspended;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIsSuspended(char isSuspended) {
		this.isSuspended = isSuspended;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMiddleName() {
		return this.middleName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPwdReminder() {
		return this.pwdReminder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getIsActive() {
		return this.isActive;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPwdReminder(String pwdReminder) {
		this.pwdReminder = pwdReminder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUserName() {
		return this.userName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set getAllJurisdictions() {
		return new UserDAO().getJurisdictionsForUser(this.getId(), new Date());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set getAllJurisdictionsForLevel(BoundaryType bt) {
		/*
		 * FIXME Not convincing reason getAllJurisdictions returns Jurisdiction object but here its casting to JurisdictionValues
		 */

		final Set retSet = new HashSet();
		if (bt == null) {
			return retSet;
		}
		final Set jurSet = this.getAllJurisdictions();
		for (Iterator iter = jurSet.iterator(); iter.hasNext();) {
			final JurisdictionValues element = (JurisdictionValues) iter.next();
			if (element.getBoundary().getBoundaryType().equals(bt)) {
				retSet.add(element.getBoundary());
			}
		}
		return retSet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set getAllJurisdictionsForLevelFullReslove(BoundaryType bt) {
		final Set finalBnSet = new HashSet();
		if (bt == null) {
			return finalBnSet;
		}
		final Set jurSet = this.getAllJurisdictions();
		final Set bndrySet = new HashSet();

		for (final Iterator iter = jurSet.iterator(); iter.hasNext();) {
			final Jurisdiction element = (Jurisdiction) iter.next();
			if (element.getJurisdictionLevel().equals(bt)) {
				for (final Iterator iterator = element.getJurisdictionValues().iterator(); iterator.hasNext();) {
					final JurisdictionValues elementVal = (JurisdictionValues) iterator.next();
					bndrySet.add(elementVal.getBoundary());
				}

				for (final Iterator iterator = bndrySet.iterator(); iterator.hasNext();) {
					final Boundary bndry = (Boundary) iterator.next();
					final VisitableBoundary vBoundary = new VisitableBoundary(bndry);
					final BoundaryFinder visitor = new BoundaryFinder();
					visitor.setTargetBoundaryType(null, true);
					vBoundary.accept(visitor);
					final Collection children = visitor.getResult();
					finalBnSet.addAll(children);
				}
				finalBnSet.addAll(bndrySet);
			}
		}
		return finalBnSet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set getAllJurisdictionsFullReslove() {
		final Set jurSet = this.getAllJurisdictions();
		final Set finalBnSet = new HashSet();
		final Set bndrySet = new HashSet();
		for (final Iterator iter = jurSet.iterator(); iter.hasNext();) {
			final Jurisdiction element = (Jurisdiction) iter.next();
			for (final Iterator iterator = element.getJurisdictionValues().iterator(); iterator.hasNext();) {
				final JurisdictionValues elementVal = (JurisdictionValues) iterator.next();
				bndrySet.add(elementVal.getBoundary());
			}

			for (final Iterator iterator = bndrySet.iterator(); iterator.hasNext();) {
				final Boundary bndry = (Boundary) iterator.next();
				final VisitableBoundary vBoundary = new VisitableBoundary(bndry);
				final BoundaryFinder visitor = new BoundaryFinder();
				visitor.setTargetBoundaryType(null, true);
				vBoundary.accept(visitor);
				final Collection children = visitor.getResult();
				finalBnSet.addAll(children);
			}
			finalBnSet.addAll(bndrySet);
		}
		return finalBnSet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAllJurisdictions(Set allJurisdictions) {
		boolean bool = true;
		for (Iterator iter = allJurisdictions.iterator(); iter.hasNext();) {
			Jurisdiction element = (Jurisdiction) iter.next();
			bool = element.validate();
		}
		if (bool) {
			this.allJurisdictions = allJurisdictions;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addJurisdiction(Jurisdiction jur) {
		boolean bool = jur.validate();
		if (bool) {
			jur.setUser(this);
			this.allJurisdictions.add(jur);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {

		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		if (this.getUserName().equals(other.getUserName())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int hashCode = 0;
		hashCode = hashCode + this.getUserName().hashCode();
		return hashCode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeRole(Role role) {
		if (this.roles.contains(role)) {
			this.roles.remove(role);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeJurisdiction(Jurisdiction jur) {
		jur.setUser(null);
		if (this.allJurisdictions.contains(jur)) {
			this.allJurisdictions.remove(jur);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<? extends Boundary> getJurisdictionData() {
		/*
		 * FIXME Not convincing reason finally sending an empty list
		 */
		final Set allJurs = this.getAllJurisdictions();
		final HashSet<? extends Boundary> allBndries = new HashSet<Boundary>();
		for (Iterator iter = allJurs.iterator(); iter.hasNext();) {
			final Jurisdiction element = (Jurisdiction) iter.next();
			allBndries.addAll(element.getJurisdictionValues());
		}
		List<? extends Boundary> lst = new ArrayList<Boundary>();
		return lst;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getTopBoundaryID() {
		return this.topBoundaryID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTopBoundaryID(Integer topBoundaryID) {
		this.topBoundaryID = topBoundaryID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User getParent() {
		return this.parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParent(User parent) {
		this.parent = parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setReportees(Set reportees) {
		this.reportees = reportees;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set getReportees() {
		return this.reportees;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getDob() {
		return this.dob;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDob(Date dob) {
		this.dob = dob;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<UserRole> getUserRoles() {
		return this.userRoles;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserSignature getUserSignature() {
		return this.userSignature;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUserSignature(UserSignature userSignature) {
		this.userSignature = userSignature;

	}
}
