/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.restapi.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PropertyAddressDetails implements Serializable {

	private String localityNum;
	private String streetNum;
	private String electionWardNum;
	private String doorNo;
	private String enumerationBlockCode;
	private String pinCode;
	private String zoneNum;
	private String wardNum;
	private String blockNum;
	private Boolean isCorrAddrDiff = false;
	private CorrespondenceAddressDetails corrAddressDetails;

	public String getLocalityNum() {
		return localityNum;
	}

	public void setLocalityNum(String localityNum) {
		this.localityNum = localityNum;
	}

	public String getStreetNum() {
		return streetNum;
	}

	public void setStreetNum(String streetNum) {
		this.streetNum = streetNum;
	}

	public String getElectionWardNum() {
		return electionWardNum;
	}

	public void setElectionWardNum(String electionWardNum) {
		this.electionWardNum = electionWardNum;
	}

	public String getDoorNo() {
		return doorNo;
	}

	public void setDoorNo(String doorNo) {
		this.doorNo = doorNo;
	}

	public String getEnumerationBlockCode() {
		return enumerationBlockCode;
	}

	public void setEnumerationBlockCode(String enumerationBlockCode) {
		this.enumerationBlockCode = enumerationBlockCode;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public Boolean getIsCorrAddrDiff() {
		return isCorrAddrDiff;
	}

	public void setIsCorrAddrDiff(Boolean isCorrAddrDiff) {
		this.isCorrAddrDiff = isCorrAddrDiff;
	}

	public CorrespondenceAddressDetails getCorrAddressDetails() {
		return corrAddressDetails;
	}

	public void setCorrAddress(CorrespondenceAddressDetails corrAddressDetails) {
		this.corrAddressDetails = corrAddressDetails;
	}

	@Override
	public String toString() {
		return "PropertyAddressDetails [localityNum=" + localityNum + ", streetNum=" + streetNum + ", electionWardNum="
				+ electionWardNum + ", doorNo=" + doorNo + ", enumerationBlockCode=" + enumerationBlockCode
				+ ", zoneNum=" + zoneNum + ", wardNum=" + wardNum + ", pinCode=" + pinCode + ", isCorrAddrDiff="
				+ isCorrAddrDiff + ", corrAddressDetails=" + corrAddressDetails + "]";
	}

	public String getZoneNum() {
		return zoneNum;
	}

	public void setZoneNum(String zoneNum) {
		this.zoneNum = zoneNum;
	}

	public String getWardNum() {
		return wardNum;
	}

	public void setWardNum(String wardNum) {
		this.wardNum = wardNum;
	}

	public String getBlockNum() {
		return blockNum;
	}

	public void setBlockNum(String blockNum) {
		this.blockNum = blockNum;
	}

}
