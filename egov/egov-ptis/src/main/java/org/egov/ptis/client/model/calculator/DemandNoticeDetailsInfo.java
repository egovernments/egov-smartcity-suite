package org.egov.ptis.client.model.calculator;

import org.egov.commons.Installment;

import java.math.BigDecimal;

public class DemandNoticeDetailsInfo {

    private Installment installment;
    private String fromDate;
    private String toDate;
    private BigDecimal propertyTax = new BigDecimal(0);
    private BigDecimal penalty = new BigDecimal(0);
    private BigDecimal waterTax = new BigDecimal(0);
    private BigDecimal drinageSewarageTax = new BigDecimal(0);
    private BigDecimal vacantLandTax = new BigDecimal(0);
    private final BigDecimal total = new BigDecimal(0);

    public Installment getInstallment() {
        return installment;
    }

    public void setInstallment(final Installment installment) {
        this.installment = installment;
    }

    public BigDecimal getPropertyTax() {
        if (propertyTax.equals(new BigDecimal(0)))
            return null;
        else
            return propertyTax;
    }

    public void setPropertyTax(final BigDecimal propertyTax) {
        this.propertyTax = propertyTax;
    }

    public BigDecimal getPenalty() {
        if (penalty.equals(new BigDecimal(0)))
            return null;
        else
            return penalty;
    }

    public void setPenalty(final BigDecimal penalty) {
        this.penalty = penalty;
    }

    public BigDecimal getWaterTax() {
        if (waterTax.equals(new BigDecimal(0)))
            return null;
        else
            return waterTax;
    }

    public void setWaterTax(final BigDecimal waterTax) {
        this.waterTax = waterTax;
    }

    public BigDecimal getDrinageSewarageTax() {
        if (drinageSewarageTax.equals(new BigDecimal(0)))
            return null;
        else
            return drinageSewarageTax;
    }

    public void setDrinageSewarageTax(final BigDecimal drinageSewarageTax) {
        this.drinageSewarageTax = drinageSewarageTax;
    }

    public BigDecimal getVacantLandTax() {
        if (vacantLandTax.equals(new BigDecimal(0)))
            return null;
        else
            return vacantLandTax;
    }

    public void setVacantLandTax(final BigDecimal vacantLandTax) {
        this.vacantLandTax = vacantLandTax;
    }

    public BigDecimal getTotal() {
        return addTotal();
    }

    public BigDecimal addTotal() {
        BigDecimal sumAmount = new BigDecimal(0);
        if (propertyTax != null)
            sumAmount = sumAmount.add(propertyTax);
        if (penalty != null)
            sumAmount = sumAmount.add(penalty);
        if (waterTax != null)
            sumAmount = sumAmount.add(waterTax);
        if (vacantLandTax != null)
            sumAmount = sumAmount.add(vacantLandTax);
        if (drinageSewarageTax != null)
            sumAmount = sumAmount.add(drinageSewarageTax);
        return sumAmount;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(final String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(final String toDate) {
        this.toDate = toDate;
    }

}
