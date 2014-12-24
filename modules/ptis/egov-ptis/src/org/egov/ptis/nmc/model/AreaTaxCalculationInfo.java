package org.egov.ptis.nmc.model;

import java.math.BigDecimal;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("areataxes")
public class AreaTaxCalculationInfo {
    @XStreamAsAttribute
    private BigDecimal taxableArea;
    @XStreamAsAttribute
    private BigDecimal monthlyBaseRent;
    private BigDecimal calculatedTax;

    public BigDecimal getTaxableArea() {
        return taxableArea;
    }

    public void setTaxableArea(BigDecimal taxableArea) {
        this.taxableArea = taxableArea;
    }

    public BigDecimal getMonthlyBaseRent() {
        return monthlyBaseRent;
    }

    public void setMonthlyBaseRent(BigDecimal monthlyBaseRent) {
        this.monthlyBaseRent = monthlyBaseRent;
    }

    public BigDecimal getCalculatedTax() {
        return calculatedTax;
    }

    public void setCalculatedTax(BigDecimal calculatedTax) {
        this.calculatedTax = calculatedTax;
    }

    @Override
    public int hashCode() {
        int hashCode = this.taxableArea.hashCode() + this.monthlyBaseRent.hashCode() + this.calculatedTax.hashCode();
        return hashCode;
    }

}
