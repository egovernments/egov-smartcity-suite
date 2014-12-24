package org.egov.payroll.web.actions.reports;

import org.apache.log4j.Logger;
import org.displaytag.pagination.PaginatedList;
import org.egov.infstr.search.SearchQuery;
import org.egov.payroll.services.reports.PayrollReportService;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.web.actions.SearchFormAction;

@SuppressWarnings("serial")
public abstract class CommonSearchReport extends SearchFormAction {

	private static final Logger LOGGER=Logger.getLogger(CommonSearchReport.class);
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		return null;
	}
	protected PayrollReportService payrollReportService;
	protected PayrollExternalInterface payrollExternalInterface;
	protected PaginatedList salaryDeductionList;
	protected Integer month=0;
	protected Integer year=0;
	protected Integer department;
	protected String monthStr;
	protected String yearStr;
	protected String departmentStr;
	protected String billNumber;
	protected Integer billNumberId;
	protected String billNumberHeading="";
	
	public String getYearStr() {
		return yearStr;
	}
	public void setYearStr(String yearStr) {
		this.yearStr = yearStr;
	}
	public String getDepartmentStr() {
		return departmentStr;
	}
	public void setDepartmentStr(String departmentStr) {
		this.departmentStr = departmentStr;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getDepartment() {
		return department;
	}
	public void setDepartment(Integer department) {
		this.department = department;
	}
	public String getMonthStr() {
		return monthStr;
	}
	public void setMonthStr(String monthStr) {
		this.monthStr = monthStr;
	}
	public PaginatedList getSalaryDeductionList() {
		return salaryDeductionList;
	}
	public void setSalaryDeductionList(PaginatedList salaryDeductionList) {
		this.salaryDeductionList = salaryDeductionList;
	}
	public PayrollReportService getPayrollReportService() {
		return payrollReportService;
	}
	public void setPayrollReportService(PayrollReportService payrollReportService) {
		this.payrollReportService = payrollReportService;
	}
	public PayrollExternalInterface getPayrollExternalInterface() {
		return payrollExternalInterface;
	}
	public void setPayrollExternalInterface(
			PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
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
	public String getBillNumberHeading() {
		return billNumberHeading;
	}
	public void setBillNumberHeading(String billNumberHeading) {
		this.billNumberHeading = billNumberHeading;
	}
	

}
