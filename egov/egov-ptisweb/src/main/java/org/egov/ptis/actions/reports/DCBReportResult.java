package org.egov.ptis.actions.reports;

import java.math.BigDecimal;

public class DCBReportResult {
    
    private String boundaryName; 
    private Integer boundaryId;
    private String basicPropertyId; 
    private String assessmentNo;
    private BigDecimal dmnd_arrearPT = BigDecimal.ZERO; //GeneralTax    
    private BigDecimal dmnd_arrearLC = BigDecimal.ZERO; //LibCessTax
    private BigDecimal dmnd_arrearEC = BigDecimal.ZERO; //EduCessTax
    private BigDecimal dmnd_arrearUPT = BigDecimal.ZERO; //UnauthPenaltyTax
    private BigDecimal dmnd_arrearPFT = BigDecimal.ZERO; //PenaltyFinesTax
    private BigDecimal dmnd_arrearST = BigDecimal.ZERO; //SewarageTax
    private BigDecimal dmnd_arrearVLT = BigDecimal.ZERO; //VacantLandTax
    private BigDecimal dmnd_arrearPSCT = BigDecimal.ZERO; //PubSerChrgTax
    private BigDecimal dmnd_arrearTotal = BigDecimal.ZERO;
    
    private BigDecimal dmnd_currentPT = BigDecimal.ZERO;
    private BigDecimal dmnd_currentLC = BigDecimal.ZERO;
    private BigDecimal dmnd_currentEC = BigDecimal.ZERO;
    private BigDecimal dmnd_currentUPT = BigDecimal.ZERO;
    private BigDecimal dmnd_currentPFT = BigDecimal.ZERO;
    private BigDecimal dmnd_currentST = BigDecimal.ZERO;
    private BigDecimal dmnd_currentVLT = BigDecimal.ZERO;
    private BigDecimal dmnd_currentPSCT = BigDecimal.ZERO;
    private BigDecimal dmnd_currentTotal = BigDecimal.ZERO;
    private BigDecimal totalDemand = BigDecimal.ZERO;
    
    private BigDecimal clctn_arrearPT = BigDecimal.ZERO;
    private BigDecimal clctn_arrearLC = BigDecimal.ZERO;
    private BigDecimal clctn_arrearEC = BigDecimal.ZERO;
    private BigDecimal clctn_arrearUPT = BigDecimal.ZERO;
    private BigDecimal clctn_arrearPFT = BigDecimal.ZERO;
    private BigDecimal clctn_arrearST = BigDecimal.ZERO;
    private BigDecimal clctn_arrearVLT = BigDecimal.ZERO;
    private BigDecimal clctn_arrearPSCT = BigDecimal.ZERO;
    private BigDecimal clctn_arrearTotal = BigDecimal.ZERO;
    
    private BigDecimal clctn_currentPT = BigDecimal.ZERO;
    private BigDecimal clctn_currentLC = BigDecimal.ZERO;
    private BigDecimal clctn_currentEC = BigDecimal.ZERO;
    private BigDecimal clctn_currentUPT = BigDecimal.ZERO;
    private BigDecimal clctn_currentPFT = BigDecimal.ZERO;
    private BigDecimal clctn_currentST = BigDecimal.ZERO;
    private BigDecimal clctn_currentVLT = BigDecimal.ZERO;
    private BigDecimal clctn_currentPSCT = BigDecimal.ZERO;
    private BigDecimal clctn_currentTotal = BigDecimal.ZERO;
    private BigDecimal totalCollection = BigDecimal.ZERO;
    private BigDecimal bal_arrearPT = BigDecimal.ZERO;
    private BigDecimal bal_currentPT = BigDecimal.ZERO;
    private BigDecimal totalPTBalance = BigDecimal.ZERO;
    
