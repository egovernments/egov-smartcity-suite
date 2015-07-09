package org.egov.ptis.client.model.calculator;

import java.math.BigDecimal;
import java.util.Map;

import org.egov.commons.Installment;

public class DemandNoticeDetailsInfo {
    
    private Installment installment;
    private BigDecimal propertyTax = new BigDecimal(0);
    private BigDecimal penalty = new BigDecimal(0);
    private BigDecimal waterTax = new BigDecimal(0);
    private BigDecimal drinageSewarageTax = new BigDecimal(0);
    private BigDecimal vacantLandTax = new BigDecimal(0);
    private BigDecimal total = new BigDecimal(0);
    private boolean currentDemand;
    
    public Installment getInstallment() {
        return installment;
    }
    public void setInstallment(Installment installment) {
        this.installment = installment;
    }
    public BigDecimal getPropertyTax() {
        return propertyTax;
    }
    public void setPropertyTax(BigDecimal propertyTax) {
        this.propertyTax = propertyTax;
    }
    public BigDecimal getPenalty() {
        return penalty;
    }
    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }
    public BigDecimal getWaterTax() {
        return waterTax;
    }
    public void setWaterTax(BigDecimal waterTax) {
        this.waterTax = waterTax;
    }
    public BigDecimal getDrinageSewarageTax() {
        return drinageSewarageTax;
    }
    public void setDrinageSewarageTax(BigDecimal drinageSewarageTax) {
        this.drinageSewarageTax = drinageSewarageTax;
    }
    public BigDecimal getVacantLandTax() {
        return vacantLandTax;
    }
    public void setVacantLandTax(BigDecimal vacantLandTax) {
        this.vacantLandTax = vacantLandTax;
    }
    public BigDecimal getTotal() {
        return total;
    }
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    public boolean getCurrentDemand() {
        return currentDemand;
    }
    public void setCurrentDemand(boolean currentDemand) {
        this.currentDemand = currentDemand;
    }
    
    public BigDecimal addTotal(){
        return this.propertyTax.add(this.penalty).add(this.vacantLandTax).add(this.waterTax).add(this.drinageSewarageTax);
    }

}
