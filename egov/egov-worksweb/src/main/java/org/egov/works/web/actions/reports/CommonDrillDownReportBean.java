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
import java.math.RoundingMode;
import java.util.Date;

public class CommonDrillDownReportBean {

    private Long estimateId;
    private String estNumber;
    private String estName;
    private Date adminSanctionDate;
    private Date woDate;
    private String workOrderNo;
    private Date siteHandedDate;
    private Date workCommencedDate;
    private BigDecimal woValue = new BigDecimal(0.00);
    private String contractorName;
    private int contractPeriod;
    private Date expectedCompletionDate;
    private Date completionDate;
    private BigDecimal paymentReleasedAmount = new BigDecimal(0.00);
    private Long woId;
    private BigDecimal milestonePerc;
    private BigDecimal paymentReleasedPerc;
    private BigDecimal estAmount = new BigDecimal(0.00);
    private BigDecimal spillOverWorkValue = new BigDecimal(0.00);
    private Date estCreatedDate;
    private Date estDate;
    private String jurisdiction;
    private String wardName;
    private String estPreparedBy;
    private Date estApprovedDate;
    private String tenderNegotiationNum;
    private String typeOfWork;
    private String subTypeOfWork;
    private Date tenderNegotiationDate;
    private Date worksPackageDate;
    private Long worksPackageId;
    private String worksPackageNo;
    private Date tenderDocReleaseDate;
    private String tenderType;
    private String finalBillStatus = "In Progress";
    private BigDecimal tenderValueAfterNegotiation;
    private BigDecimal negotiationPerc;
    private BigDecimal savings;
    private Date workOrderApprovedDate;

