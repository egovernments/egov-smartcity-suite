/*
 * @(#)OwnerIF.java 3.0, 16 Jun, 2013 12:34:57 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.citizen.model;

import java.util.Date;
import java.util.Set;

import org.egov.lib.address.model.Address;

public interface OwnerIF {

	Integer getCitizenID();

	String getSsn();

	String getPanNumber();

	String getPassportNumber();

	String getDrivingLicenceNumber();

	String getRationCardNumber();

	String getVoterRegistrationNumber();

	String getFirstName();

	String getMiddleName();

	String getLastName();

	Date getBirthDate();

	String getHomePhone();

	String getOfficePhone();

	String getMobilePhone();

	String getFax();

	String getEmailAddress();

	String getOccupation();

	String getJobStatus();

	String getLocale();

	String getFirstNameLocal();

	String getMiddleNameLocal();

	String getLastNameLocal();

	String getOwnerTitle();

	String getOwnertitleLocal();

	String getSex();

	String getFatherName();

	void setCitizenID(Integer citizenID);

	void setSsn(String ssn);

	void setPanNumber(String panNumber);

	void setPassportNumber(String passportNumber);

	void setDrivingLicenceNumber(String drivingLicenceNumber);

	void setRationCardNumber(String rationCardNumber);

	void setVoterRegistrationNumber(String voterRegistrationNumber);

	void setFirstName(String firstName);

	void setMiddleName(String middleName);

	void setLastName(String lastName);

	void setBirthDate(Date birthDate);

	void setHomePhone(String homePhone);

	void setOfficePhone(String officePhone);

	void setMobilePhone(String mobilePhone);

	void setFax(String fax);

	void setEmailAddress(String emailAddress);

	void setOccupation(String occupation);

	void setJobStatus(String jobStatus);

	void setLocale(String locale);

	void setFirstNameLocal(String firstNameLocal);

	void setMiddleNameLocal(String middleNameLocal);

	void setLastNameLocal(String lastNameLocal);

	void setOwnerTitle(String ownerTitle);

	void setOwnertitleLocal(String ownertitleLocal);

	void setSex(String sex);

	void setFatherName(String fatherName);

	Set getAddressSet();

	void setAddressSet(Set addressSet);

	Set addAddress(Address add);

	Set removeAddress(Address add);

}