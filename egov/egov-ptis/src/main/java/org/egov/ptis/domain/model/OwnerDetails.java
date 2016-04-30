package org.egov.ptis.domain.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;


@SuppressWarnings("serial")
public class OwnerDetails implements Serializable {

	@JsonIgnore
	private String aadhaarNo;
	@JsonIgnore
	private String salutationCode;
	@JsonIgnore
	private String name;
	@JsonIgnore
	private String gender;
	@JsonIgnore
	private String mobileNumber;
	
	@JsonIgnore
	private String emailId;
	@JsonIgnore
	private String guardianRelation;
	@JsonIgnore
	private String guardian;

	private String ownerName;
	
	private String mobileNo;

	public String getAadhaarNo() {  
		return aadhaarNo;
	}

	public void setAadhaarNo(String aadhaarNo) {
		this.aadhaarNo = aadhaarNo;
	}

	public String getSalutationCode() {
		return salutationCode;
	}

	public void setSalutationCode(String salutationCode) {
		this.salutationCode = salutationCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getGuardianRelation() {
		return guardianRelation;
	}

	public void setGuardianRelation(String guardianRelation) {
		this.guardianRelation = guardianRelation;
	}

	public String getGuardian() {
		return guardian;
	}

	public void setGuardian(String guardian) {
		this.guardian = guardian;
	}

	@Override
	public String toString() {
		return "OwnerFieldDetails [aadhaarNo=" + aadhaarNo + ", salutationCode=" + salutationCode + ", name="
				+ name + ", gender=" + gender + ", mobileNumber=" + mobileNumber + ", emailId=" + emailId
				+ ", guardianRelation=" + guardianRelation + ", guardian=" + guardian + "]";
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

}
