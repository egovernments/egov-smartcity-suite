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
package org.egov.ptis.domain.entity.property;

import java.math.BigDecimal;
import java.util.Date;

public class RedNoticeInfo {

    private String assessmentNo;
    private String fromInstallment;
    private String toInstallment;
    private BigDecimal arrearTax;
    private BigDecimal arrearPenaltyTax;
    private BigDecimal currentTax;
    private BigDecimal currentTaxPenalty;
    private BigDecimal totalDue;
    private String ownerName;
    private String revenueWard;
    private String doorNo;
    private String locality;
    private String mobileNo;
    private boolean installmentCount;
    private Date minDate;
    private Date maxDate;

    public String getAssessmentNo() {
        return assessmentNo;
    }

    public void setAssessmentNo(String assessmentNo) {
        this.assessmentNo = assessmentNo;
    }

    public String getFromInstallment() {
        return fromInstallment;
    }

    public void setFromInstallment(String fromInstallment) {
        this.fromInstallment = fromInstallment;
    }

    public String getToInstallment() {
        return toInstallment;
    }

    public void setToInstallment(String toInstallment) {
        this.toInstallment = toInstallment;
    }

    public BigDecimal getArrearTax() {
        return arrearTax;
    }

    public void setArrearTax(BigDecimal arrearTax) {
        this.arrearTax = arrearTax;
    }

    public BigDecimal getArrearPenaltyTax() {
        return arrearPenaltyTax;
    }

    public void setArrearPenaltyTax(BigDecimal arrearPenaltyTax) {
        this.arrearPenaltyTax = arrearPenaltyTax;
    }

    public BigDecimal getCurrentTax() {
        return currentTax;
    }

    public void setCurrentTax(BigDecimal currentTax) {
        this.currentTax = currentTax;
    }

    public BigDecimal getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(BigDecimal totalDue) {
        this.totalDue = totalDue;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getRevenueWard() {
        return revenueWard;
    }

    public void setRevenueWard(String revenueWard) {
        this.revenueWard = revenueWard;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public BigDecimal getCurrentTaxPenalty() {
        return currentTaxPenalty;
    }

    public void setCurrentTaxPenalty(BigDecimal currentTaxPenalty) {
        this.currentTaxPenalty = currentTaxPenalty;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public Boolean getInstallmentCount() {
        return installmentCount;
    }

    public void setInstallmentCount(Boolean installmentCount) {
        this.installmentCount = installmentCount;
    }
}
