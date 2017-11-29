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
package org.egov.ptis.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

/**
 * The AssessmentDetails class is used to contain assessment details such as property id, owner details, total tax and plinth area details
 */
@SuppressWarnings("serial")
public class RestAssessmentDetails implements Serializable {

    private static final long serialVersionUID = 355399781881256188L;
    private String assessmentNo;
    private String propertyAddress;
    private String localityName = "";
    private Set<OwnerName> ownerDetails;
    private Float plinthArea = null;
    private BigDecimal totalTaxDue = BigDecimal.ZERO;
    private String isMutationFeePaid;
    private String feeReceipt;
    private String feeReceiptDate;
    private BigDecimal mutationFee = BigDecimal.ZERO;
    private String applicationNo;

    @Override
    public String toString() {
        return "AssessmentDetails [assessmentNo=" + assessmentNo + ", ownerDetails=" + ownerDetails
                + ", propertyAddress=" + propertyAddress + ", localityName=" + localityName
                + ", plinthArea=" + plinthArea + ", totalTaxDue=" + totalTaxDue + ", isMutationFeePaid=" + isMutationFeePaid
                + ", feeReceipt=" + feeReceipt + ", feeReceiptDate=" + feeReceiptDate
                + ", mutationFee=" + mutationFee + ", applicationNo=" + applicationNo;
              
    }

	public String getAssessmentNo() {
		return assessmentNo;
	}


	public void setAssessmentNo(String assessmentNo) {
		this.assessmentNo = assessmentNo;
	}


	public Set<OwnerName> getOwnerDetails() {
		return ownerDetails;
	}


	public void setOwnerDetails(Set<OwnerName> ownerDetails) {
		this.ownerDetails = ownerDetails;
	}


	public String getPropertyAddress() {
		return propertyAddress;
	}


	public void setPropertyAddress(String propertyAddress) {
		this.propertyAddress = propertyAddress;
	}


	public String getLocalityName() {
		return localityName;
	}


	public void setLocalityName(String localityName) {
		this.localityName = localityName;
	}


	public BigDecimal getTotalTaxDue() {
		return totalTaxDue;
	}


	public void setTotalTaxDue(BigDecimal totalTaxDue) {
		this.totalTaxDue = totalTaxDue;
	}


	public String getIsMutationFeePaid() {
		return isMutationFeePaid;
	}


	public void setIsMutationFeePaid(String isMutationFeePaid) {
		this.isMutationFeePaid = isMutationFeePaid;
	}

	public Float getPlinthArea() {
		return plinthArea;
	}

	public void setPlinthArea(Float plinthArea) {
		this.plinthArea = plinthArea;
	}

	public String getFeeReceipt() {
		return feeReceipt;
	}

	public void setFeeReceipt(String feeReceipt) {
		this.feeReceipt = feeReceipt;
	}

	public String getFeeReceiptDate() {
		return feeReceiptDate;
	}

	public void setFeeReceiptDate(String feeReceiptDate) {
		this.feeReceiptDate = feeReceiptDate;
	}

	public BigDecimal getMutationFee() {
		return mutationFee;
	}

	public void setMutationFee(BigDecimal mutationFee) {
		this.mutationFee = mutationFee;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

}
