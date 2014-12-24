package org.egov.payroll.reports;

import org.apache.struts.action.ActionForm;

public class SearchForm extends ActionForm{
	private String deptid;
	private String empid;
	private String fromdate;
	private String todate;
	private String month;
	private String finYr;
	private String type;
	private String fromMonth;
	private String toMonth;
	private String fromFinYr;
	private String toFinYr;
	private String billNumber;
	private Integer billNumberId;
	
	//search results 
	private String[] deptids;  
	private String[] deptnames;
	private String[] empids;
	private String[] empnames;
	private String[] empcodes;
	private String[] fromdates;
	private String[] todates;
	private String[] remarks;
	private String[] paytypes;
	private String functionaryId;
	private String errorPay;
    private String empType ="0";
    private String functionaryIds[];
    private String ess = "0";
    
	public String[] getFunctionaryIds() {
		return functionaryIds;
	}
	public void setFunctionaryIds(String[] functionaryIds) {
		this.functionaryIds = functionaryIds;
	}
	public String getEmpType() {
		return empType;
	}
	public void setEmpType(String empType) {
		this.empType = empType;
	}
	public String getErrorPay() {
		return errorPay;
	}
	public void setErrorPay(String errorPay) {
		this.errorPay = errorPay;
	}
	
	public String getFunctionaryId() {
		return functionaryId;
	}
	public void setFunctionaryId(String functionaryId) {
		this.functionaryId = functionaryId;
	}
	public String[] getPaytypes() {
		return paytypes;
	}
	public void setPaytypes(String[] paytypes) {
		this.paytypes = paytypes;
	}
	public String[] getRemarks() {
		return remarks;
	}
	public void setRemarks(String[] remarks) {
		this.remarks = remarks;
	}
	public String getDeptid() {
		return deptid;
	}
	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}
	public String getEmpid() {
		return empid;
	}
	public void setEmpid(String employee) {
		this.empid = employee;
	}
	public String getFinYr() {
		return finYr;
	}
	public void setFinYr(String finYr) {
		this.finYr = finYr;
	}
	public String getFromdate() {
		return fromdate;
	}
	public void setFromdate(String fromdate) {
		this.fromdate = fromdate;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getTodate() {
		return todate;
	}
	public void setTodate(String todate) {
		this.todate = todate;
	}
	public String[] getDeptids() {
		return deptids;
	}
	public void setDeptids(String[] deptids) {
		this.deptids = deptids;
	}
	public String[] getDeptnames() {
		return deptnames;
	}
	public void setDeptnames(String[] deptnames) {
		this.deptnames = deptnames;
	}
	public String[] getEmpids() {
		return empids;
	}
	public void setEmpids(String[] empids) {
		this.empids = empids;
	}
	public String[] getEmpnames() {
		return empnames;
	}
	public void setEmpnames(String[] empnames) {
		this.empnames = empnames;
	}
	
	public String[] getFromdates() {
		return fromdates;
	}
	public void setFromdates(String[] fromdates) {
		this.fromdates = fromdates;
	}
	public String[] getTodates() {
		return todates;
	}
	public void setTodates(String[] todates) {
		this.todates = todates;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String[] getEmpcodes() {
		return empcodes;
	}
	public void setEmpcodes(String[] empcodes) {
		this.empcodes = empcodes;
	}
	public String getFromMonth() {
		return fromMonth;
	}
	public void setFromMonth(String fromMonth) {
		this.fromMonth = fromMonth;
	}
	public String getToMonth() {
		return toMonth;
	}
	public void setToMonth(String toMonth) {
		this.toMonth = toMonth;
	}
	public String getFromFinYr() {
		return fromFinYr;
	}
	public void setFromFinYr(String fromFinYr) {
		this.fromFinYr = fromFinYr;
	}
	public String getToFinYr() {
		return toFinYr;
	}
	public void setToFinYr(String toFinYr) {
		this.toFinYr = toFinYr;
	}
	public String getEss() {
		return ess;
	}
	public void setEss(String ess) {
		this.ess = ess;
	}

	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public Integer getBillNumberId() {
		return billNumberId;
	}
	public void setBillNumberId(Integer billNumberId) {
		this.billNumberId = billNumberId;     
	}
	
	
}
