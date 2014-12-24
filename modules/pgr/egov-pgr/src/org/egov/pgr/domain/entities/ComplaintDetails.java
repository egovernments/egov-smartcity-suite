/*
 * @(#)ComplaintDetails.java 3.0, 23 Jul, 2013 3:29:42 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.domain.entities;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.egov.infstr.models.GeoLocation;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.models.validator.Required;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.hibernate.validator.constraints.Length;

public class ComplaintDetails extends StateAware {

	private static final long serialVersionUID = 1L;
	private Priority priority;
	private Boundary topLevelbndry;
	private Boundary boundary;

	private CompReceivingModes compReceivingModes;

	@Length(max = 64, message = "complaint.complaintNumber.length")
	private String complaintNumber;

	private Date complaintDate;

	private String firstName;
	private String initials;
	private String lastName;

	private String address;

	private String city;
	private String telePhone;

	private String email;

	private ComplaintTypes complaintType;
	private String details;
	private String title;
	private Integer refComplaintId;
	private ComplaintReceivingCenter receivingCenter;
	private String complaintlocation;
	private DepartmentImpl department;
	/*
	 * private String landmark; private Integer severity; private String firstNameLocal; private String initialsLocal; private String lastnameLocal; private String addressLocal; private String cityLocal; private String detailsLocal; private String locationLocal; private String landmarkLocal; private
	 * String titleLocal;
	 */
	private String mobileNumber;
	private String mode;

	private String pincode;
	private String othervalue;
	private String extrafield1;

	private Integer isEscalated;

	private Date escalatedTime;

	private Date expiryDate;

	private Boundary locBndry;

	private String imageDocNumber;

	private GeoLocation geoLocationDetails;

	private RedressalDetails redressal;

	public Boundary getBoundary() {
		return this.boundary;
	}

	public void setBoundary(final Boundary boundary) {
		this.boundary = boundary;
	}

	@Required(message = "complaint.receiving mode.null")
	public CompReceivingModes getCompReceivingModes() {
		return this.compReceivingModes;
	}

	public void setCompReceivingModes(final CompReceivingModes compReceivingModes) {
		this.compReceivingModes = compReceivingModes;
	}

	public Priority getPriority() {
		return this.priority;
	}

	public void setPriority(final Priority priority) {
		this.priority = priority;
	}

	public ComplaintTypes getComplaintType() {
		return this.complaintType;
	}

	public void setComplaintType(final ComplaintTypes complaintType) {
		this.complaintType = complaintType;
	}

	public Boundary getTopLevelbndry() {
		return this.topLevelbndry;
	}

	public void setTopLevelbndry(final Boundary topLevelbndry) {
		this.topLevelbndry = topLevelbndry;
	}

	public String getComplaintNumber() {
		return this.complaintNumber;
	}

	public void setComplaintNumber(final String complaintNumber) {
		this.complaintNumber = complaintNumber;
	}

	public Date getComplaintDate() {
		return this.complaintDate;
	}

	public void setComplaintDate(final Date complaintDate) {
		this.complaintDate = complaintDate;
	}

	@Required(message = "complaint.register.name.null")
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(final String firstName) {

		this.firstName = setNullIfEmpty(firstName);
	}

	public String getInitials() {
		return this.initials;
	}

	public void setInitials(final String initials) {

		this.initials = setNullIfEmpty(initials);
	}

	public String getLastName() {
		return this.lastName;
	}

	public ComplaintReceivingCenter getReceivingCenter() {
		return this.receivingCenter;
	}

	public void setReceivingCenter(final ComplaintReceivingCenter receivingCenter) {
		this.receivingCenter = receivingCenter;
	}

	public void setLastName(final String lastName) {
		this.lastName = setNullIfEmpty(lastName);
	}

	@Required(message = "complaint.address.null")
	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = setNullIfEmpty(address);
		;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	public String getTelePhone() {
		return StringUtils.isEmpty(this.telePhone) ? null : this.telePhone;
	}

	public void setTelePhone(final String telePhone) {

		this.telePhone = setNullIfEmpty(telePhone);
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {

		this.email = setNullIfEmpty(email);
	}

	public Date getEscalatedTime() {
		return this.escalatedTime;
	}

	public void setEscalatedTime(final Date escalatedTime) {
		this.escalatedTime = escalatedTime;
	}

	public String getDetails() {
		return this.details;
	}

	public void setDetails(final String details) {
		this.details = setNullIfEmpty(details);
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = setNullIfEmpty(title);
	}

	public Integer getRefComplaintId() {
		return this.refComplaintId;
	}

	public void setRefComplaintId(final Integer refComplaintId) {
		this.refComplaintId = refComplaintId;
	}

	public String getComplaintlocation() {
		return this.complaintlocation;
	}

	public void setComplaintlocation(final String complaintlocation) {
		this.complaintlocation = setNullIfEmpty(complaintlocation);
	}

	public String getMobileNumber() {
		return this.mobileNumber;
	}

	public void setMobileNumber(final String mobileNumber) {
		this.mobileNumber = setNullIfEmpty(mobileNumber);
	}

	public String getMode() {
		return this.mode;
	}

	public void setMode(final String mode) {
		this.mode = mode;
	}

	public String getPincode() {
		return this.pincode;
	}

	public void setPincode(final String pincode) {
		this.pincode = setNullIfEmpty(pincode);
	}

	public String getOthervalue() {
		return this.othervalue;
	}

	public void setOthervalue(final String othervalue) {
		this.othervalue = othervalue;
	}

	public String getExtrafield1() {
		return this.extrafield1;
	}

	public void setExtrafield1(final String extrafield1) {
		this.extrafield1 = extrafield1;
	}

	public Date getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(final Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	@Required(message = "complaint.isEscalated.null")
	public Integer getIsEscalated() {
		return this.isEscalated;
	}

	public void setIsEscalated(final Integer isEscalated) {
		this.isEscalated = isEscalated;
	}

	public GeoLocation getGeoLocationDetails() {
		return this.geoLocationDetails;
	}

	public void setGeoLocationDetails(final GeoLocation geoLocationDetails) {
		this.geoLocationDetails = geoLocationDetails;
	}

	/**
	 * @return the redressal
	 */
	public RedressalDetails getRedressal() {
		return this.redressal;
	}

	/**
	 * @param redressal the redressal to set
	 */
	public void setRedressal(final RedressalDetails redressal) {
		this.redressal = redressal;
	}

	/**
	 * @return the department
	 */
	public DepartmentImpl getDepartment() {
		return this.department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(final DepartmentImpl department) {
		this.department = department;
	}

	/**
	 * @return the locBndry
	 */
	public Boundary getLocBndry() {
		return this.locBndry;
	}

	/**
	 * @param locBndry the locBndry to set
	 */
	public void setLocBndry(final Boundary locBndry) {
		this.locBndry = locBndry;
	}

	private String setNullIfEmpty(final String field) {
		if (field != null && field.trim().length() == 0) {
			return null;
		} else {
			return field;
		}
	}

	public String getFullName() {
		final StringBuffer citizenName = new StringBuffer();
		if (getFirstName() != null && !"".equals(getFirstName())) {
			citizenName.append(getFirstName());
			citizenName.append(" ");
		}
		if (getInitials() != null && !"".equals(getInitials())) {
			citizenName.append(getInitials());
			citizenName.append(" ");
		}
		if (getLastName() != null && !"".equals(getLastName())) {
			citizenName.append(getLastName());
		}

		return citizenName.toString();
	}

	public String getPhoneNumber() {
		final StringBuffer phoneNo = new StringBuffer();
		Boolean flag = Boolean.FALSE;
		if (getMobileNumber() != null && !"".equals(getMobileNumber())) {
			phoneNo.append("Mobile No: ");
			phoneNo.append(getMobileNumber());
			phoneNo.append(" ");
			flag = Boolean.TRUE;
		}
		if (getTelePhone() != null && !"".equals(getTelePhone())) {
			phoneNo.append("Telephone No: ");
			phoneNo.append(getTelePhone());
			flag = Boolean.TRUE;
		}
		if (!flag) {
			phoneNo.append("N/A");
		}

		return phoneNo.toString();
	}

	public String getFullAddress() {
		final StringBuffer address = new StringBuffer();
		Boolean flag = Boolean.FALSE;
		if (getAddress() != null && !"".equals(getAddress())) {
			address.append(getAddress());
			address.append(" ");
			flag = Boolean.TRUE;
		} else if (getComplaintlocation() != null && !"".equals(getComplaintlocation())) {
			address.append(getComplaintlocation());
			address.append(" ");
			flag = Boolean.TRUE;
		}
		if (getPincode() != null && !"".equals(getPincode())) {
			address.append("Pincode : ");
			address.append(getPincode());
			flag = Boolean.TRUE;
		}
		if (!flag) {
			address.append("N/A");
		}
		return address.toString();
	}

	/**
	 * @return the imageDocNumber
	 */
	public String getImageDocNumber() {
		return this.imageDocNumber;
	}

	/**
	 * @param imageDocNumber the imageDocNumber to set
	 */
	public void setImageDocNumber(final String imageDocNumber) {
		this.imageDocNumber = imageDocNumber;
	}

	@Override
	public String getStateDetails() {
		return getTitle();
	}

}
