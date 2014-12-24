package org.egov.payroll.model;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.commons.CFinancialYear;
import org.egov.lib.rjbac.user.User;
import org.egov.pims.model.PersonalInformation;

public class IncrementDetails implements java.io.Serializable{
	private Integer id;
	private Date incrementDate;
	private CFinancialYear financialyear;
	private BigDecimal month;
    private PersonalInformation employee;
	private String remarks;
	private User createdby;
	private Date createddate;
	private Integer status ;
	private BigDecimal amount ;
	
	public Integer getId()
	{
		return this.id;
	}
	public void setId(Integer id)
	{
		this.id=id;
	}
	public CFinancialYear getFinancialyear() {
		return financialyear;
	}
	public void setFinancialyear(CFinancialYear financialayear) {
		this.financialyear = financialayear;
	}
	public BigDecimal getMonth() {
		return month;
	}
	public void setMonth(BigDecimal month) {
		this.month = month;
	}
	public IncrementDetails(Integer id,CFinancialYear financialyear,
			BigDecimal month,Date incrementdate,String remarks,PersonalInformation employee,
			User createdby,Date createddate,Integer status,BigDecimal amount) {
		this.id = id;
		this.financialyear=financialyear;
		this.month = month;
		this.remarks = remarks;
		this.employee=employee;
		this.createdby=createdby;
		this.createddate=createddate;
		this.status = status ;	
		this.amount = amount ;
		this.incrementDate = incrementdate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public IncrementDetails() {
		// TODO Auto-generated constructor stub
	}
	public PersonalInformation getEmployee() {
		return employee;
	}
	public void setEmployee(PersonalInformation employee) {
		this.employee = employee;
	}	
	public User getCreatedby() {
		return createdby;
	}
	public void setCreatedby(User createdby) {
		this.createdby = createdby;
	}
	public Date getCreateddate() {
		return createddate;
	}
	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {	
		this.status = status;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Date getIncrementDate() {
		return incrementDate;
	}
	public void setIncrementDate(Date incrementDate) {
		this.incrementDate = incrementDate;
	}
	
}