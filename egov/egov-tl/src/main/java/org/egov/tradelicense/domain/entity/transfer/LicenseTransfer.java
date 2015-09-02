package org.egov.tradelicense.domain.entity.transfer;

import java.util.Date;

import org.egov.infstr.models.StateAware;
import org.egov.lib.address.model.Address;
import org.egov.lib.admbndry.Boundary;
import org.egov.tradelicense.domain.entity.License;


public class LicenseTransfer extends StateAware {
	private License license;
	private String oldApplicantName;
	private String oldApplicationNumber;
	private Date oldApplicationDate;
	private String oldNameOfEstablishment;
	private Address oldAddress;
	private String oldPhoneNumber;
	private String oldHomePhoneNumber;
	private String oldMobileNumber;
	private String oldEmailId;
	private String oldUid;
	protected Boundary boundary;
	
	public Boundary getBoundary() {
		return boundary;
	}
	public void setBoundary(Boundary boundary) {
		this.boundary = boundary;
	}
	private boolean approved; 
	public String getOldNameOfEstablishment() {
		return oldNameOfEstablishment;
	}
	public void setOldNameOfEstablishment(String oldNameOfEstablishment) {
		this.oldNameOfEstablishment = oldNameOfEstablishment;
	}
	public String getOldPhoneNumber() {
		return oldPhoneNumber;
	}
	public void setOldPhoneNumber(String oldPhoneNumber) {
		this.oldPhoneNumber = oldPhoneNumber;
	}
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	private String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public License getLicense() {
		return license;
	}
	public void setLicense(License license) {
		this.license = license;
	}
	public String getOldApplicantName() {
		return oldApplicantName;
	}
	public void setOldApplicantName(String oldApplicantName) {
		this.oldApplicantName = oldApplicantName;
	}
	public String getOldApplicationNumber() {
		return oldApplicationNumber;
	}
	public void setOldApplicationNumber(String oldApplicationNumber) {
		this.oldApplicationNumber = oldApplicationNumber;
	}
	public Date getOldApplicationDate() {
		return oldApplicationDate;
	}
	public void setOldApplicationDate(Date oldApplicationDate) {
		this.oldApplicationDate = oldApplicationDate;
	}
	public Address getOldAddress() {
		return oldAddress;
	}
	public void setOldAddress(Address oldAddress) {
		this.oldAddress = oldAddress;
	}
	@Override
	public String getStateDetails() {
		return this.license.getApplicationNumber();
	}
	public String getOldHomePhoneNumber() {
		return oldHomePhoneNumber;
	}
	public void setOldHomePhoneNumber(String oldHomePhoneNumber) {
		this.oldHomePhoneNumber = oldHomePhoneNumber;
	}
	public String getOldMobileNumber() {
		return oldMobileNumber;
	}
	public void setOldMobileNumber(String oldMobileNumber) {
		this.oldMobileNumber = oldMobileNumber;
	}
	public String getOldEmailId() {
		return oldEmailId;
	}
	public void setOldEmailId(String oldEmailId) {
		this.oldEmailId = oldEmailId;
	}
	public String getOldUid() {
		return oldUid;
	}
	public void setOldUid(String oldUid) {
		this.oldUid = oldUid;
	}
	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("LicenseTransfer={");
		str.append("oldApplicantName=").append(oldApplicantName == null ? "null" : oldApplicantName.toString());
		str.append("oldApplicationNumber=").append(oldApplicationNumber == null ? "null" : oldApplicationNumber.toString());
		str.append("oldApplicationDate=").append(oldApplicationDate == null ? "null" : oldApplicationDate.toString());
		str.append("oldNameOfEstablishment=").append(oldNameOfEstablishment == null ? "null" : oldNameOfEstablishment.toString());
		str.append("oldAddress=").append(oldAddress == null ? "null" : oldAddress.toString());
		str.append("oldPhoneNumber=").append(oldPhoneNumber == null ? "null" : oldPhoneNumber.toString());
		str.append("oldHomePhoneNumber=").append(oldHomePhoneNumber == null ? "null" : oldHomePhoneNumber.toString());
		str.append("oldMobileNumber=").append(oldMobileNumber == null ? "null" : oldMobileNumber.toString());
		str.append("oldEmailId=").append(oldEmailId == null ? "null" : oldEmailId.toString());
		str.append("oldUid=").append(oldUid == null ? "null" : oldUid.toString());
		str.append("boundary=").append(boundary == null ? "null" : boundary.toString());
		str.append("}");
		return str.toString();
	}
}
