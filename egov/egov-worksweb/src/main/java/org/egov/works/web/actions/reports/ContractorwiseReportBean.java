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
package org.egov.works.web.actions.reports;

import java.math.BigDecimal;

public class ContractorwiseReportBean {

    private Long contractorId;
    private String contractorName;
    private String contractorCode;
    private String contractorClass;
    private Integer takenUpEstimateCount = 0;
    private BigDecimal takenUpWOAmount = BigDecimal.ZERO;
    private Integer completedEstimateCount = 0;
    private BigDecimal completedWOAmount = BigDecimal.ZERO;
    private Integer inProgressEstimateCount = 0;
    private BigDecimal inProgressTenderNegotiatedAmt = BigDecimal.ZERO;
    private BigDecimal inProgressPaymentReleasedAmt = BigDecimal.ZERO;
    private BigDecimal inProgressBalanceAmount = BigDecimal.ZERO;
    private Integer notYetStartedEstimateCount = 0;
    private BigDecimal notYetStartedWOAmount = BigDecimal.ZERO;
    private Integer balanceEstimateCount = 0;
    private BigDecimal balanceAmount = BigDecimal.ZERO;

    public Long getContractorId() {
        return contractorId;
    }

    public void setContractorId(final Long contractorId) {
        this.contractorId = contractorId;
    }

    public String getContractorName() {
        return contractorName;
    }

    public Integer getTakenUpEstimateCount() {
        return takenUpEstimateCount;
    }

    public BigDecimal getTakenUpWOAmount() {
        return takenUpWOAmount;
    }

    public Integer getCompletedEstimateCount() {
        return completedEstimateCount;
    }

    public BigDecimal getCompletedWOAmount() {
        return completedWOAmount;
    }

    public Integer getInProgressEstimateCount() {
        return inProgressEstimateCount;
    }

    public BigDecimal getInProgressBalanceAmount() {
        return inProgressBalanceAmount;
    }

    public Integer getNotYetStartedEstimateCount() {
        return notYetStartedEstimateCount;
    }

    public BigDecimal getNotYetStartedWOAmount() {
        return notYetStartedWOAmount;
    }

    public Integer getBalanceEstimateCount() {
        return balanceEstimateCount;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public void setContractorName(final String contractorName) {
        this.contractorName = contractorName;
    }

    public void setTakenUpEstimateCount(final Integer takenUpEstimateCount) {
        this.takenUpEstimateCount = takenUpEstimateCount;
    }

    public void setTakenUpWOAmount(final BigDecimal takenUpWOAmount) {
        this.takenUpWOAmount = takenUpWOAmount;
    }

    public void setCompletedEstimateCount(final Integer completedEstimateCount) {
        this.completedEstimateCount = completedEstimateCount;
    }

    public void setCompletedWOAmount(final BigDecimal completedWOAmount) {
        this.completedWOAmount = completedWOAmount;
    }

    public void setInProgressEstimateCount(final Integer inProgressEstimateCount) {
        this.inProgressEstimateCount = inProgressEstimateCount;
    }

    public void setInProgressBalanceAmount(final BigDecimal inProgressBalanceAmount) {
        this.inProgressBalanceAmount = inProgressBalanceAmount;
    }

    public void setNotYetStartedEstimateCount(final Integer notYetStartedEstimateCount) {
        this.notYetStartedEstimateCount = notYetStartedEstimateCount;
    }

    public void setNotYetStartedWOAmount(final BigDecimal notYetStartedWOAmount) {
        this.notYetStartedWOAmount = notYetStartedWOAmount;
    }

    public void setBalanceEstimateCount(final Integer balanceEstimateCount) {
        this.balanceEstimateCount = balanceEstimateCount;
    }

    public void setBalanceAmount(final BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public BigDecimal getInProgressTenderNegotiatedAmt() {
        return inProgressTenderNegotiatedAmt;
    }

    public BigDecimal getInProgressPaymentReleasedAmt() {
        return inProgressPaymentReleasedAmt;
    }

    public void setInProgressTenderNegotiatedAmt(final BigDecimal inProgressTenderNegotiatedAmt) {
        this.inProgressTenderNegotiatedAmt = inProgressTenderNegotiatedAmt;
    }

    public void setInProgressPaymentReleasedAmt(final BigDecimal inProgressPaymentReleasedAmt) {
        this.inProgressPaymentReleasedAmt = inProgressPaymentReleasedAmt;
    }

    public String getContractorCode() {
        return contractorCode;
    }

    public String getContractorClass() {
        return contractorClass;
    }

    public void setContractorCode(final String contractorCode) {
        this.contractorCode = contractorCode;
    }

    public void setContractorClass(final String contractorClass) {
        this.contractorClass = contractorClass;
    }

}
