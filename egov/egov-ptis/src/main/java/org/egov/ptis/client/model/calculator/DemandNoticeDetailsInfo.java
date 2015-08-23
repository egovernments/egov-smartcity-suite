package org.egov.ptis.client.model.calculator;

import java.math.BigDecimal;
import java.util.Map;

import org.egov.commons.Installment;

public class DemandNoticeDetailsInfo {
    
    private Installment installment;
    private String fromDate;
    private String toDate;
    private BigDecimal propertyTax = new BigDecimal(0);
    private BigDecimal penalty = new BigDecimal(0);
    private BigDecimal waterTax = new BigDecimal(0);
    private BigDecimal drinageSewarageTax = new BigDecimal(0);
    private BigDecimal vacantLandTax = new BigDecimal(0);
    private BigDecimal total = new BigDecimal(0);
    
    public Installment getInstallment() {
        return installment;
    }
    public void setInstallment(Installment installment) {
        this.installment = installment;
    }
    public BigDecimal getPropertyTax() {
        if(propertyTax.equals(new BigDecimal(0))){
            return null;
        }
        else
            return propertyTax;
    }
    public void setPropertyTax(BigDecimal propertyTax) {
        this.propertyTax = propertyTax;
    }
    public BigDecimal getPenalty() {
        if(penalty.equals(new BigDecimal(0))){
            return null;
        }
        else
            return penalty;
    }
    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }
    public BigDecimal getWaterTax() {
        if(waterTax.equals(new BigDecimal(0))){
            return null;
        }
        else
        return waterTax;
    }
    public void setWaterTax(BigDecimal waterTax) {
        this.waterTax = waterTax;
    }
    public BigDecimal getDrinageSewarageTax() {
        if(drinageSewarageTax.equals(new BigDecimal(0))){
            return null;
        }
        else
        return drinageSewarageTax;
    }
    public void setDrinageSewarageTax(BigDecimal drinageSewarageTax) {
        this.drinageSewarageTax = drinageSewarageTax;
    }
    public BigDecimal getVacantLandTax() {
        if(vacantLandTax.equals(new BigDecimal(0))){
            return null;
        }
        else
        return vacantLandTax;
    }
    public void setVacantLandTax(BigDecimal vacantLandTax) {
        this.vacantLandTax = vacantLandTax;
    }
    public BigDecimal getTotal() {
        return addTotal();
    }
    public BigDecimal addTotal(){
        BigDecimal sumAmount = new BigDecimal(0);
        if(this.propertyTax!=null)
            sumAmount=sumAmount.add(this.propertyTax);
        if(this.penalty!=null)
            sumAmount=sumAmount.add(this.penalty);
        if(this.waterTax!=null)
            sumAmount=sumAmount.add(this.waterTax);
        if(this.vacantLandTax!=null)
            sumAmount=sumAmount.add(this.vacantLandTax);
        if(this.drinageSewarageTax!=null)
            sumAmount=sumAmount.add(this.drinageSewarageTax);
        return sumAmount;
    }
    public String getFromDate() {
        return fromDate;
    }
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }
    public String getToDate() {
        return toDate;
    }
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

}
