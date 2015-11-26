package org.egov.ptis.domain.model;

import java.math.BigDecimal;

public class RestPropertyTaxDetails {

	private String installment="";
	private BigDecimal taxAmount =BigDecimal.ZERO;
	private BigDecimal chqBouncePenalty=BigDecimal.ZERO;
	private BigDecimal penalty=BigDecimal.ZERO;
	private BigDecimal rebate=BigDecimal.ZERO;
	private BigDecimal totalAmount=BigDecimal.ZERO;

	public String getInstallment() {
		return installment;
	}
	public void setInstallment(String installment) {
		this.installment = installment;
	}
	public BigDecimal getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}
	public BigDecimal getChqBouncePenalty() {
		return chqBouncePenalty;
	}
	public void setChqBouncePenalty(BigDecimal chqBouncePenalty) {
		this.chqBouncePenalty = chqBouncePenalty;
	}
	public BigDecimal getPenalty() {
		return penalty;
	}
	public void setPenalty(BigDecimal penalty) {
		this.penalty = penalty;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public BigDecimal getRebate() {
		return rebate;
	}
	public void setRebate(BigDecimal rebate) {
		this.rebate = rebate;
	}

}
