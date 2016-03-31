package org.egov.adtax.entity;

public class ScheduleOfRateSearch {
    
    private String category;
    private String subCategory;
    private String unitofmeasure;
    private String classtype;
    private String financialyear;
    private Double unitfrom;
    private Double unitto;
    private Double amount;
    private Double unitfactor;

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getSubCategory() {
        return subCategory;
    }
    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }
    public String getUnitofmeasure() {
        return unitofmeasure;
    }
    public void setUnitofmeasure(String unitofmeasure) {
        this.unitofmeasure = unitofmeasure;
    }
    public String getClasstype() {
        return classtype;
    }
    public void setClasstype(String classtype) {
        this.classtype = classtype;
    }
    public String getFinancialyear() {
        return financialyear;
    }
    public void setFinancialyear(String financialyear) {
        this.financialyear = financialyear;
    }
    public Double getUnitfrom() {
        return unitfrom;
    }
    public void setUnitfrom(Double unitfrom) {
        this.unitfrom = unitfrom;
    }
    public Double getUnitto() {
        return unitto;
    }
    public void setUnitto(Double unitto) {
        this.unitto = unitto;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public Double getUnitfactor() {
        return unitfactor;
    }
    public void setUnitfactor(Double unitfactor) {
        this.unitfactor = unitfactor;
    }
}
