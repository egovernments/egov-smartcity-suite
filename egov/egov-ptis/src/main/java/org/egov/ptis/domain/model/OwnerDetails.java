package org.egov.ptis.domain.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class OwnerDetails implements Serializable {

	private String aadhaarNo;
	private String salutationCode;
	private String name;
	private String gender;
	private String mobileNumber;
	private String emailId;
	private String guardianRelation;
	private String guardian;

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

}
