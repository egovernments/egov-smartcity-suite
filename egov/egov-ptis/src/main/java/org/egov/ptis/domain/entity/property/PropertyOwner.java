/*
 * PropertyOwners.java Created on Nov 26, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.entity.property;

import java.util.Date;
import java.util.List;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Address;
import org.egov.infra.admin.master.entity.enums.Gender;
import org.egov.infra.citizen.entity.Citizen;

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
public class PropertyOwner extends Citizen {

	private PropertyImpl property;
	private PropertySource source;
	private Integer orderNo;

	public PropertyOwner() {

	}

	public PropertyOwner(String aadhaarNumber, String activationCode, boolean active,
			List<Address> address, String altContactNumber, Date dob, String emailId,
			Gender gender, String locale, String mobileNumber, String name, String pan,
			String password, String salutation, PropertySource propSource, String username,
			Integer orderNo, PropertyImpl property) {
		setAadhaarNumber(aadhaarNumber);
		setActivationCode(activationCode);
		setActive(active);
		setAddress(address);
		setAltContactNumber(altContactNumber);
		setDob(dob);
		setEmailId(emailId);
		setGender(gender);
		setLocale(locale);
		setMobileNumber(mobileNumber);
		setName(name);
		this.orderNo = orderNo;
		setPan(pan);
		setPassword(password);
		this.property = property;
		setSalutation(salutation);
		this.source = propSource;
		setUsername(username);
	}

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

		final PropertyOwner propertyOwners = (PropertyOwner) that;
		if (this.getId() != null && propertyOwners.getId() != null) {
			if (getId().equals(propertyOwners.getId())) {
				return true;
			} else
				return false;
		} else if (this.getName() != null && propertyOwners.getName() != null) {
			if (getName().equals(propertyOwners.getName())) {
				return true;
			} else
				return false;
		} else

			return false;
	}

	public int hashCode() {
		int hashCode = 0;
		if (this.getProperty() != null) {
			hashCode += this.getProperty().hashCode();
		} else if (this.getId() != null) {
			hashCode += this.getId().hashCode();
		} else if (this.getOrderNo() != null) {
			hashCode += this.getOrderNo().hashCode();
		}
		return hashCode;
	}

	public boolean validate() {
		if (getProperty() == null)
			throw new EGOVRuntimeException(
					"In PropertyOwners Validate : 'ID_Property' Attribute is Not Set, Please Check !!");

		if (getId() == null)
			throw new EGOVRuntimeException(
					"In PropertyOwners Validate : 'Owner ID' Attribute is Not Set, Please Check !!");

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