    public String getBoundaryName() {
        return boundaryName;
    }
    public void setBoundaryName(String boundaryName) {   
        this.boundaryName = boundaryName;
    }
    public BigDecimal getDmnd_arrearPT() {
        return dmnd_arrearPT;
    }
    public void setDmnd_arrearPT(BigDecimal dmnd_arrearPT) {
        this.dmnd_arrearPT = dmnd_arrearPT;
    }
    public BigDecimal getDmnd_arrearLC() {
        return dmnd_arrearLC;
    }
    public void setDmnd_arrearLC(BigDecimal dmnd_arrearLC) {
        this.dmnd_arrearLC = dmnd_arrearLC;
    }
    public BigDecimal getDmnd_arrearTotal() {
        return (this.getDmnd_arrearLC().add(this.getDmnd_arrearPT()));
    }
    public BigDecimal getDmnd_currentPT() {
        return dmnd_currentPT;
    }
    public void setDmnd_currentPT(BigDecimal dmnd_currentPT) {
        this.dmnd_currentPT = dmnd_currentPT;
    }
    public BigDecimal getDmnd_currentLC() {
        return dmnd_currentLC;
    }
    public void setDmnd_currentLC(BigDecimal dmnd_currentLC) {
        this.dmnd_currentLC = dmnd_currentLC;
    }
    public BigDecimal getDmnd_currentTotal() {
        return (this.getDmnd_currentLC().add(this.getDmnd_currentPT()));
    }
    public BigDecimal getTotalDemand() {
        return (this.getDmnd_arrearTotal().add(this.getDmnd_currentTotal()));
    }
    public BigDecimal getClctn_arrearPT() {
        return clctn_arrearPT;
    }
    public void setClctn_arrearPT(BigDecimal clctn_arrearPT) {
        this.clctn_arrearPT = clctn_arrearPT;
    }
    public BigDecimal getClctn_arrearLC() {
        return clctn_arrearLC;
    }
    public void setClctn_arrearLC(BigDecimal clctn_arrearLC) {
        this.clctn_arrearLC = clctn_arrearLC;
    }
    public BigDecimal getClctn_arrearTotal() {
        return (this.getClctn_arrearLC().add(this.getClctn_arrearPFT().add(this.getClctn_arrearPT())));
    }
    public BigDecimal getClctn_currentPT() {
        return clctn_currentPT;
    }
    public void setClctn_currentPT(BigDecimal clctn_currentPT) {
        this.clctn_currentPT = clctn_currentPT;
    }
    public BigDecimal getClctn_currentLC() {
        return clctn_currentLC;
    }
    public void setClctn_currentLC(BigDecimal clctn_currentLC) {
        this.clctn_currentLC = clctn_currentLC;
    }
    public BigDecimal getClctn_currentTotal() {
        return (this.getClctn_currentLC().add(this.getClctn_currentPFT().add(this.getClctn_currentPT())));
    }
    public BigDecimal getTotalCollection() {
        return (this.getClctn_arrearTotal().add(this.getClctn_currentTotal()));
    }
    public BigDecimal getBal_arrearPT() {
        return (this.getDmnd_arrearPT().subtract(this.getClctn_arrearPT()));
    }
    public BigDecimal getBal_currentPT() {
        return (this.getDmnd_currentPT().subtract(this.getClctn_currentPT()));
    }
    public BigDecimal getTotalPTBalance() {
        return (this.getBal_arrearPT().add(this.getBal_currentPT()));
    }
    public BigDecimal getDmnd_arrearEC() {
        return dmnd_arrearEC;
    }
    public void setDmnd_arrearEC(BigDecimal dmnd_arrearEC) {
        this.dmnd_arrearEC = dmnd_arrearEC;
    }
    public BigDecimal getDmnd_arrearUPT() {
        return dmnd_arrearUPT;
    }
    public void setDmnd_arrearUPT(BigDecimal dmnd_arrearUPT) {
        this.dmnd_arrearUPT = dmnd_arrearUPT;
    }
    public BigDecimal getDmnd_arrearPFT() {
        return dmnd_arrearPFT;
    }
    public void setDmnd_arrearPFT(BigDecimal dmnd_arrearPFT) {
        this.dmnd_arrearPFT = dmnd_arrearPFT;
    }
    public BigDecimal getDmnd_arrearST() {
        return dmnd_arrearST;
    }
    public void setDmnd_arrearST(BigDecimal dmnd_arrearST) {
        this.dmnd_arrearST = dmnd_arrearST;
    }
    public BigDecimal getDmnd_arrearVLT() {
        return dmnd_arrearVLT;
    }
    public void setDmnd_arrearVLT(BigDecimal dmnd_arrearVLT) {
        this.dmnd_arrearVLT = dmnd_arrearVLT;
    }
    public BigDecimal getDmnd_arrearPSCT() {
        return dmnd_arrearPSCT;
    }
    public void setDmnd_arrearPSCT(BigDecimal dmnd_arrearPSCT) {
        this.dmnd_arrearPSCT = dmnd_arrearPSCT;
    }
    public BigDecimal getDmnd_currentEC() {
        return dmnd_currentEC;
    }
    public void setDmnd_currentEC(BigDecimal dmnd_currentEC) {
        this.dmnd_currentEC = dmnd_currentEC;
    }
    public BigDecimal getDmnd_currentUPT() {
        return dmnd_currentUPT;
    }
    public void setDmnd_currentUPT(BigDecimal dmnd_currentUPT) {
        this.dmnd_currentUPT = dmnd_currentUPT;
    }
    public BigDecimal getDmnd_currentPFT() {
        return dmnd_currentPFT;
    }
    public void setDmnd_currentPFT(BigDecimal dmnd_currentPFT) {
        this.dmnd_currentPFT = dmnd_currentPFT;
    }
    public BigDecimal getDmnd_currentST() {
        return dmnd_currentST;
    }
    public void setDmnd_currentST(BigDecimal dmnd_currentST) {
        this.dmnd_currentST = dmnd_currentST;
    }
    public BigDecimal getDmnd_currentVLT() {
        return dmnd_currentVLT;
    }
    public void setDmnd_currentVLT(BigDecimal dmnd_currentVLT) {
        this.dmnd_currentVLT = dmnd_currentVLT;
    }
    public BigDecimal getDmnd_currentPSCT() {
        return dmnd_currentPSCT;
    }
    public void setDmnd_currentPSCT(BigDecimal dmnd_currentPSCT) {
        this.dmnd_currentPSCT = dmnd_currentPSCT;
    }
    public BigDecimal getClctn_arrearEC() {
        return clctn_arrearEC;
    }
    public void setClctn_arrearEC(BigDecimal clctn_arrearEC) {
        this.clctn_arrearEC = clctn_arrearEC;
    }
    public BigDecimal getClctn_arrearUPT() {
        return clctn_arrearUPT;
    }
    public void setClctn_arrearUPT(BigDecimal clctn_arrearUPT) {
        this.clctn_arrearUPT = clctn_arrearUPT;
    }
    public BigDecimal getClctn_arrearPFT() {
        return clctn_arrearPFT;
    }
    public void setClctn_arrearPFT(BigDecimal clctn_arrearPFT) {
        this.clctn_arrearPFT = clctn_arrearPFT;
    }
    public BigDecimal getClctn_arrearST() {
        return clctn_arrearST;
    }
    public void setClctn_arrearST(BigDecimal clctn_arrearST) {
        this.clctn_arrearST = clctn_arrearST;
    }
    public BigDecimal getClctn_arrearVLT() {
        return clctn_arrearVLT;
    }
    public void setClctn_arrearVLT(BigDecimal clctn_arrearVLT) {
        this.clctn_arrearVLT = clctn_arrearVLT;
    }
    public BigDecimal getClctn_arrearPSCT() {
        return clctn_arrearPSCT;
    }
    public void setClctn_arrearPSCT(BigDecimal clctn_arrearPSCT) {
        this.clctn_arrearPSCT = clctn_arrearPSCT;
    }
    public BigDecimal getClctn_currentEC() {
        return clctn_currentEC;
    }
    public void setClctn_currentEC(BigDecimal clctn_currentEC) {
        this.clctn_currentEC = clctn_currentEC;
    }
    public BigDecimal getClctn_currentUPT() {
        return clctn_currentUPT;
    }
    public void setClctn_currentUPT(BigDecimal clctn_currentUPT) {
        this.clctn_currentUPT = clctn_currentUPT;
    }
    public BigDecimal getClctn_currentPFT() {
        return clctn_currentPFT;
    }
    public void setClctn_currentPFT(BigDecimal clctn_currentPFT) {
        this.clctn_currentPFT = clctn_currentPFT;
    }
    public BigDecimal getClctn_currentST() {
        return clctn_currentST;
    }
    public void setClctn_currentST(BigDecimal clctn_currentST) {
        this.clctn_currentST = clctn_currentST;
    }
    public BigDecimal getClctn_currentVLT() {
        return clctn_currentVLT;
    }
    public void setClctn_currentVLT(BigDecimal clctn_currentVLT) {
        this.clctn_currentVLT = clctn_currentVLT;
    }
    public BigDecimal getClctn_currentPSCT() {
        return clctn_currentPSCT;
    }
    public void setClctn_currentPSCT(BigDecimal clctn_currentPSCT) {
        this.clctn_currentPSCT = clctn_currentPSCT;
    }
    public String getBasicPropertyId() {
        return basicPropertyId;
    }
    public void setBasicPropertyId(String basicPropertyId) {
        this.basicPropertyId = basicPropertyId;
    }
    public String getAssessmentNo() {
        return assessmentNo;
    }
    public void setAssessmentNo(String assessmentNo) {
        this.assessmentNo = assessmentNo;
    }
    public Integer getBoundaryId() {
        return boundaryId;
    }
    public void setBoundaryId(Integer boundaryId) {
        this.boundaryId = boundaryId;
    }
}