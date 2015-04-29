package org.egov.works.models.contractorBill;

import java.io.Serializable;

import org.egov.commons.CChartOfAccounts;
import org.egov.model.budget.BudgetGroup;

public class DepositCOABudgetHead implements Serializable {
	private Long id;
	private String depositCOA;
	private String workDoneBudgetGroup;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDepositCOA() {
		return depositCOA;
	}
	public void setDepositCOA(String depositCOA) {
		this.depositCOA = depositCOA;
	}
	public String getWorkDoneBudgetGroup() {
		return workDoneBudgetGroup;
	}
	public void setWorkDoneBudgetGroup(String workDoneBudgetGroup) {
		this.workDoneBudgetGroup = workDoneBudgetGroup;
	}
	
}
