/*
 * PropertyOwners.java Created on Nov 26, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.entity.property;

import java.util.Date;

import org.egov.exceptions.EGOVRuntimeException;
//TODO -- Fix me (Commented to Resolve compilation issues)
//import org.egov.lib.citizen.model.Owner;

/**
 * This class defines Property Owners i.e A Property can have multiple Owners at
 * same point of time
 * 
 * @author Neetu
 * @version 1.00
 * @see
 * @see
 * @since 1.00
 */
//TODO -- Fix me (Commented to Resolve compilation issues)
public class PropertyOwner /*extends Owner*/ {

	private PropertyImpl property;
	private PropertySource source;
	private Integer orderNo;
	public PropertyOwner() {
		
	}
	
	//TODO -- Fix me (Commented to Resolve compilation issues)
	/*public PropertyOwner(Integer citizenID, String ssn, String panNumber, String passportNumber,
			String drivingLicenceNumber, String rationCardNumber, String voterRegistrationNumber, String firstName,
			String middleName, String lastName, Date birthDate, String homePhone, String officePhone,
			String mobilePhone, String fax, String emailAddress, String occupation, String jobStatus, String locale,
			String firstNameLocal, String middleNameLocal, String lastNameLocal, String ownerTitle,
			String ownerTitleLocal, Integer orderNo, PropertySource propSource) {
		setSsn(ssn);
		setPanNumber(panNumber);
		setPassportNumber(passportNumber);
		setDrivingLicenceNumber(drivingLicenceNumber);
		setRationCardNumber(rationCardNumber);
		setVoterRegistrationNumber(voterRegistrationNumber);
		setFirstName(firstName);
		setMiddleName(middleName);
		setLastName(lastName);
		setBirthDate(getBirthDate());
		setHomePhone(homePhone);
		setOfficePhone(officePhone);
		setMobilePhone(mobilePhone);
		setFax(fax);
		setEmailAddress(emailAddress);
		setOccupation(occupation);
		setJobStatus(jobStatus);
		setLocale(locale);
		setFirstNameLocal(firstNameLocal);
		setMiddleNameLocal(middleNameLocal);
		setLastNameLocal(lastNameLocal);
		setOwnerTitle(ownerTitle);
		setOwnertitleLocal(ownerTitleLocal);
		this.orderNo = orderNo;
		this.source = propSource;
	}*/

	public PropertyImpl getProperty() {
		return property;
	}

	public void setProperty(PropertyImpl property) {
		this.property = property;
	}

	public PropertySource getSource() {
		return source;
	}

	public void setSource(PropertySource source) {
		this.source = source;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (this == that)
			return true;

		if (that.getClass() != this.getClass())
			return false;
		/*final PropertyOwner propertyOwners = (PropertyOwner) that;
		if (this.getCitizenID() != null && propertyOwners.getCitizenID() != null) {
			if (getCitizenID().equals(propertyOwners.getCitizenID())) {
				return true;
			} else
				return false;
		} else if (this.getFirstName() != null && propertyOwners.getFirstName() != null) {
			if (getFirstName().equals(propertyOwners.getFirstName())) {
				return true;
			} else
				return false;
		} else*/
			return false;
	}

	public int hashCode() {
		int hashCode = 0;
		if (this.getProperty() != null) {
			hashCode += this.getProperty().hashCode();
		} /*else if (this.getCitizenID() != null) {
			hashCode += this.getCitizenID().hashCode();
		}*/ else if (this.getOrderNo() != null) {
			hashCode += this.getOrderNo().hashCode();
		}
		return hashCode;
	}

	public boolean validate() {
		if (getProperty() == null)
			throw new EGOVRuntimeException(
					"In PropertyOwners Validate : 'ID_Property' Attribute is Not Set, Please Check !!");
		/*if (getCitizenID() == null)
			throw new EGOVRuntimeException(
					"In PropertyOwners Validate : 'Owner ID' Attribute is Not Set, Please Check !!");*/
		return true;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getProperty().getId()).append("|Source: ").append(getSource())
				.append("|Orderno: ").append(getOrderNo());

		return objStr.toString();
	}
}
