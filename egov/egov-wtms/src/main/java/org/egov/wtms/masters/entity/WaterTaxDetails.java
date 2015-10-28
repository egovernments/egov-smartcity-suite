/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wtms.masters.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import org.egov.ptis.domain.model.ErrorDetails;

/**
 * The WaterTaxDetails class is used to contain water tax details, arrears
 * details and the corresponding error details if any.
 *
 * @author ranjit
 */
@SuppressWarnings("serial")
public class WaterTaxDetails implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -9178106359243528750L;
    private String applicationNumber;
    private BigDecimal penalty;
    private BigDecimal arrears;
    private BigDecimal donationCharges;
    private BigDecimal estimationCharges;
    private BigDecimal waterTax;
    private BigDecimal totalTaxAmt;
    private ErrorDetails errorDetails;
    public String getApplicationNumber() {
        return applicationNumber;
    }
    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }
    public BigDecimal getPenalty() {
        return penalty;
    }
    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }
    public BigDecimal getArrears() {
        return arrears;
    }
    public void setArrears(BigDecimal arrears) {
        this.arrears = arrears;
    }
    public BigDecimal getDonationCharges() {
        return donationCharges;
    }
    public void setDonationCharges(BigDecimal donationCharges) {
        this.donationCharges = donationCharges;
    }
    public BigDecimal getEstimationCharges() {
        return estimationCharges;
    }
    public void setEstimationCharges(BigDecimal estimationCharges) {
        this.estimationCharges = estimationCharges;
    }
    public BigDecimal getWaterTax() {
        return waterTax;
    }
    public void setWaterTax(BigDecimal waterTax) {
        this.waterTax = waterTax;
    }
    public BigDecimal getTotalTaxAmt() {
        return totalTaxAmt;
    }
    public void setTotalTaxAmt(BigDecimal totalTaxAmt) {
        this.totalTaxAmt = totalTaxAmt;
    }
    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }
    public void setErrorDetails(ErrorDetails errorDetails) {
        this.errorDetails = errorDetails;
    }
    @Override
    public String toString() {
        return "WaterTaxDetails [applicationNumber=" + applicationNumber + ", penalty=" + penalty + ", arrears="
                + arrears + ", donationCharges=" + donationCharges + ", estimationCharges=" + estimationCharges
                + ", waterTax=" + waterTax + ", totalTaxAmt=" + totalTaxAmt + ", errorDetails=" + errorDetails + "]";
    }

   

}
