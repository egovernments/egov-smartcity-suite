/*
 * @(#)Licensee.java 3.0, 29 Jul, 2013 1:24:28 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.entity;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Required;
import org.egov.lib.address.model.Address;
import org.egov.lib.admbndry.Boundary;
import org.hibernate.validator.constraints.Length;

public class Licensee extends BaseModel {
	private static final long serialVersionUID = 1L;
	@Required(message = "licensee.name.err.required")
	@Length(min = 1, max = 256, message = "licensee.name.err.maxlength")
	private String applicantName;
	private Address address;
	private String nationality;
	private String fatherOrSpouseName;
	// private String spouseName;
	private String qualification;
	private Integer age;
	private String gender;
	private String panNumber;
	private String phoneNumber;
	private String mobilePhoneNumber;
	private String uid;
	private String emailId;
	private Boundary boundary;

	public Boundary getBoundary() {
		return this.boundary;
	}

	public void setBoundary(final Boundary boundary) {
		this.boundary = boundary;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMobilePhoneNumber() {
		return this.mobilePhoneNumber;
	}

	public void setMobilePhoneNumber(final String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}

	public String getUid() {
		return this.uid;
	}

	public void setUid(final String uid) {
		this.uid = uid;
	}

	public String getEmailId() {
		return this.emailId;
	}

	public void setEmailId(final String emailId) {
		this.emailId = emailId;
	}

	private License license;

	public License getLicense() {
		return this.license;
	}

	public void setLicense(final License license) {
		this.license = license;
	}

	public String getApplicantName() {
		return this.applicantName;
	}

	public void setApplicantName(final String applicantName) {
		this.applicantName = applicantName;
	}

	public Address getAddress() {
		return this.address;
	}

	public void setAddress(final Address address) {
		this.address = address;
	}

	public String getNationality() {
		return this.nationality;
	}

	public void setNationality(final String nationality) {
		this.nationality = nationality;
	}

	public String getFatherOrSpouseName() {
		return this.fatherOrSpouseName;
	}

	public void setFatherOrSpouseName(final String fatherOrSpouseName) {
		this.fatherOrSpouseName = fatherOrSpouseName;
	}

	public String getQualification() {
		return this.qualification;
	}

	public void setQualification(final String qualification) {
		this.qualification = qualification;
	}

	public Integer getAge() {
		return this.age;
	}

	public void setAge(final Integer age) {
		this.age = age;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(final String gender) {
		this.gender = gender;
	}

	public String getPanNumber() {
		return this.panNumber;
	}

	public void setPanNumber(final String panNumber) {
		this.panNumber = panNumber;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("Licensee={");
		str.append("  applicantName=").append(this.applicantName == null ? "null" : this.applicantName.toString());
		str.append("  address=").append(this.address == null ? "null" : this.address.toString());
		str.append("  nationality=").append(this.nationality == null ? "null" : this.nationality.toString());
		str.append("  fatherOrSpouseName=").append(this.fatherOrSpouseName == null ? "null" : this.fatherOrSpouseName.toString());
		str.append("  qualification=").append(this.qualification == null ? "null" : this.qualification.toString());
		str.append("  age=").append(this.age == null ? "null" : this.age.toString());
		str.append("  gender=").append(this.gender == null ? "null" : this.gender.toString());
		str.append("  panNumber=").append(this.panNumber == null ? "null" : this.panNumber.toString());
		str.append("  phoneNumber=").append(this.phoneNumber == null ? "null" : this.phoneNumber.toString());
		str.append("  boundary=").append(this.boundary == null ? "null" : this.boundary.toString());
		str.append("}");
		return str.toString();
	}
}
