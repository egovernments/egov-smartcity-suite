/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.domain.entity.property;

import org.egov.infra.persistence.entity.Address;

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
		//TODO PHOENIX remove this,  address.getId is no more available
		objStr.append("Id: ").append("").append("|").append("SubNumber: ").append(getSubNumber()).append(
				"|DoorNumOld: ").append(getDoorNumOld()).append("|EmailAddress : ").append(getEmailAddress()).append(
				"|MobileNo: ").append(getMobileNo());

		return objStr.toString();
	}
}
