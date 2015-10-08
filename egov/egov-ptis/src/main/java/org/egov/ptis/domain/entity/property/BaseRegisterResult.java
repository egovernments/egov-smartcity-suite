package org.egov.ptis.domain.entity.property;

import java.math.BigDecimal;


public class BaseRegisterResult {

    private String assessmentNo;
    private String ownerName;
    private String doorNO;
    private String natureOfUsage;
    private BigDecimal generalTax;
    private BigDecimal libraryCessTax;
    private BigDecimal eduCessTax;
    private BigDecimal penaltyFines;
    private BigDecimal currTotal;
    private BigDecimal arrearGenaralTax;
    private BigDecimal arrearLibraryTax;
    private BigDecimal arrearEduCess;
    private BigDecimal arrearPenaltyFines;
    private BigDecimal arrearTotal;
    private Boolean isExempted;
    private Boolean courtCase;
    private String arrearPeriod;

    public String getAssessmentNo() {
        return assessmentNo;
    }

    public void setAssessmentNo(String assessmentNo) {
        this.assessmentNo = assessmentNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getDoorNO() {
        return doorNO;
    }

    public void setDoorNO(String doorNO) {
        this.doorNO = doorNO;
    }

    public BigDecimal getGeneralTax() {
        return generalTax;
    }

    public void setGeneralTax(BigDecimal generalTax) {
        this.generalTax = generalTax;
    }

    public BigDecimal getLibraryCessTax() {
        return libraryCessTax;
    }

    public void setLibraryCessTax(BigDecimal libraryCessTax) {
        this.libraryCessTax = libraryCessTax;
    }

    public BigDecimal getEduCessTax() {
        return eduCessTax;
    }

    public void setEduCessTax(BigDecimal eduCessTax) {
        this.eduCessTax = eduCessTax;
    }

    public BigDecimal getPenaltyFines() {
        return penaltyFines;
    }

    public void setPenaltyFines(BigDecimal penaltyFines) {
        this.penaltyFines = penaltyFines;
    }

    public BigDecimal getArrearPenaltyFines() {
        return arrearPenaltyFines;
    }

    public void setArrearPenaltyFines(BigDecimal arrearPenaltyFines) {
        this.arrearPenaltyFines = arrearPenaltyFines;
    }

    public BigDecimal getCurrTotal() {
        return currTotal;
    }

    public void setCurrTotal(BigDecimal currTotal) {
        this.currTotal = currTotal;
    }

    public BigDecimal getArrearGenaralTax() {
        return arrearGenaralTax;
    }

    public void setArrearGenaralTax(BigDecimal arrearGenaralTax) {
        this.arrearGenaralTax = arrearGenaralTax;
    }

    public BigDecimal getArrearLibraryTax() {
        return arrearLibraryTax;
    }

    public void setArrearLibraryTax(BigDecimal arrearLibraryTax) {
        this.arrearLibraryTax = arrearLibraryTax;
    }

    public BigDecimal getArrearEduCess() {
        return arrearEduCess;
    }

    public void setArrearEduCess(BigDecimal arrearEduCess) {
        this.arrearEduCess = arrearEduCess;
    }

    public BigDecimal getArrearTotal() {
        return arrearTotal;
    }

    public void setArrearTotal(BigDecimal arrearTotal) {
        this.arrearTotal = arrearTotal;
    }

    public String getNatureOfUsage() {
        return natureOfUsage;
    }

    public void setNatureOfUsage(String natureOfUsage) {
        this.natureOfUsage = natureOfUsage;
    }

    public Boolean getIsExempted() {
        return isExempted;
    }

    public void setIsExempted(Boolean isExempted) {
        this.isExempted = isExempted;
    }

    public Boolean getCourtCase() {
        return courtCase;
    }

    public void setCourtCase(Boolean courtCase) {
        this.courtCase = courtCase;
    }

    public String getArrearPeriod() {
        return arrearPeriod;
    }

    public void setArrearPeriod(String arrearPeriod) {
        this.arrearPeriod = arrearPeriod;
    }
    
}
