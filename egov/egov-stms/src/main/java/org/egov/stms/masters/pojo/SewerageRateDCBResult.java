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