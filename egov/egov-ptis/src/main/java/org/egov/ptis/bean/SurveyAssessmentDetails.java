/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.ptis.bean;

import java.math.BigDecimal;
import java.util.List;

import org.egov.ptis.domain.model.OwnerInformation;

public class SurveyAssessmentDetails {

	private String assessmentNo;
    private String doorNo;
    private String propertyAddress;
    private String assessmentYear;
    private String propertyType;
    private String propertyCategory;
    private BigDecimal totalSitalArea = BigDecimal.ZERO;
    private BigDecimal totalTax = BigDecimal.ZERO;
    private List<OwnerInformation> ownerInformation;
	
    public String getAssessmentNo() {
		return assessmentNo;
	}
	public void setAssessmentNo(String assessmentNo) {
		this.assessmentNo = assessmentNo;
	}
	public String getDoorNo() {
		return doorNo;
	}
	public void setDoorNo(String doorNo) {
		this.doorNo = doorNo;
	}
	public String getPropertyAddress() {
		return propertyAddress;
	}
	public void setPropertyAddress(String propertyAddress) {
		this.propertyAddress = propertyAddress;
	}
	public String getAssessmentYear() {
		return assessmentYear;
	}
	public void setAssessmentYear(String assessmentYear) {
		this.assessmentYear = assessmentYear;
	}
	public String getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
	public String getPropertyCategory() {
		return propertyCategory;
	}
	public void setPropertyCategory(String propertyCategory) {
		this.propertyCategory = propertyCategory;
	}
	public BigDecimal getTotalSitalArea() {
		return totalSitalArea;
	}
	public void setTotalSitalArea(BigDecimal totalSitalArea) {
		this.totalSitalArea = totalSitalArea;
	}
	public BigDecimal getTotalTax() {
		return totalTax;
	}
	public void setTotalTax(BigDecimal totalTax) {
		this.totalTax = totalTax;
	}
	public List<OwnerInformation> getOwnerInformation() {
		return ownerInformation;
	}
	public void setOwnerDetails(List<OwnerInformation> ownerInformation) {
		this.ownerInformation = ownerInformation;
	}
	
	@Override
	public String toString() {
		return "SurveyAssessmentDetails [assessmentNo=" + assessmentNo + ", doorNo=" + doorNo + ", propertyAddress="
				+ propertyAddress + ", assessmentYear=" + assessmentYear + ", propertyType=" + propertyType
				+ ", propertyCategory=" + propertyCategory + ", totalSitalArea=" + totalSitalArea + ", totalTax=" + totalTax
				+ ", ownerInformation=" + ownerInformation + "]";
	}
}
