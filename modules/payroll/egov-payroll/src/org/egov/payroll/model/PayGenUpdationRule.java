package org.egov.payroll.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.egov.commons.CFinancialYear;
import org.egov.pims.model.EmployeeGroupMaster;

public class PayGenUpdationRule implements Serializable 
{
	private Integer id;
	private SalaryCodes salaryCodes;
	private BigDecimal percentage;
	private BigDecimal monthlyAmt;
	private BigDecimal month;
	private CFinancialYear financialyear;
	private Date lastmodifieddate;
	private Date effectivedate;
	private EmployeeGroupMaster empGroupMstrs;
	public Date getLastmodifieddate() {
		return lastmodifieddate;
	}
	public void setLastmodifieddate(Date lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
	}
	
	/**
	 * @return the effectivedate
	 */
	public Date getEffectivedate() {
		return effectivedate;
	}
	/**
	 * @param effectivedate the effectivedate to set
	 */
	public void setEffectivedate(Date effectivedate) {
		this.effectivedate = effectivedate;
	}
	public PayGenUpdationRule()
	{
		
	}
	public PayGenUpdationRule(Integer id,SalaryCodes salaryCodes,BigDecimal percentage,
			BigDecimal monthlyAmt,BigDecimal month,CFinancialYear financialyear)
	{
		this.id=id;
		this.salaryCodes=salaryCodes;
		this.percentage=percentage;
		this.monthlyAmt=monthlyAmt;
		this.month=month;
		this.financialyear=financialyear;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public SalaryCodes getSalaryCodes() {
		return salaryCodes;
	}
	public void setSalaryCodes(SalaryCodes salaryCodes) {
		this.salaryCodes = salaryCodes;
	}
	public BigDecimal getPercentage() {
		return percentage;
	}
	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}
	public BigDecimal getMonthlyAmt() {
		return monthlyAmt;
	}
	public void setMonthlyAmt(BigDecimal monthlyAmt) {
		this.monthlyAmt = monthlyAmt;
	}
	public BigDecimal getMonth() {
		return month;
	}
	public void setMonth(BigDecimal month) {
		this.month = month;
	}
	public CFinancialYear getFinancialyear() {
		return financialyear;
	}
	public void setFinancialyear(CFinancialYear financialyear) {
		this.financialyear = financialyear;
	}
	public EmployeeGroupMaster getEmpGroupMstrs() {
		return empGroupMstrs;
	}
	public void setEmpGroupMstrs(EmployeeGroupMaster empGroupMstrs) {
		this.empGroupMstrs = empGroupMstrs;
	}	
	
}
