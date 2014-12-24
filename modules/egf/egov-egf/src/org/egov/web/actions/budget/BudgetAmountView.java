package org.egov.web.actions.budget;

import java.math.BigDecimal;

import org.egov.utils.Constants;

public class BudgetAmountView {
	private BigDecimal previousYearActuals = BigDecimal.ZERO;
	private String oldActuals = Constants.ZERO;
	private String reappropriation = Constants.ZERO;
	private BigDecimal currentYearBeActuals = BigDecimal.ZERO;
	private String currentYearBeApproved = Constants.ZERO;
	private String currentYearReApproved = Constants.ZERO;
	private String total = Constants.ZERO;
	private Long id;
	public BigDecimal getPreviousYearActuals() {
		return previousYearActuals;
	}
	
	public String getReappropriation() {
		return reappropriation;
	}
	public String getOldActuals() {
		return oldActuals;
	}
	public BigDecimal getCurrentYearBeActuals() {
		return currentYearBeActuals;
	}
	public String getCurrentYearBeApproved() {
		return currentYearBeApproved;
	}
	public String getCurrentYearReApproved() {
		return currentYearReApproved;
	}
	public void setPreviousYearActuals(BigDecimal previousYearActuals) {
		this.previousYearActuals = previousYearActuals;
	}
	public void setReappropriation(String reappropriation) {
		this.reappropriation = reappropriation;
	}
	public void setOldActuals(String oldActuals) {
		this.oldActuals = oldActuals;
	}
	public void setCurrentYearBeActuals(BigDecimal currentYearBeActuals) {
		this.currentYearBeActuals = currentYearBeActuals;
	}
	public void setCurrentYearBeApproved(String currentYearBeApproved) {
		this.currentYearBeApproved = currentYearBeApproved;
	}
	public void setCurrentYearReApproved(String currentYearReApproved) {
		this.currentYearReApproved = currentYearReApproved;
	}
	public void setTotal(String total) {
		this.total = total;
	}

	public String getTotal() {
		return total;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}
