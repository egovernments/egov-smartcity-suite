package org.egov.ptis.actions.reports;

import java.math.BigDecimal;

public class CollectionSummaryReportResult {

    private String boundaryName;
    private String propertyType;
    private BigDecimal arrearTaxAmount = BigDecimal.ZERO;
    private BigDecimal arrearLibraryCess = BigDecimal.ZERO;
    private BigDecimal arrearTotal = BigDecimal.ZERO;
    private BigDecimal taxAmount = BigDecimal.ZERO;
    private BigDecimal libraryCess = BigDecimal.ZERO;
    private BigDecimal currentTotal = BigDecimal.ZERO;
    private BigDecimal penalty = BigDecimal.ZERO;
    private BigDecimal arrearPenalty = BigDecimal.ZERO;
    private BigDecimal penaltyTotal = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;

    public String getBoundaryName() {
        return boundaryName;
    }

    public void setBoundaryName(final String boundaryName) {
        this.boundaryName = boundaryName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(final String propertyType) {
        this.propertyType = propertyType;
    }

    public BigDecimal getArrearTaxAmount() {
        return arrearTaxAmount;
    }

    public void setArrearTaxAmount(final BigDecimal arrearTaxAmount) {
        this.arrearTaxAmount = arrearTaxAmount;
    }

    public BigDecimal getArrearLibraryCess() {
        return arrearLibraryCess;
    }

    public void setArrearLibraryCess(final BigDecimal arrearLibraryCess) {
        this.arrearLibraryCess = arrearLibraryCess;
    }

    public BigDecimal getArrearTotal() {
        return arrearTotal;
    }

    public void setArrearTotal(final BigDecimal arrearTotal) {
        this.arrearTotal = arrearTotal;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(final BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getLibraryCess() {
        return libraryCess;
    }

    public void setLibraryCess(final BigDecimal libraryCess) {
        this.libraryCess = libraryCess;
    }

    public BigDecimal getCurrentTotal() {
        return currentTotal;
    }

    public void setCurrentTotal(final BigDecimal currentTotal) {
        this.currentTotal = currentTotal;
    }

    public BigDecimal getPenalty() {
        return penalty;
    }

    public void setPenalty(final BigDecimal penalty) {
        this.penalty = penalty;
    }

    public BigDecimal getArrearPenalty() {
        return arrearPenalty;
    }

    public void setArrearPenalty(final BigDecimal arrearPenalty) {
        this.arrearPenalty = arrearPenalty;
    }

    public BigDecimal getPenaltyTotal() {
        return penaltyTotal;
    }

    public void setPenaltyTotal(final BigDecimal penaltyTotal) {
        this.penaltyTotal = penaltyTotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(final BigDecimal total) {
        this.total = total;
    }

}
