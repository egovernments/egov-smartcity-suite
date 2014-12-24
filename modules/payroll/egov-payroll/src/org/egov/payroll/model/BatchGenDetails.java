package org.egov.payroll.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.infstr.models.BaseModel;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.user.User;
import org.egov.masters.model.BillNumberMaster;

public class BatchGenDetails extends BaseModel implements java.io.Serializable
{
	private DepartmentImpl department;
	private Functionary functionary;
	private Date fromDate;
	private Date toDate;
	private CFinancialYear financialyear;
	private BigDecimal month;
	private String remarks;
	private Integer succCount=0;
	private Integer failCount=0;
	private Integer status = 1;
	private String schJobGroupName;	
	private String schJobName;
	private CFunction function; 
	private BillNumberMaster billNumber;
	
	public Integer getFailCount() {
		return failCount;
	}
	public void setFailCount(Integer failcount) {
		this.failCount = failcount;
	}
	public Integer getSuccCount() {
		return succCount;
	}
	public void setSuccCount(Integer succcount) {
		this.succCount = succcount;
	}
	public DepartmentImpl getDepartment()
	{
		return this.department;
	}
	public void setDepartment(DepartmentImpl dept)
	{
		this.department=dept;
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
	public void setFinancialyear(CFinancialYear financialyear) {
		this.financialyear = financialyear;
	}
	public BigDecimal getMonth() {
		return month;
	}
	public void setMonth(BigDecimal month) {
		this.month = month;
	}
	public BatchGenDetails(Long id,CFinancialYear financialyear,
			BigDecimal month,DepartmentImpl department,Date fromdate,Date todate,User createdby,Date createddate,
			String remarks,Integer succcount,Integer failcount,Integer status,String schJobName,String schJobGroupName,User modifiedby,Timestamp modifieddate) {
		this.id = id;
		this.financialyear=financialyear;
		this.month = month;
		this.toDate=todate;
		this.fromDate=fromdate;
		this.department=department;
		this.createdBy=createdby;
		this.createdDate=createddate;
		this.modifiedBy=modifiedby;
		this.modifiedDate=modifieddate;
		this.remarks = remarks;
		this.succCount=succcount;
		this.failCount=failcount;
		this.status = status;
		this.schJobGroupName=schJobGroupName;
		this.schJobName=schJobName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public BatchGenDetails() {
		// TODO Auto-generated constructor stub
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Functionary getFunctionary() {
		return functionary;
	}
	public void setFunctionary(Functionary functionary) {
		this.functionary = functionary;
	}
	public String getSchJobGroupName() {
		return schJobGroupName;
	}
	public void setSchJobGroupName(String schJobGroupName) {
		this.schJobGroupName = schJobGroupName;
	}
	public String getSchJobName() {
		return schJobName;
	}
	public void setSchJobName(String schJobName) {
		this.schJobName = schJobName;
	}
	public CFunction getFunction() {
		return function;
	}
	public void setFunction(CFunction function) {
		this.function = function;
	}
	public BillNumberMaster getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(BillNumberMaster billNumber) {
		this.billNumber = billNumber;
	}
	
}