    public String getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(final String jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public Date getEstDate() {
        return estDate;
    }

    public void setEstDate(final Date estDate) {
        this.estDate = estDate;
    }

    public BigDecimal getEstAmount() {
        return estAmount;
    }

    public void setEstAmount(final BigDecimal estAmount) {
        this.estAmount = estAmount;
    }

    public BigDecimal getSpillOverWorkValue() {
        return spillOverWorkValue;
    }

    public void setSpillOverWorkValue(final BigDecimal spillOverWorkValue) {
        this.spillOverWorkValue = spillOverWorkValue;
    }

    public Date getEstCreatedDate() {
        return estCreatedDate;
    }

    public void setEstCreatedDate(final Date estCreatedDate) {
        this.estCreatedDate = estCreatedDate;
    }

    public Long getWoId() {
        return woId;
    }

    public void setWoId(final Long woId) {
        this.woId = woId;
    }

    public String getEstNumber() {
        return estNumber;
    }

    public void setEstNumber(final String estNumber) {
        this.estNumber = estNumber;
    }

    public String getEstName() {
        return estName;
    }

    public void setEstName(final String estName) {
        this.estName = estName;
    }

    public Date getAdminSanctionDate() {
        return adminSanctionDate;
    }

    public void setAdminSanctionDate(final Date adminSanctionDate) {
        this.adminSanctionDate = adminSanctionDate;
    }

    public Date getWoDate() {
        return woDate;
    }

    public void setWoDate(final Date woDate) {
        this.woDate = woDate;
    }

    public Date getSiteHandedDate() {
        return siteHandedDate;
    }

    public void setSiteHandedDate(final Date siteHandedDate) {
        this.siteHandedDate = siteHandedDate;
    }

    public Date getWorkCommencedDate() {
        return workCommencedDate;
    }

    public void setWorkCommencedDate(final Date workCommencedDate) {
        this.workCommencedDate = workCommencedDate;
    }

    public BigDecimal getWoValue() {
        return woValue;
    }

    public void setWoValue(final BigDecimal woValue) {
        this.woValue = woValue;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(final String contractorName) {
        this.contractorName = contractorName;
    }

    public int getContractPeriod() {
        return contractPeriod;
    }

    public void setContractPeriod(final int contractPeriod) {
        this.contractPeriod = contractPeriod;
    }

    public Date getExpectedCompletionDate() {
        return expectedCompletionDate;
    }

    public void setExpectedCompletionDate(final Date expectedCompletionDate) {
        this.expectedCompletionDate = expectedCompletionDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(final Date completionDate) {
        this.completionDate = completionDate;
    }

    public BigDecimal getPaymentReleasedAmount() {
        return paymentReleasedAmount;
    }

    public void setPaymentReleasedAmount(final BigDecimal paymentReleasedAmount) {
        this.paymentReleasedAmount = paymentReleasedAmount;
    }

    public BigDecimal getMilestonePerc() {
        return milestonePerc;
    }

    public void setMilestonePerc(final BigDecimal milestonePerc) {
        this.milestonePerc = milestonePerc;
    }

    public BigDecimal getPaymentReleasedPerc() {
        return paymentReleasedPerc;
    }

    public void setPaymentReleasedPerc(final BigDecimal paymentReleasedPerc) {
        this.paymentReleasedPerc = paymentReleasedPerc;
    }

    public Long getEstimateId() {
        return estimateId;
    }

    public void setEstimateId(final Long estimateId) {
        this.estimateId = estimateId;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(final String wardName) {
        this.wardName = wardName;
    }

    public String getEstPreparedBy() {
        return estPreparedBy;
    }

    public void setEstPreparedBy(final String estPreparedBy) {
        this.estPreparedBy = estPreparedBy;
    }

    public Date getEstApprovedDate() {
        return estApprovedDate;
    }

    public void setEstApprovedDate(final Date estApprovedDate) {
        this.estApprovedDate = estApprovedDate;
    }

    public String getTenderNegotiationNum() {
        return tenderNegotiationNum;
    }

    public void setTenderNegotiationNum(final String tenderNegotiationNum) {
        this.tenderNegotiationNum = tenderNegotiationNum;
    }

    public Date getTenderNegotiationDate() {
        return tenderNegotiationDate;
    }

    public void setTenderNegotiationDate(final Date tenderNegotiationDate) {
        this.tenderNegotiationDate = tenderNegotiationDate;
    }

    public Date getWorksPackageDate() {
        return worksPackageDate;
    }

    public void setWorksPackageDate(final Date worksPackageDate) {
        this.worksPackageDate = worksPackageDate;
    }

    public String getTenderType() {
        return tenderType;
    }

    public void setTenderType(final String tenderType) {
        this.tenderType = tenderType;
    }

    public BigDecimal getTenderValueAfterNegotiation() {
        return tenderValueAfterNegotiation;
    }

    public void setTenderValueAfterNegotiation(final BigDecimal tenderValueAfterNegotiation) {
        this.tenderValueAfterNegotiation = tenderValueAfterNegotiation;
    }

    public BigDecimal getNegotiationPerc() {
        return negotiationPerc;
    }

    public void setNegotiationPerc(final BigDecimal negotiationPerc) {
        this.negotiationPerc = negotiationPerc;
    }

    public String getWorksPackageNo() {
        return worksPackageNo;
    }

    public void setWorksPackageNo(final String worksPackageNo) {
        this.worksPackageNo = worksPackageNo;
    }

    public Date getTenderDocReleaseDate() {
        return tenderDocReleaseDate;
    }

    public void setTenderDocReleaseDate(final Date tenderDocReleaseDate) {
        this.tenderDocReleaseDate = tenderDocReleaseDate;
    }

    public String getFinalBillStatus() {
        return finalBillStatus;
    }

    public void setFinalBillStatus(final String finalBillStatus) {
        this.finalBillStatus = finalBillStatus;
    }

    public String getWorkOrderNo() {
        return workOrderNo;
    }

    public void setWorkOrderNo(final String workOrderNo) {
        this.workOrderNo = workOrderNo;
    }

    public Long getWorksPackageId() {
        return worksPackageId;
    }

    public void setWorksPackageId(final Long worksPackageId) {
        this.worksPackageId = worksPackageId;
    }

    public String getTypeOfWork() {
        return typeOfWork;
    }

    public String getSubTypeOfWork() {
        return subTypeOfWork;
    }

    public void setTypeOfWork(final String typeOfWork) {
        this.typeOfWork = typeOfWork;
    }

    public void setSubTypeOfWork(final String subTypeOfWork) {
        this.subTypeOfWork = subTypeOfWork;
    }

    public BigDecimal getSavings() {
        return savings;
    }

    public void setSavings(final BigDecimal savings) {
        this.savings = savings;
    }

    public BigDecimal getRoundedOffEstAmount() {
        return estAmount == null ? null : estAmount.setScale(0, RoundingMode.HALF_UP);
    }

    public Date getWorkOrderApprovedDate() {
        return workOrderApprovedDate;
    }

    public void setWorkOrderApprovedDate(final Date workOrderApprovedDate) {
        this.workOrderApprovedDate = workOrderApprovedDate;
    }

}
