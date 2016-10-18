package org.egov.works.reports.entity;

import java.math.BigDecimal;

public class ContractorWiseAbstractSearchResult {

    private String contractorName;
    private String contractorCode;
    private String contractorClass;
    private String electionWard;
    private Integer approvedEstimates;
    private BigDecimal approvedAmount;
    private Integer siteNotHandedOverEstimates;
    private BigDecimal siteNotHandedOverAmount;
    private Integer notWorkCommencedEstimates;
    private BigDecimal notWorkCommencedAmount;
    private Integer workCommencedEstimates;
    private BigDecimal workCommencedAmount;
    private Integer lagecyWorkCommencedEstimates;
    private BigDecimal lagecyWorkCommencedAmount;
    private Integer workCompletedEstimates;
    private BigDecimal workCompletedAmount;
    private Integer balanceWorkEstimates;
    private BigDecimal balanceWorkAmount;
    private BigDecimal liableAmount;

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(final String contractorName) {
        this.contractorName = contractorName;
    }

    public String getContractorClass() {
        return contractorClass;
    }

    public void setContractorClass(final String contractorClass) {
        this.contractorClass = contractorClass;
    }

    public Integer getApprovedEstimates() {
        return approvedEstimates;
    }

    public void setApprovedEstimates(final Integer approvedEstimates) {
        this.approvedEstimates = approvedEstimates;
    }

    public BigDecimal getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(final BigDecimal approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public Integer getSiteNotHandedOverEstimates() {
        return siteNotHandedOverEstimates;
    }

    public void setSiteNotHandedOverEstimates(final Integer siteNotHandedOverEstimates) {
        this.siteNotHandedOverEstimates = siteNotHandedOverEstimates;
    }

    public BigDecimal getSiteNotHandedOverAmount() {
        return siteNotHandedOverAmount;
    }

    public void setSiteNotHandedOverAmount(final BigDecimal siteNotHandedOverAmount) {
        this.siteNotHandedOverAmount = siteNotHandedOverAmount;
    }

    public Integer getNotWorkCommencedEstimates() {
        return notWorkCommencedEstimates;
    }

    public void setNotWorkCommencedEstimates(final Integer notWorkCommencedEstimates) {
        this.notWorkCommencedEstimates = notWorkCommencedEstimates;
    }

    public BigDecimal getNotWorkCommencedAmount() {
        return notWorkCommencedAmount;
    }

    public void setNotWorkCommencedAmount(final BigDecimal notWorkCommencedAmount) {
        this.notWorkCommencedAmount = notWorkCommencedAmount;
    }

    public Integer getWorkCommencedEstimates() {
        return workCommencedEstimates;
    }

    public void setWorkCommencedEstimates(final Integer workCommencedEstimates) {
        this.workCommencedEstimates = workCommencedEstimates;
    }

    public BigDecimal getWorkCommencedAmount() {
        return workCommencedAmount;
    }

    public void setWorkCommencedAmount(final BigDecimal workCommencedAmount) {
        this.workCommencedAmount = workCommencedAmount;
    }

    public Integer getWorkCompletedEstimates() {
        return workCompletedEstimates;
    }

    public void setWorkCompletedEstimates(final Integer workCompletedEstimates) {
        this.workCompletedEstimates = workCompletedEstimates;
    }

    public BigDecimal getWorkCompletedAmount() {
        return workCompletedAmount;
    }

    public void setWorkCompletedAmount(final BigDecimal workCompletedAmount) {
        this.workCompletedAmount = workCompletedAmount;
    }

    public Integer getBalanceWorkEstimates() {
        return balanceWorkEstimates;
    }

    public void setBalanceWorkEstimates(final Integer balanceWorkEstimates) {
        this.balanceWorkEstimates = balanceWorkEstimates;
    }

    public BigDecimal getBalanceWorkAmount() {
        return balanceWorkAmount;
    }

    public void setBalanceWorkAmount(final BigDecimal balanceWorkAmount) {
        this.balanceWorkAmount = balanceWorkAmount;
    }

    public BigDecimal getLiableAmount() {
        return liableAmount;
    }

    public void setLiableAmount(final BigDecimal liableAmount) {
        this.liableAmount = liableAmount;
    }

    public String getElectionWard() {
        return electionWard;
    }

    public void setElectionWard(final String electionWard) {
        this.electionWard = electionWard;
    }

    public String getContractorCode() {
        return contractorCode;
    }

    public void setContractorCode(final String contractorCode) {
        this.contractorCode = contractorCode;
    }

    public Integer getLagecyWorkCommencedEstimates() {
        return lagecyWorkCommencedEstimates;
    }

    public void setLagecyWorkCommencedEstimates(final Integer lagecyWorkCommencedEstimates) {
        this.lagecyWorkCommencedEstimates = lagecyWorkCommencedEstimates;
    }

    public BigDecimal getLagecyWorkCommencedAmount() {
        return lagecyWorkCommencedAmount;
    }

    public void setLagecyWorkCommencedAmount(final BigDecimal lagecyWorkCommencedAmount) {
        this.lagecyWorkCommencedAmount = lagecyWorkCommencedAmount;
    }

}
