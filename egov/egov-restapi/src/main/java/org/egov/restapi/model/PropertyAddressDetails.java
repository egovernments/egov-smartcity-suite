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

	private String localityCode;
	private String street;
	private String electionWardCode;
	private String doorNo;
	private String enumerationBlockCode;
	private String pinCode;
	private Boolean isCorrAddrDiff;
	private CorrespondenceAddressDetails corrAddressDetails;

	public String getLocalityCode() {
		return localityCode;
	}

	public void setLocalityCode(String localityCode) {
		this.localityCode = localityCode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getElectionWardCode() {
		return electionWardCode;
	}

	public void setElectionWardCode(String electionWardCode) {
		this.electionWardCode = electionWardCode;
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
		return "PropertyAddressDetails [localityCode=" + localityCode + ", street=" + street + ", electionWardCode="
				+ electionWardCode + ", doorNo=" + doorNo + ", enumerationBlockCode=" + enumerationBlockCode
				+ ", pinCode=" + pinCode + ", isCorrAddrDiff=" + isCorrAddrDiff + ", corrAddressDetails=" + corrAddressDetails + "]";
	}

}
