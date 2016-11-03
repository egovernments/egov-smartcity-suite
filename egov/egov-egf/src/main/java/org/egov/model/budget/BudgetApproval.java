package org.egov.model.budget;

import java.math.BigDecimal;

public class BudgetApproval {

    private Long id;
    
    private String department;
    
    private String parent;
    
    private String referenceBudget;
    
    private Long count;
    
    private BigDecimal reAmount;
    
    private BigDecimal beAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getReferenceBudget() {
        return referenceBudget;
    }

    public void setReferenceBudget(String referenceBudget) {
        this.referenceBudget = referenceBudget;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public BigDecimal getReAmount() {
        return reAmount;
    }

    public void setReAmount(BigDecimal reAmount) {
        this.reAmount = reAmount;
    }

    public BigDecimal getBeAmount() {
        return beAmount;
    }

    public void setBeAmount(BigDecimal beAmount) {
        this.beAmount = beAmount;
    }

    
}
