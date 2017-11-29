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

package org.egov.stms.masters.pojo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SewerageRateDCBResult {
    private String applicationNumber;
    private Integer installmentYearId;
    private String installmentYearDescription;
   
    private BigDecimal collectedAmount=BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_UP);
    private BigDecimal pendingDemandAmount=BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
    private BigDecimal recieptAmountCollected=BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_UP);
    
    private BigDecimal arrearAmount=BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
    private BigDecimal demandAmount=BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
    private BigDecimal penaltyAmount=BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_UP);
   
    
    private BigDecimal collectedArrearAmount=BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
    private BigDecimal collectedDemandAmount=BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
    private BigDecimal collectedPenaltyAmount=BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
    
    private BigDecimal advanceAmount = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
    private BigDecimal collectedAdvanceAmount = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
    
    private Map<String, Map<String, Map<Date, BigDecimal>>> receipts = new HashMap <String, Map<String, Map<Date, BigDecimal>>>();
    
    public String getInstallmentYearDescription() {
        return installmentYearDescription;
    }
    public void setInstallmentYearDescription(String installmentYearDescription) {
        this.installmentYearDescription = installmentYearDescription;
    }
    public BigDecimal getDemandAmount() {
        return demandAmount;
    }
    public void setDemandAmount(BigDecimal demandAmount) {
        this.demandAmount = demandAmount;
    }
    public BigDecimal getCollectedAmount() {
        return collectedAmount;
    }
    public void setCollectedAmount(BigDecimal collectedAmount) {
        this.collectedAmount = collectedAmount;
    }
    public BigDecimal getPendingDemandAmount() {
        return pendingDemandAmount;
    }
    public void setPendingDemandAmount(BigDecimal pendingDemandAmount) {
        this.pendingDemandAmount = pendingDemandAmount;
    }
    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }
    public void setPenaltyAmount(BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }
    public BigDecimal getArrearAmount() {
        return arrearAmount;
    }
    public void setArrearAmount(BigDecimal arrearAmount) {
        this.arrearAmount = arrearAmount;
    }
    public BigDecimal getCollectedArrearAmount() {
        return collectedArrearAmount;
    }
    public void setCollectedArrearAmount(BigDecimal collectedArrearAmount) {
        this.collectedArrearAmount = collectedArrearAmount;
    }
    public BigDecimal getCollectedDemandAmount() {
        return collectedDemandAmount;
    }
    public void setCollectedDemandAmount(BigDecimal collectedDemandAmount) {
        this.collectedDemandAmount = collectedDemandAmount;
    }
    public BigDecimal getCollectedPenaltyAmount() {
        return collectedPenaltyAmount;
    }
    public void setCollectedPenaltyAmount(BigDecimal collectedPenaltyAmount) {
        this.collectedPenaltyAmount = collectedPenaltyAmount;
    }
    
    public BigDecimal getRecieptAmountCollected() {
        return recieptAmountCollected;
    }
    public void setRecieptAmountCollected(BigDecimal recieptAmountCollected) {
        this.recieptAmountCollected = recieptAmountCollected;
    }
    public Integer getInstallmentYearId() {
        return installmentYearId;
    }
    public void setInstallmentYearId(Integer installmentYearId) {
        this.installmentYearId = installmentYearId;
    }
    public String getApplicationNumber() {
        return applicationNumber;
    }
    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }
    public BigDecimal getAdvanceAmount() {
        return advanceAmount;
    }
    public void setAdvanceAmount(BigDecimal advanceAmount) {
        this.advanceAmount = advanceAmount;
    }
    public Map<String, Map<String, Map<Date, BigDecimal>>> getReceipts() {
        return receipts;
    }
    public void setReceipts(Map<String, Map<String, Map<Date, BigDecimal>>> receipts) {
        this.receipts = receipts;
    }
    public BigDecimal getCollectedAdvanceAmount() {
        return collectedAdvanceAmount;
    }
    public void setCollectedAdvanceAmount(BigDecimal collectedAdvanceAmount) {
        this.collectedAdvanceAmount = collectedAdvanceAmount;
    }
}