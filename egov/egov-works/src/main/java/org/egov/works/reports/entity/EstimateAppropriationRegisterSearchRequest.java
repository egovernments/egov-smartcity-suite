package org.egov.works.reports.entity;

import java.util.Date;

public class EstimateAppropriationRegisterSearchRequest {

    private Date asOnDate;
    private Long budgetHead;
    private Long fund;
    private Long function;
    private Long department;
    private Long financialYear;

    public Date getAsOnDate() {
        return asOnDate;
    }

    public void setAsOnDate(final Date asOnDate) {
        this.asOnDate = asOnDate;
    }

    public Long getBudgetHead() {
        return budgetHead;
    }

    public void setBudgetHead(final Long budgetHead) {
        this.budgetHead = budgetHead;
    }

    public Long getFund() {
        return fund;
    }

    public void setFund(final Long fund) {
        this.fund = fund;
    }

    public Long getFunction() {
        return function;
    }

    public void setFunction(final Long function) {
        this.function = function;
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(final Long department) {
        this.department = department;
    }

    public Long getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(final Long financialYear) {
        this.financialYear = financialYear;
    }

}
