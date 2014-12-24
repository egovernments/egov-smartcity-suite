package org.egov.payroll.model;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.user.User;
import org.egov.masters.model.BillNumberMaster;
import org.egov.pims.model.PersonalInformation;

public class BatchFailureDetails implements java.io.Serializable
{
	private Integer id;
	private Date fromDate;
	private Date toDate;
	private CFinancialYear financialyear;
	private BigDecimal month;
    private PersonalInformation employee;
	private String remarks;
	private PayTypeMaster payType ;
	private User createdby;
	private Date createddate;
	private Integer status ;
	private String isHistory ="N" ;
	private DepartmentImpl department;
	private Functionary functionary;
	private CFunction function;
	private BillNumberMaster billNumber;
	
	public CFunction getFunction() {
		return function;
	}
	public void setFunction(CFunction function) {
		this.function = function;
	}
	public Functionary getFunctionary() {
		return functionary;
	}
	public void setFunctionary(Functionary functionary) {
		this.functionary = functionary;
	}
	public Integer getId()
	{
		return this.id;
	}
	public void setId(Integer id)
	{
		this.id=id;
	}

	public Date getFromDate()
	{
		return this.fromDate;
	}
	public void setFromDate(Date fromDate)
	{
		this.fromDate=fromDate;
	}
	public Date getToDate()
	{
		return this.toDate;
	}
	public void setToDate(Date toDate)
	{
		this.toDate=toDate;
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
	public DepartmentImpl getDepartment()
	{
		return this.department;
	}
	public void setDepartment(DepartmentImpl dept)
	{
		this.department=dept;
	}
	public BatchFailureDetails(Integer id,CFinancialYear financialyear,
			BigDecimal month,Date fromdate,Date todate,String remarks,PersonalInformation employee,
			PayTypeMaster paytype,DepartmentImpl department,User createdby,Date createddate,Integer status,String isHistory) {
		this.id = id;
		this.financialyear=financialyear;
		this.month = month;
		this.toDate=todate;
		this.fromDate=fromdate;
		this.remarks = remarks;
		this.employee=employee;
		this.payType=paytype;
		this.createdby=createdby;
		this.createddate=createddate;
		this.department=department;
		this.status = status ;
		this.isHistory=isHistory;
		
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public BatchFailureDetails() {
		// TODO Auto-generated constructor stub
	}
	public PersonalInformation getEmployee() {
		return employee;
	}
	public void setEmployee(PersonalInformation employee) {
		this.employee = employee;
	}
	public PayTypeMaster getPayType() {
		return payType;
	}
	public void setPayType(PayTypeMaster payType) {
		this.payType = payType;
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
	public String getIsHistory() {
		return isHistory;
	}
	public void setIsHistory(String isHistory) {
		this.isHistory = isHistory;
	}
	public BillNumberMaster getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(BillNumberMaster billNumber) {
		this.billNumber = billNumber;
	}


}