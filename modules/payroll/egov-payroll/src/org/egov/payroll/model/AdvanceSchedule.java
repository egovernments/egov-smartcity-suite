package org.egov.payroll.model;

// Generated Aug 28, 2007 7:45:39 PM by Hibernate Tools 3.2.0.b9

import java.math.BigDecimal;

/**
 * @author surya
 */
public class AdvanceSchedule implements java.io.Serializable {

	private Integer id;
	private Advance advance;
	private Integer installmentNo;
	private BigDecimal principalAmt;
	private BigDecimal interestAmt;
	private String recover;
	private Deductions egPayDeductions;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Advance getAdvance() {
		return advance;
	}
	public void setAdvance(Advance advance) {
		this.advance = advance;
	}
	public Integer getInstallmentNo() {
		return installmentNo;
	}
	public void setInstallmentNo(Integer installmentNo) {
		this.installmentNo = installmentNo;
	}
	public BigDecimal getPrincipalAmt() {
		return principalAmt;
	}
	public void setPrincipalAmt(BigDecimal principalAmt) {
		this.principalAmt = principalAmt;
	}
	public BigDecimal getInterestAmt() {
		return interestAmt;
	}
	public void setInterestAmt(BigDecimal interestAmt) {
		this.interestAmt = interestAmt;
	}
	public String getRecover() {
		return recover;
	}
	public void setRecover(String recover) {
		this.recover = recover;
	}
	public Deductions getEgPayDeductions() {
		return egPayDeductions;
	}
	public void setEgPayDeductions(Deductions egPayDeductions) {
		this.egPayDeductions = egPayDeductions;
	}
	
}
