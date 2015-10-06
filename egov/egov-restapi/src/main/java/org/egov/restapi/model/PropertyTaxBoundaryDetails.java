package org.egov.restapi.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PropertyTaxBoundaryDetails implements Serializable {

	private String circleName;
	private String zoneName;
	private String wardName;
	private String blockName;
	private String ownerName;
	private String doorNo;
	private String aadhaarNumber;
	private String mobileNumber;

	public String getCircleName() {
		return circleName;
	}

	public void setCircleName(String circleName) {
		this.circleName = circleName;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getDoorNo() {
		return doorNo;
	}

	public void setDoorNo(String doorNo) {
		this.doorNo = doorNo;
	}

	public String getAadhaarNumber() {
		return aadhaarNumber;
	}

	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Override
	public String toString() {
		return "ProeprtyTaxBoundaryDetails [circleName=" + circleName + ", zoneName=" + zoneName + ", wardName="
				+ wardName + ", blockName=" + blockName + ", ownerName=" + ownerName + ", doorNo=" + doorNo
				+ ", aadhaarNumber=" + aadhaarNumber + ", mobileNumber=" + mobileNumber + "]";
	}

}
