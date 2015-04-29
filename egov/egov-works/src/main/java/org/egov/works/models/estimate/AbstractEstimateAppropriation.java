package org.egov.works.models.estimate;

import java.math.BigDecimal;

import org.egov.infstr.models.BaseModel;
import org.egov.model.budget.BudgetUsage;

public class AbstractEstimateAppropriation extends BaseModel{

	private AbstractEstimate abstractEstimate;
	private BudgetUsage budgetUsage;
	private BigDecimal balanceAvailable;
	private DepositWorksUsage depositWorksUsage;
	
	public DepositWorksUsage getDepositWorksUsage() {
		return depositWorksUsage;
	}

	public void setDepositWorksUsage(DepositWorksUsage depositWorksUsage) {
		this.depositWorksUsage = depositWorksUsage;
	}

	public BudgetUsage getBudgetUsage() {
		return budgetUsage;
	}

	public void setBudgetUsage(BudgetUsage budgetUsage) {
		this.budgetUsage = budgetUsage;
	}

	public AbstractEstimate getAbstractEstimate() { 
		return abstractEstimate;
	}

	public void setAbstractEstimate(AbstractEstimate abstractEstimate) {
		this.abstractEstimate = abstractEstimate;
	}

	public BigDecimal getBalanceAvailable() {
		return balanceAvailable;
	}

	public void setBalanceAvailable(BigDecimal balanceAvailable) {
		this.balanceAvailable = balanceAvailable;
	}

}