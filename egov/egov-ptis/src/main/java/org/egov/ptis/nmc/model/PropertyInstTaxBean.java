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
	private BigDecimal instBalanceAmt;
	private BigDecimal instPenaltyAmt;
	private BigDecimal instRebateAmt;
	private BigDecimal instPenaltyCollAmt = BigDecimal.ZERO;

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

	public BigDecimal getInstBalanceAmt() {
		return instBalanceAmt;
	}

	public void setInstBalanceAmt(BigDecimal instBalanceAmt) {
		this.instBalanceAmt = instBalanceAmt;
	}

	public BigDecimal getInstPenaltyAmt() {
		return instPenaltyAmt;
	}

	public void setInstPenaltyAmt(BigDecimal instPenaltyAmt) {
		this.instPenaltyAmt = instPenaltyAmt;
	}

	public BigDecimal getInstRebateAmt() {
		return instRebateAmt;
	}

	public void setInstRebateAmt(BigDecimal instRebateAmt) {
		this.instRebateAmt = instRebateAmt;
	}

	public BigDecimal getInstPenaltyCollAmt() {
		return instPenaltyCollAmt;
	}

	public void setInstPenaltyCollAmt(BigDecimal instPenaltyCollAmt) {
		this.instPenaltyCollAmt = instPenaltyCollAmt;
	}
}
