package org.egov.web.actions.budget;

import java.math.BigDecimal;

import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.utils.Constants;

public class BudgetReAppropriationView{
	Long id;
	private BudgetDetail budgetDetail = new BudgetDetail();
	Budget budget;
	private BigDecimal deltaAmount = new BigDecimal(Constants.ZERO);
	private BigDecimal approvedDeltaAmount = new BigDecimal(Constants.ZERO);
	private BigDecimal addedReleased = new BigDecimal(Constants.ZERO);
	private BigDecimal approvedAmount = new BigDecimal(Constants.ZERO);
	private BigDecimal appropriatedAmount = new BigDecimal(Constants.ZERO);
	private BigDecimal actuals = new BigDecimal(Constants.ZERO);
	private BigDecimal anticipatoryAmount = new BigDecimal(Constants.ZERO);
	private BigDecimal availableAmount = new BigDecimal(Constants.ZERO);
	String changeRequestType;
	String sequenceNumber;
	String comments;
	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}

	public void setDeltaAmount(BigDecimal deltaAmount) {
		this.deltaAmount = deltaAmount;
	}

	public Budget getBudget() {
		return budget;
	}

	public BigDecimal getDeltaAmount() {
		return deltaAmount;
	}

	public BigDecimal getApprovedAmount() {
		return approvedAmount;
	}

	public BigDecimal getActuals() {
		return actuals;
	}

	public String getChangeRequestType() {
		return changeRequestType;
	}

	public void setApprovedAmount(BigDecimal approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public void setActuals(BigDecimal actuals) {
		this.actuals = actuals;
	}

	public void setChangeRequestType(String changeRequestType) {
		this.changeRequestType = changeRequestType;
	}

	public BigDecimal getAnticipatoryAmount() {
		return anticipatoryAmount;
	}

	public void setAnticipatoryAmount(BigDecimal anticipatoryAmount) {
		this.anticipatoryAmount = anticipatoryAmount;
	}

	public BudgetDetail getBudgetDetail() {
		return budgetDetail;
	}
	
	public void setBudgetDetail(BudgetDetail budgetDetail) {
		this.budgetDetail = budgetDetail;
	}

	public void setAppropriatedAmount(BigDecimal appropriatedAmount) {
		this.appropriatedAmount = appropriatedAmount;
	}

	public BigDecimal getAppropriatedAmount() {
		return appropriatedAmount;
	}

	public void setAddedReleased(BigDecimal addedReleased) {
		this.addedReleased = addedReleased;
	}

	public BigDecimal getAddedReleased() {
		return addedReleased;
	}

	public void setApprovedDeltaAmount(BigDecimal approvedDeltaAmount) {
		this.approvedDeltaAmount = approvedDeltaAmount;
	}

	public BigDecimal getApprovedDeltaAmount() {
		return approvedDeltaAmount;
	}
}