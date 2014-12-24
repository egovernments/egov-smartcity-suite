package org.egov.model.budget;

import java.math.BigDecimal;

import org.egov.infstr.models.StateAware;

public class BudgetReAppropriation extends StateAware{
	private Long id = null;
	private BudgetDetail budgetDetail;
	private BigDecimal additionAmount = new BigDecimal("0.0");
	private BigDecimal deductionAmount = new BigDecimal("0.0");
	private BigDecimal originalAdditionAmount = new BigDecimal("0.0");
	private BigDecimal originalDeductionAmount = new BigDecimal("0.0");
	private BigDecimal anticipatoryAmount = new BigDecimal("0.0");
	private BudgetReAppropriationMisc reAppropriationMisc;

	public BudgetReAppropriationMisc getReAppropriationMisc() {
		return reAppropriationMisc;
	}

	public void setReAppropriationMisc(BudgetReAppropriationMisc reAppropriationMisc) {
		this.reAppropriationMisc = reAppropriationMisc;
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
	
	public BigDecimal getAdditionAmount() {
		return additionAmount;
	}

	public void setAdditionAmount(BigDecimal additionAmount) {
		this.additionAmount = additionAmount;
	}

	public BigDecimal getDeductionAmount() {
		return deductionAmount;
	}

	public void setDeductionAmount(BigDecimal deductionAmount) {
		this.deductionAmount = deductionAmount;
	}

	public void setBudgetDetail(BudgetDetail budgetDetail) {
		this.budgetDetail = budgetDetail;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public String getStateDetails() {
		return null;
	}

	public void setOriginalAdditionAmount(BigDecimal originalAdditionAmount) {
		this.originalAdditionAmount = originalAdditionAmount;
	}

	public BigDecimal getOriginalAdditionAmount() {
		return originalAdditionAmount;
	}

	public void setOriginalDeductionAmount(BigDecimal originalDeductionAmount) {
		this.originalDeductionAmount = originalDeductionAmount;
	}

	public BigDecimal getOriginalDeductionAmount() {
		return originalDeductionAmount;
	}
	
}
