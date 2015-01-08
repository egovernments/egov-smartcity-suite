/*
 * @(#)Owner.java 3.0, 16 Jun, 2013 12:34:16 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.citizen.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.commons.Bidder;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.lib.address.model.Address;

public class Owner implements OwnerIF, Bidder {

	private Integer citizenID;
	private String ssn;
	private String panNumber;
	private String passportNumber;
	private String drivingLicenceNumber;
	private String rationCardNumber;
	private String voterRegistrationNumber;
	private String firstName;
	private String middleName;
	private String lastName;
	private Date birthDate;
	private String homePhone;
	private String officePhone;
	private String mobilePhone;
	private String fax;
	private String emailAddress;
	private String occupation;
	private String jobStatus;
	private String locale;
	private String firstNameLocal;
	private String middleNameLocal;
	private String lastNameLocal;
	private String ownerTitle;
	private String ownertitleLocal;
	private String sex;
	private Date lastUpdatedTimeStamp;
	private String fatherName;
	private Integer lastModifiedBy = null;
	private Integer createdBy = null;
	private Timestamp createdAt;
	private Set addressSet = new HashSet();

	public Owner() {
		// For Hibernate
	}

	public Owner(Integer citizenID, String ssn, String panNumber, String passportNumber, String drivingLicenceNumber, String rationCardNumber, String voterRegistrationNumber, String firstName, String middleName, String lastName, Date birthDate,
			String homePhone, String officePhone, String mobilePhone, String fax, String emailAddress, String occupation, String jobStatus, String locale, String firstNameLocal, String middleNameLocal, String lastNameLocal, String ownerTitle,
			String ownertitleLocal) {

		this.citizenID = citizenID;
		this.ssn = ssn;
		this.panNumber = panNumber;
		this.passportNumber = passportNumber;
		this.drivingLicenceNumber = drivingLicenceNumber;
		this.rationCardNumber = rationCardNumber;
		this.voterRegistrationNumber = voterRegistrationNumber;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.homePhone = homePhone;
		this.officePhone = officePhone;
		this.mobilePhone = mobilePhone;
		this.fax = fax;
		this.emailAddress = emailAddress;
		this.occupation = occupation;
		this.jobStatus = jobStatus;
		this.locale = locale;
		this.firstNameLocal = firstNameLocal;
		this.middleNameLocal = middleNameLocal;
		this.lastNameLocal = lastNameLocal;
		this.ownerTitle = ownerTitle;
		this.ownertitleLocal = ownertitleLocal;

	}

	/**
	 * @return Returns the fatherName.
	 */
	public String getFatherName() {
		return fatherName;
	}

	/**
	 * @param fatherName The fatherName to set.
	 */
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public Integer getCitizenID() {
		return citizenID;
	}

	public String getSsn() {
		return ssn;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public String getDrivingLicenceNumber() {
		return drivingLicenceNumber;
	}

	public String getRationCardNumber() {
		return rationCardNumber;
	}

	public String getVoterRegistrationNumber() {
		return voterRegistrationNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public String getFax() {
		return fax;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getOccupation() {
		return occupation;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public String getLocale() {
		return locale;
	}

	public String getFirstNameLocal() {
		return firstNameLocal;
	}

	public String getMiddleNameLocal() {
		return middleNameLocal;
	}

	public String getLastNameLocal() {
		return lastNameLocal;
	}

	public String getOwnerTitle() {
		return ownerTitle;
	}

	public String getOwnertitleLocal() {
		return ownertitleLocal;
	}

	/**
	 * @return Returns the lastUpdatedTimeStamp.
	 */
	public Date getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	/**
	 * @param lastUpdatedTimeStamp The lastUpdatedTimeStamp to set.
	 */
	public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}

	// public Address[] getAddress(){return address;}

	/**
	 * @return Returns if the given Object is equal to Owner
	 */
	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (this == that)
			return true;

		// NOTE :The instanceof operator condition fails to return false if the argument is a subclass of the class
		if (that.getClass() != this.getClass())
			return false;
		final Owner thatOwner = (Owner) that;

		if (this.getCitizenID() != null && thatOwner.getCitizenID() != null) {
			if (getCitizenID().toString().equals(thatOwner.getCitizenID().toString())) {
				return true;
			} else
				return false;
		} else
			return false;

	}

	/**
	 * @return Returns the hashCode
	 */
	public int hashCode() {
		int hashCode = 0;
		if (getCitizenID() != null) {
			hashCode += this.getCitizenID().toString().hashCode();
		}
		return hashCode;
	}

	/**
	 * @return Returns the boolean after validating the current object
	 */
	public boolean validate() {

		if (getCitizenID() == 0)
			throw new EGOVRuntimeException("CitizenID is Invalid, Please Check !!");

		return true;
	}

	/**
	 * @return Returns the sex.
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex The sex to set.
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setCitizenID(Integer citizenID) {
		this.citizenID = citizenID;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public void setDrivingLicenceNumber(String drivingLicenceNumber) {
		this.drivingLicenceNumber = drivingLicenceNumber;
	}

	public void setRationCardNumber(String rationCardNumber) {
		this.rationCardNumber = rationCardNumber;
	}

	public void setVoterRegistrationNumber(String voterRegistrationNumber) {
		this.voterRegistrationNumber = voterRegistrationNumber;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public void setFirstNameLocal(String firstNameLocal) {
		this.firstNameLocal = firstNameLocal;
	}

	public void setMiddleNameLocal(String middleNameLocal) {
		this.middleNameLocal = middleNameLocal;
	}

	public void setLastNameLocal(String lastNameLocal) {
		this.lastNameLocal = lastNameLocal;
	}

	public void setOwnerTitle(String ownerTitle) {
		this.ownerTitle = ownerTitle;
	}

	public void setOwnertitleLocal(String ownertitleLocal) {
		this.ownertitleLocal = ownertitleLocal;
	}

	/*
	 * public void setAddress(Address [] address ) { this.address = address; }
	 */
	/**
	 * @return Returns the addressSet.
	 */
	public Set getAddressSet() {
		return addressSet;
	}

	/**
	 * @param addressSet The addressSet to set.
	 */
	public void setAddressSet(Set addressSet) {
		this.addressSet = addressSet;
	}

	public Set addAddress(Address add) {
		getAddressSet().add(add);
		return this.addressSet;
	}

	public Set removeAddress(Address add) {
		getAddressSet().remove(add);
		return this.addressSet;
	}

	/**
	 * @return Returns the createdAt.
	 */
	public Timestamp getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt The createdAt to set.
	 */
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return Returns the createdBy.
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy The createdBy to set.
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return Returns the lastModifiedBy.
	 */
	public Integer getLastModifiedBy() {
		return lastModifiedBy;
	}

	/**
	 * @param lastModifiedBy The lastModifiedBy to set.
	 */
	public void setLastModifiedBy(Integer lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Override
	public String getAddress() {
		return null;
	}

	public Integer getId() {
		return this.citizenID;
	}

	@Override
	public String getName() {
		return this.firstName;
	}

	@Override
	public String getCode() {
		return this.firstName;
	}

	@Override
	public String getBidderType() {
		return "OWNER";
	}

	@Override
	public Integer getBidderId() {
		return this.citizenID;
	}
}