/**
 * Class to populate installment wise tax and penalty
 */
package org.egov.ptis.nmc.model;

import java.math.BigDecimal;

import org.egov.commons.Installment;

public class PropertyInstTaxBean {
	private Installment installment;
	private Integer installmentId;
	private String installmentStr;
	private BigDecimal instTaxAmt;
	private BigDecimal instCollAmt;
	private BigDecimal instPenaltyAmt;

	public Installment getInstallment() {
		return installment;
	}

	public void setInstallment(Installment installment) {
		this.installment = installment;
	}

	public Integer getInstallmentId() {
		return installmentId;
	}

	public void setInstallmentId(Integer installmentId) {
		this.installmentId = installmentId;
	}

	public String getInstallmentStr() {
		return installmentStr;
	}

	public void setInstallmentStr(String installmentStr) {
		this.installmentStr = installmentStr;
	}

	public BigDecimal getInstTaxAmt() {
		return instTaxAmt;
	}

	public void setInstTaxAmt(BigDecimal instTaxAmt) {
		this.instTaxAmt = instTaxAmt;
	}

	public BigDecimal getInstCollAmt() {
		return instCollAmt;
	}

	public void setInstCollAmt(BigDecimal instCollAmt) {
		this.instCollAmt = instCollAmt;
	}

	public BigDecimal getInstPenaltyAmt() {
		return instPenaltyAmt;
	}

	public void setInstPenaltyAmt(BigDecimal instPenaltyAmt) {
		this.instPenaltyAmt = instPenaltyAmt;
	}
}
