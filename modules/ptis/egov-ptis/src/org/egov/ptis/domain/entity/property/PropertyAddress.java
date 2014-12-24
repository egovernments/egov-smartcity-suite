package org.egov.ptis.domain.entity.property;

import org.egov.lib.address.model.Address;

public class PropertyAddress extends Address {
	private String subNumber;
	private String doorNumOld;
	private String emailAddress;
	private String mobileNo;

	private String extraField1;
	private String extraField2;
	private String extraField3;
	private String extraField4;

	public String getDoorNumOld() {
		return doorNumOld;
	}

	public void setDoorNumOld(String doorNumOld) {
		this.doorNumOld = doorNumOld;
	}

	public String getSubNumber() {
		return subNumber;
	}

	public void setSubNumber(String subNumber) {
		this.subNumber = subNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getExtraField1() {
		return extraField1;
	}

	public String getExtraField2() {
		return extraField2;
	}

	public String getExtraField3() {
		return extraField3;
	}

	public String getExtraField4() {
		return extraField4;
	}

	public void setExtraField1(String extraField1) {
		this.extraField1 = extraField1;
	}

	public void setExtraField2(String extraField2) {
		this.extraField2 = extraField2;
	}

	public void setExtraField3(String extraField3) {
		this.extraField3 = extraField3;
	}

	public void setExtraField4(String extraField4) {
		this.extraField4 = extraField4;
	}

	@Override
	public String toString() {

		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getAddressID()).append("|").append("SubNumber: ").append(getSubNumber()).append(
				"|DoorNumOld: ").append(getDoorNumOld()).append("|EmailAddress : ").append(getEmailAddress()).append(
				"|MobileNo: ").append(getMobileNo());

		return objStr.toString();
	}
}
