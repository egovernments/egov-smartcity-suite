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

package org.egov.adtax.search.contract;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HoardingDcbReport {
    private String installmentYearDescription;
    private String demandReason;
    private BigDecimal demandAmount = BigDecimal.ZERO;
    private BigDecimal penaltyAmount = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private BigDecimal collectedAmount = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private String  payeeName;
    private String  receiptNumber;
    private BigDecimal pendingAmount;
    private String applicationNumber;
    private String permissionNumber;
    private String category;
    private String subcategory;
    private String locality;
    private String ward;
    private String ownerDetail;
    private String agencyName;
    private BigDecimal totalDemandAmount;
    private BigDecimal arrearAmount = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private BigDecimal collectedArrearAmount = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private BigDecimal collectedDemandAmount = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private BigDecimal collectedPenaltyAmount = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private Map<Date, String> collectReceiptMap = new HashMap<Date, String>();
    
    
    
    
    public String getOwnerDetail() {
        return ownerDetail;
    }
    public void setOwnerDetail(String ownerDetail) {
        this.ownerDetail = ownerDetail;
    }
    public String getDemandReason() {
        return demandReason;
    }
    public void setDemandReason(String demandReason) {
        this.demandReason = demandReason;
    }
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
    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }
    public void setPenaltyAmount(BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }
    public BigDecimal getCollectedAmount() {
        return collectedAmount;
    }
    public void setCollectedAmount(BigDecimal collectedAmount) {
        this.collectedAmount = collectedAmount;
    }
    public String getPayeeName() {
        return payeeName;
    }
    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }
    public String getReceiptNumber() {
        return receiptNumber;
    }
    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }
    public BigDecimal getPendingAmount() {
        return pendingAmount;
    }
    public void setPendingAmount(BigDecimal pendingAmount) {
        this.pendingAmount = pendingAmount;
    }
    public String getApplicationNumber() {
        return applicationNumber;
    }
    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }
    public String getAgencyName() {
        return agencyName;
    }
    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getSubcategory() {
        return subcategory;
    }
    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }
    public String getLocality() {
        return locality;
    }
    public void setLocality(String locality) {
        this.locality = locality;
    }
    public String getWard() {
        return ward;
    }
    public void setWard(String ward) {
        this.ward = ward;
    }
    public String getPermissionNumber() {
        return permissionNumber;
    }
    public void setPermissionNumber(String permissionNumber) {
        this.permissionNumber = permissionNumber;
    }
    public BigDecimal getTotalDemandAmount() {
        return totalDemandAmount;
    }
    public void setTotalDemandAmount(BigDecimal totalDemandAmount) {
        this.totalDemandAmount = totalDemandAmount;
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
    public Map<Date, String> getCollectReceiptMap() {
        return collectReceiptMap;
    }
    public void setCollectReceiptMap(Map<Date, String> collectReceiptMap) {
        this.collectReceiptMap = collectReceiptMap;
    }
}
