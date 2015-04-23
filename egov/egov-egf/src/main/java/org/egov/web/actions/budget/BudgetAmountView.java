package org.egov.web.actions.budget;

import java.math.BigDecimal;

import org.egov.utils.Constants;

public class BudgetAmountView {
	

	private BigDecimal previousYearActuals = BigDecimal.ZERO;
	private BigDecimal twopreviousYearActuals = BigDecimal.ZERO;
	private String oldActuals = Constants.ZERO;
	private String reappropriation = Constants.ZERO;
	private BigDecimal currentYearBeActuals = BigDecimal.ZERO;
	private String currentYearBeApproved = Constants.ZERO;
	private String currentYearReApproved = Constants.ZERO;
	private String total = Constants.ZERO;
	private Long id;
	private String LastBEApproved=Constants.ZERO;
	private String lastTotal=Constants.ZERO;	
	/**
	 * @return the lastBEApproved
	 */
	public String getLastBEApproved() {
		return LastBEApproved;
	}

	/**
	 * @return the lastTotal
	 */
	public String getLastTotal() {
		return lastTotal;
	}

	/**
	 * @param lastTotal the lastTotal to set
	 */
	public void setLastTotal(String lastTotal) {
		this.lastTotal = lastTotal;
	}

	/**
	 * @param lastBEApproved the lastBEApproved to set
	 */
	public void setLastBEApproved(String lastBEApproved) {
		LastBEApproved = lastBEApproved;
	}

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

	public BigDecimal getTwopreviousYearActuals() {
		return twopreviousYearActuals;
	}

	public void setTwopreviousYearActuals(BigDecimal twopreviousYearActuals) {
		this.twopreviousYearActuals = twopreviousYearActuals;
	}
	@Override
	public String toString() {
	 return new StringBuffer(500).append(id).append("--").append(currentYearBeActuals).append("--")
	 .append(previousYearActuals).append("--").append(twopreviousYearActuals).toString();	}
}
